package account.exceptions.user;

import account.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Can't lock the ADMINISTRATOR!")
public class LockAdministratorException extends CustomException {
}
