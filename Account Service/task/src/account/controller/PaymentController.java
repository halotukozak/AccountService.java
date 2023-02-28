package account.controller;

import account.model.Payment;
import account.model.User;
import account.request.PaymentRequest;
import account.response.OKResponse;
import account.response.PaymentResponse;
import account.service.PaymentService;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class PaymentController {
    private final UserService userService;
    private final PaymentService paymentService;


    @Autowired
    public PaymentController(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }


    @GetMapping("/empl/payment")
    ResponseEntity<?> getPayments(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) String period) {
        User user = (User) userDetails;
        if (period == null) {
            List<Payment> result = paymentService.getPayment(user.getEmail());
            return ResponseEntity.ok(PaymentResponse.of(result, user));
        } else {
            Payment result = paymentService.getPayment(user.getEmail(), period);
            return ResponseEntity.ok(PaymentResponse.of(result, user));
        }
    }

    @PostMapping("/acct/payments")
    OKResponse uploadPayment(@RequestBody List<@Valid PaymentRequest> paymentRequestList) {
        paymentService.createPayments(paymentRequestList);
        return new OKResponse("Added successfully!");

    }

    @PutMapping("/acct/payments")
    OKResponse changeSalary(@RequestBody @Valid PaymentRequest paymentRequest) {
        userService.loadUserByUsername(paymentRequest.employee());
        paymentService.updatePayment(paymentRequest.employee(), paymentRequest.period(), paymentRequest.salary());
        return new OKResponse("Updated successfully!");
    }

    @DeleteMapping()
    OKResponse deleteAll() {
        paymentService.deleteAll();
        userService.deleteAll();
        return new OKResponse("Deleted successfully");
    }

}
