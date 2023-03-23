package account.security;

import account.db.model.Event;
import account.http.response.ErrorResponse;
import account.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final EventService eventService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String email = request.getUserPrincipal().getName();

        ErrorResponse responseData = new ErrorResponse(LocalDateTime.now(), HttpServletResponse.SC_FORBIDDEN, "Forbidden", "Access Denied!", request.getRequestURI());
        eventService.registerEvent(Event.ACTION.ACCESS_DENIED, email, request.getRequestURI());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream().println(responseData.toString());
    }
}
