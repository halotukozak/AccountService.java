package account.security;

import account.db.model.Event;
import account.db.model.User;
import account.service.EventService;
import account.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class AccessDeniedHandlerImpl extends SimpleUrlAuthenticationFailureHandler {


    private final ObjectMapper objectMapper = new ObjectMapper();

    final
    UserService userService;
    final
    EventService eventService;

    public AccessDeniedHandlerImpl(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

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

        eventService.registerEvent(Event.ACTION.LOGIN_FAILED, email, request.getRequestURI(), request.getRequestURI());

        super.onAuthenticationFailure(request, response, exception);
    }

}