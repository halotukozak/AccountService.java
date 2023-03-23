package account.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
