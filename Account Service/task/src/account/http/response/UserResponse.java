package account.http.response;

import account.db.model.Role;
import account.db.model.User;
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
    private final Collection<String> roles;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(Role::getName).sorted().toList();
    }
}
