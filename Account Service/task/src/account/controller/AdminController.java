package account.controller;

import account.http.request.GiveRoleRequest;
import account.http.response.OKResponse;
import account.http.response.UserResponse;
import account.model.User;
import account.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/user")
@Validated
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    UserResponse getUser() {

        return new UserResponse((User) userService.loadUserByUsername(""));
    }

    @DeleteMapping
    OKResponse deleteUser() {
        userService.deleteUserByEmail("");
        return new OKResponse("");
    }

    @PutMapping("/role")
    OKResponse giveRole(@Valid @RequestBody GiveRoleRequest request) {

        return new OKResponse("");
    }
}
