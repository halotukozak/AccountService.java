package account.exceptions.user;

import account.exceptions.user.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found!")
public class EmailNotFoundException extends UserException {
}