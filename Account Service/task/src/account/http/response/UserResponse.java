package account.http.response;

import account.model.Role;
import account.model.User;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@ResponseBody
@Getter
public class UserResponse {
    private final Long id;
    private final String name;
    private final String lastname;
    private final String email;
    private final Collection<Role> roles;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
