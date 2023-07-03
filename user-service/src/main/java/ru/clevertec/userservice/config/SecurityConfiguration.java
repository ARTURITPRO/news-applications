package ru.clevertec.userservice.config;

import lombok.RequiredArgsConstructor;
import ru.clevertec.userservice.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * <p>Configuration class for security.</p>
 *
 * @author Artur Malashkov
 * @see JwtFilter
 * @since 17
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;

    /**
     * This bean will be processed in a chain Http Security
     *
     * @param http HttpSecurity
     * @return security filter chain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(
                        authorizedUrl -> authorizedUrl
                                .requestMatchers("/api/auth/login", "/api/auth/token", "/api/auth/register",
                                        "/api/auth/user/**").permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                ).build();
    }

}
