package account.http.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.OK)
public class ChangeUserLockResponse extends ResponseEntity<Map<String, String>> {

    public ChangeUserLockResponse(String email, String operation) {
        super(Map.of("status", "User " + email + " " + operation + "!"), HttpStatus.OK);
    }

    public static ChangeUserLockResponse lock(String email){
        return new ChangeUserLockResponse(email, "locked");
    }

    public static ChangeUserLockResponse unlock(String email){
        return new ChangeUserLockResponse(email, "unlocked");
    }
}