package account.security;

import account.db.model.User;
import account.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    UserService userService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        String email = request.getParameter("email");
        if (email != null) {
            User user = (User) userService.loadUserByUsername(email);
            if (user != null) {
                if (user.isAccountNonLocked()) {
                    userService.increaseFailedAttempts(user);
                }
            }
        }

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