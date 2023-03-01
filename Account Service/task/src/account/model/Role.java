package account.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;

@Entity
@Getter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Privilege> privileges = new LinkedHashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges.addAll(privileges);
    }
}
