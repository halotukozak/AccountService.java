package account.controller;

import account.db.model.Event;
import account.db.model.User;
import account.exceptions.InvalidMethodException;
import account.http.request.ChangeLockRequest;
import account.http.request.GiveRoleRequest;
import account.http.response.ChangeUserLockResponse;
import account.http.response.DeletedUserResponse;
import account.http.response.UserResponse;
import account.service.EventService;
import account.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/user")
@Validated
public class AdminController {
    private final UserService userService;
    private final EventService eventService;


    public AdminController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping
    List<UserResponse> getUser() {
        List<User> users = userService.loadAllUsers();
        return users.stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @DeleteMapping("/{email}")
    DeletedUserResponse deleteUser(@PathVariable String email, @AuthenticationPrincipal UserDetails subject) {
        userService.deleteUserByEmail(email);
        eventService.registerEvent(Event.ACTION.DELETE_USER, email, subject.getUsername(), "/api/admin/user");
        return new DeletedUserResponse(email);
    }

    @PutMapping("/role")
    UserResponse manageRole(@RequestBody GiveRoleRequest request, @AuthenticationPrincipal UserDetails subject) {
        String roleName = "ROLE_" + request.role();
        User user = switch (request.operation()) {
            case "GRANT" -> userService.giveRole(request.user(), roleName);
            case "REMOVE" -> userService.removeRole(request.user(), roleName);
            default -> throw new InvalidMethodException();
        };
        eventService.registerEvent(request.operation().equals("GRANT") ? Event.ACTION.GRANT_ROLE : Event.ACTION.REMOVE_ROLE, request.user(), subject.getUsername(), "/api/admin/user/role");
        return new UserResponse(user);
    }

    @PutMapping("/access")
    ChangeUserLockResponse changeUserLock(@RequestBody ChangeLockRequest request, @AuthenticationPrincipal UserDetails subject) {
        final String path = "/api/admin/user/access";
        switch (request.operation()) {
            case "LOCK" -> {
                userService.lock(request.user());
                eventService.registerEvent(Event.ACTION.LOCK_USER, request.user(), subject.getUsername(), path);
                return ChangeUserLockResponse.lock(request.user());
            }
            case "UNLOCK" -> {
                userService.unlock(request.user());
                eventService.registerEvent(Event.ACTION.UNLOCK_USER, request.user(), subject.getUsername(), path);
                return ChangeUserLockResponse.unlock(request.user());
            }
            default -> throw new InvalidMethodException();
        }
    }
}
