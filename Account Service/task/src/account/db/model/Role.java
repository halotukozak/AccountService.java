package account.db.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Role {

    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMINISTRATOR";
    public static final String ACCOUNTANT = "ROLE_ACCOUNTANT";
    public static final String AUDITOR = "ROLE_AUDITOR";
    public static final List<String> BUSINESS = List.of(USER, ACCOUNTANT, AUDITOR);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private final Collection<Privilege> privileges = new LinkedHashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges.addAll(privileges);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
