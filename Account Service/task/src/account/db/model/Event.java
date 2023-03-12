package account.db.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime date;
    private ACTION action;
    private String subject;
    private String object;
    private String path;

    public Event(LocalDateTime date, String action, String subject, String object, String path) {
        this(date, ACTION.valueOf(action), subject, object, path);
    }

    public Event(LocalDateTime date, ACTION action, String subject, String object, String path) {
        this.date = date;
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }


    public enum ACTION {
        CREATE_USER, LOGIN_FAILED, GRANT_ROLE, REMOVE_ROLE, DELETE_USER, CHANGE_PASSWORD, ACCESS_DENIED, BRUTE_FORCE, LOCK_USER, UNLOCK_USER
    }
}
