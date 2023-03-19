package account.exceptions.role;

import account.exceptions.role.RoleException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Can't remove ADMINISTRATOR role!")
public class RemoveAdministratorException  extends RoleException { }