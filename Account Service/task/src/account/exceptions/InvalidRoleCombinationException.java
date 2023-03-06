package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The user cannot combine administrative and business roles!")
public class InvalidRoleCombinationException extends RuntimeException {
}