package account.security;

import account.service.EventService;
import account.service.FailedAttemptService;
import account.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
@AllArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final FailedAttemptService failedAttemptService;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            String email = new String(Base64.getDecoder().decode(authorization.split("\\s+")[1])).split(":")[0];
            String path = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            failedAttemptService.increaseFailedAttempt(email, path);
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}