package account.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint) // Handle auth error
                .and().csrf().disable().headers().frameOptions().disable()// for Postman, the H2 console
                .and().authorizeRequests() // manage access
                .mvcMatchers("/api/empl/payment", "/api/auth/changepass").authenticated() // manage access
                .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                // other matchers
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}