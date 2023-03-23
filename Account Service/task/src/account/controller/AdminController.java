package account.controller;

import account.db.model.Event;
import account.db.model.User;
import account.exceptions.user.LockAdministratorException;
import account.exceptions.role.InvalidMethodException;
import account.http.request.ChangeLockRequest;
import account.http.request.GiveRoleRequest;
import account.http.response.ChangeUserLockResponse;
import account.http.response.DeletedUserResponse;
import account.http.response.UserResponse;
import account.service.EventService;
import account.service.FailedAttemptService;
import account.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/user")
@Validated
@AllArgsConstructor
public class AdminController {
    private final UserService userService;
    private final EventService eventService;
    private final FailedAttemptService failedAttemptService;


    @GetMapping
    List<UserResponse> getUser() {
        List<User> users = userService.loadAllUsers();
        return users.stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @DeleteMapping("/{email}")
    DeletedUserResponse deleteUser(@PathVariable String email, @AuthenticationPrincipal UserDetails subject) {
        userService.deleteUserByEmail(email);
        eventService.registerEvent(Event.ACTION.DELETE_USER, subject.getUsername(), email);
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
        List<String> message = List.of(request.operation().equals("GRANT") ? "Grant" : "Remove",
                "role", request.role(), request.operation().equals("GRANT") ? "to" : "from", user.getEmail());

        eventService.registerEvent(request.operation().equals("GRANT") ? Event.ACTION.GRANT_ROLE : Event.ACTION.REMOVE_ROLE,
                subject.getUsername(),
                String.join(" ", message));
        return new UserResponse(user);
    }

    @PutMapping("/access")
    ChangeUserLockResponse changeUserLock(@RequestBody ChangeLockRequest request, @AuthenticationPrincipal UserDetails subject) {
        User user = (User) userService.loadUserByUsername(request.user());
        switch (request.operation()) {
            case "LOCK" -> {
                if (user.isAdmin()) throw new LockAdministratorException();
                failedAttemptService.lock(user, subject.getUsername());
                return ChangeUserLockResponse.lock(user.getEmail());
            }
            case "UNLOCK" -> {
                failedAttemptService.unlock(user, subject.getUsername());
                return ChangeUserLockResponse.unlock(user.getEmail());
            }
            default -> throw new InvalidMethodException();
        }
    }
}
