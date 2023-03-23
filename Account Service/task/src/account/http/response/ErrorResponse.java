package account.http.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public final class ErrorResponse {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> data = new HashMap<>();


    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        data.put("timestamp", timestamp.toString());
        data.put("status", status);
        data.put("error", error);
        data.put("message", message);
        data.put("path", path);
    }


    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
