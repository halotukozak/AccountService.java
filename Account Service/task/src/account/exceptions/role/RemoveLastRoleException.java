package account.exceptions.role;

import account.db.model.Role;
import account.exceptions.role.RoleException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The user must have at least one role!")
public class RemoveLastRoleException extends RoleException {
}