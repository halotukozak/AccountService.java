package account.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {


    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Map<String, Object> data = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        data.put("timestamp", Calendar.getInstance().getTime());
        data.put("status", HttpServletResponse.SC_FORBIDDEN);
        data.put("error", "Forbidden");
        data.put("message", "Access Denied!");
        data.put("path", request.getRequestURI());

        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}