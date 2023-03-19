package account.exceptions.payment;

import account.exceptions.payment.PaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This period is already occupied!")
public class OccupiedPeriodException extends PaymentException { }