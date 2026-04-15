package nl.miwnn.cohort._9.OHI.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


/**
 * @author Sara Omlor
 * Configuration file for security
 */
@Configuration
@EnableWebSecurity
public class OHISecConfig {
    private static final Logger log = LoggerFactory.getLogger(OHISecConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests( auth ->auth
                        .requestMatchers(
                                "/",
                                "/profiles",
                                "/userlogin",
                                "/webjars/**",
                                "/static/**",
                                "/css/**")
                        .permitAll().anyRequest().authenticated())
                // custom form voor login
                .formLogin((form) -> form
                        .loginPage("/userlogin")
                        .loginProcessingUrl("/userlogin")
                        .defaultSuccessUrl("/profiles", true)
                        .permitAll());
        log.info("Toestemming gegeven");
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        var student = User.builder()
//                .username("student")
//                .password(passwordEncoder.encode("student"))
//                .roles("STUDENT")
//                .build();
//        log.info("student aangemaakt");
//        var docent = User.builder()
//                .username("docent")
//                .password(passwordEncoder.encode("docent"))
//                .roles("TEACHER")
//                .build();
//        log.info("docent aangemaakt");
//        return new InMemoryUserDetailsManager(student, docent);
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
