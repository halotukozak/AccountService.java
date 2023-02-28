package account.response;

import account.model.User;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class UserResponse {
    private final Long id;
    private final String name;
    private final String lastname;
    private final String email;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
    }


    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
