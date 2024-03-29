package account.db.model;

import account.exceptions.role.RemoveAdministratorException;
import account.exceptions.role.RemoveLastRoleException;
import account.exceptions.role.UserDoesNotHaveRoleException;
import account.security.validation.NonPwnedPassword;
import lombok.Data;
import org.hibernate.annotations.SortNatural;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name cannot be blank") String name;
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "Lastname cannot be blank")
    private String lastname;
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^\\w+@acme\\.com$", message = "Email should follow the pattern.")

    private String email;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 12, message = "Password must have at least 12 characters!")
    @NonPwnedPassword()
    private String password;

    @SortNatural
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new LinkedHashSet<>();

    private final boolean isAccountNonExpired;

    private boolean isAccountNonLocked;
    private int failedAttempt = 0;

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
        this.setUsername(this.email);
        this.setPassword(password);
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPrivileges().stream().map(p -> new SimpleGrantedAuthority(p.getName())).forEach(authorities::add);
        }
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return isAccountNonExpired && isAccountNonLocked && isCredentialsNonExpired;
    }

    public boolean isAdmin() {
        return this.roles.stream().map(Role::getName).toList().contains(Role.ADMIN);
    }

    public boolean isBusinessUser() {
        return this.roles.stream().map(Role::getName).anyMatch(Role.BUSINESS::contains);
    }

    public void giveRole(Role role) {
        this.roles.add(role);
    }

    public boolean removeRole(Role role) {
        if (role.getName().equals(Role.ADMIN)) throw new RemoveAdministratorException();
        if (!this.roles.contains(role)) throw new UserDoesNotHaveRoleException();
        if (this.roles.size() < 2) throw new RemoveLastRoleException();
        return this.roles.remove(role);
    }

    @Override
    public String toString() {
        return this.email + ", " + this.roles;
    }

    public void increaseAttempt() {
        this.failedAttempt++;
    }

    public void resetFailedAttempts() {
        this.failedAttempt = 0;
    }

    public void lock() {
        this.isAccountNonLocked = false;
    }

    public void unlock() {
        this.isAccountNonLocked = true;

    }

    public boolean isAccountLocked() {
        return !isAccountNonLocked;
    }
}