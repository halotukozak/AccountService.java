package account.security;

import account.db.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static account.db.model.Privilege.READ_PAYMENT;
import static account.db.model.Privilege.UPLOAD_PAYMENT;

@Configuration
@EnableWebSecurity(debug = true)
@AllArgsConstructor
public class SecurityConfig {
    private final AccessDeniedHandlerImpl accessDeniedHandler;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)// Handle auth error
                .and().csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and().authorizeRequests() // manage access
                .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()//
                .mvcMatchers(HttpMethod.POST, "/api/auth/changepass").authenticated() // manage access
                .mvcMatchers(HttpMethod.GET, "/api/empl/payment").hasAuthority(READ_PAYMENT)//
                .mvcMatchers(HttpMethod.POST, "/api/acct/payments").hasAuthority(UPLOAD_PAYMENT)//
                .mvcMatchers(HttpMethod.PUT, "/api/acct/payments").hasAuthority(UPLOAD_PAYMENT)//
                .mvcMatchers("/api/admin/**").hasAuthority(Role.ADMIN)//
                .mvcMatchers("/api/admin/user/**").hasAuthority(Role.ADMIN)//
                .mvcMatchers("api/security/events").hasAuthority(Role.AUDITOR)//
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)

        ;
        return http.build();
    }

}
