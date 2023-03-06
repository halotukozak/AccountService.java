package account.http.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.OK)
public class DeletedUserResponse extends ResponseEntity<Map<String, String>> {

    public DeletedUserResponse(String email) {
        super(Map.of("user", email, "status", "Deleted successfully!"), HttpStatus.OK);
    }
}