package account.db.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Privilege {
    public static final String READ_PAYMENT = "READ_PAYMENT";
    public static final String UPLOAD_PAYMENT = "UPLOAD_PAYMENT";
    public static final String READ_USER = "READ_USER";
    public static final String REMOVE_USER = "REMOVE_USER";
    public static final String MANAGE_ROLE = "MANAGE_ROLE";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Privilege(String name) {
        this.name = name;
    }
}