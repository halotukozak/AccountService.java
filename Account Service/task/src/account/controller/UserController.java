package account.controller;

import account.db.model.User;
import account.http.request.ChangePasswordRequest;
import account.http.request.RegistrationRequest;
import account.http.response.ChangePasswordResponse;
import account.http.response.UserResponse;
import account.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    UserResponse register(@Valid @RequestBody RegistrationRequest request) {
        User user = userService.registerUser(request.name(), request.lastname(), request.email(), request.password());
        return new UserResponse(user);
    }


    @PostMapping("/changepass")
    ChangePasswordResponse changePass(@Valid @RequestBody ChangePasswordRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        userService.changePassword(user, request.new_password());
        return new ChangePasswordResponse(user.getEmail());
    }
}
