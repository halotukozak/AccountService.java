package account.exceptions.role;

import account.exceptions.role.RoleException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Role not found!")
public class RoleNotFoundException extends RoleException { }