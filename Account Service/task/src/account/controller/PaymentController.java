package account.controller;

import account.model.User;
import account.response.UserResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class PaymentController {
    @GetMapping("api/empl/payment")
    UserResponse payment(@AuthenticationPrincipal UserDetails userDetails) {
        return new UserResponse((User) userDetails);
    }
}
