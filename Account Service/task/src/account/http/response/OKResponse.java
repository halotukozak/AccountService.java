package account.http.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.OK)
public class OKResponse extends ResponseEntity<Map<String, String>> {

    public OKResponse(String message) {
        super(Map.of("status", message), HttpStatus.OK);
    }
}