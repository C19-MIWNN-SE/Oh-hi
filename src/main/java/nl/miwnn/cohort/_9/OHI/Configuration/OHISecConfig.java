package nl.miwnn.cohort._9.OHI.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Sara Omlor
 * Configuration file for security
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class OHISecConfig {
    private static final Logger log = LoggerFactory.getLogger(OHISecConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/userlogin", "/webjars/**", "/css/**", "/js/**", "/static-images/**").permitAll()
                        .requestMatchers("/person/add", "person/add/**", "/person/remove", "/person/save", "/person/overview", "/account/**").hasRole("DOCENT")
                        .requestMatchers("/person/**", "/cohort/**", "/login-redirect").hasAnyRole("DOCENT", "STUDENT")
                )
                // custom form voor login
                .formLogin((form) -> form
                        .loginPage("/userlogin")
                        .loginProcessingUrl("/userlogin")
                        .defaultSuccessUrl("/login-redirect", true)
                        .permitAll())
                .logout(logout -> logout
                    .logoutSuccessUrl("/")
                    .permitAll());
        log.info("Toestemming gegeven");
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
