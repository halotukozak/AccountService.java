package account.controller;

import account.exceptions.InvalidMethodException;
import account.http.request.GiveRoleRequest;
import account.http.response.DeletedUserResponse;
import account.http.response.UserResponse;
import account.model.User;
import account.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/user")
@Validated
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    List<UserResponse> getUser() {
        List<User> users = userService.loadAllUsers();
        return users.stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @DeleteMapping("/{email}")
    DeletedUserResponse deleteUser(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return new DeletedUserResponse(email);
    }

    @PutMapping("/role")
    UserResponse manageRole(@RequestBody GiveRoleRequest request) {
        String roleName = "ROLE_" + request.role();
        User user = switch (request.operation()) {
            case "GRANT" -> userService.giveRole(request.user(), roleName);
            case "REMOVE" -> userService.removeRole(request.user(), roleName);
            default -> throw new InvalidMethodException();
        };
        return new UserResponse(user);
    }
}
