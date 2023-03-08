package account.security;

import account.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static account.model.Privilege.READ_PAYMENT;
import static account.model.Privilege.UPLOAD_PAYMENT;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic() // Handle auth error
                .and().csrf().disable().headers().frameOptions().disable()// for Postman, the H2 console.
                .and().authorizeRequests()// manage access
                .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()//
                .mvcMatchers(HttpMethod.POST, "/api/auth/changepass").authenticated() // manage access
                .mvcMatchers(HttpMethod.GET, "/api/empl/payment").hasAuthority(READ_PAYMENT)//
                .mvcMatchers(HttpMethod.POST, "/api/acct/payments").hasAuthority(UPLOAD_PAYMENT)//
                .mvcMatchers(HttpMethod.PUT, "/api/acct/payments").hasAuthority(UPLOAD_PAYMENT)//
                .mvcMatchers("/api/admin/**").hasAuthority(Role.ADMIN)//
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler())
        ;
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }
}