package account.controller;

import account.model.User;
import account.request.ChangePasswordRequest;
import account.request.RegistrationRequest;
import account.response.ChangePasswordResponse;
import account.response.UserResponse;
import account.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/signup")
    UserResponse register(@Valid @RequestBody RegistrationRequest request) {
        User user = userService.createUser(request.name(), request.lastname(), request.email(), request.password());
        return new UserResponse(user);
    }


    @PostMapping("/auth/changepass")
    ChangePasswordResponse changePass(@Valid @RequestBody ChangePasswordRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        userService.changePassword(user, request.new_password());
        return new ChangePasswordResponse(user.getEmail());
    }

//    @PutMapping("/api/admin/user/role")
//    void changeUserRole(User user){
//        this.userList.get(user.)
//    }

//    @DeleteMapping("api/admin/user")
//    void deleteUser(User user) {
//        this.userList.remove(user);
//    }
//
//    @GetMapping("/api/admin/user")
//    public List<User> getUsers() {
//        return this.userList;
//    }

}
