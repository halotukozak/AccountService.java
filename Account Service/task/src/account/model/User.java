package account.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collection;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Name cannot be blank") String name;
    @GeneratedValue
    private String username;
    @NotBlank(message = "Lastname cannot be blank")
    private String lastname;
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^\\w+@acme\\.com$", message = "Email should follow the pattern.")

    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;

    private final boolean isAccountNonExpired;

    private final boolean isAccountNonLocked;

    private final boolean isCredentialsNonExpired;

    public User() {
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
    }

    public User(String firstname, String lastname, String email, String password) {
        this();
        this.setName(firstname);
        this.setLastname(lastname);
        this.setEmail(email);
        this.setPassword(password);
    }

    public Long getId() {
        return id;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}