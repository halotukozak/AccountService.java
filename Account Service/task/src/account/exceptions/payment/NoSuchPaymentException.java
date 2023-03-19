package account.exceptions.payment;

import account.exceptions.payment.PaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "There is no such payment!")
public class NoSuchPaymentException extends PaymentException { }