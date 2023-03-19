package account.security;

import account.db.model.User;
import account.service.EventService;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

import static account.db.model.Event.ACTION.LOGIN_FAILED;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            String email = new String(Base64.getDecoder().decode(authorization.split("\\s+")[1])).split(":")[0];
            String path = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            User user = (User) userService.loadUserByUsername(email);
            if (user == null)
                eventService.registerEvent(LOGIN_FAILED, email, path);
            else {
                if (user.isAccountNonLocked()) {
                    eventService.registerEvent(LOGIN_FAILED, user.getEmail(), path);
                    userService.increaseFailedAttempts(user);
                }
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}