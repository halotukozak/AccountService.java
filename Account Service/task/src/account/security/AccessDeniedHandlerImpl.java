package account.security;

import account.db.model.Event;
import account.db.model.User;
import account.http.response.ErrorResponse;
import account.service.EventService;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public AccessDeniedHandlerImpl(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String email = request.getParameter("email");
        if (email != null) {
            User user = (User) userService.loadUserByUsername(email);
            if (user != null) {
                if (user.isAccountNonLocked()) {
                    userService.increaseFailedAttempts(user);
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorResponse responseData = new ErrorResponse(LocalDateTime.now(), HttpServletResponse.SC_FORBIDDEN, "Forbidden", "Access Denied!", request.getRequestURI());

        response.getOutputStream().println(responseData.toString());
        eventService.registerEvent(Event.ACTION.LOGIN_FAILED, email, request.getRequestURI());
    }
}