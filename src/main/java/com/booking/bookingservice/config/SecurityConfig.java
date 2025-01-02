package com.booking.bookingservice.config;

import com.booking.bookingservice.domain.security.filter.JwtAuthenticationFilter;
import com.booking.bookingservice.exception.handler.ExceptionHandlerFilter;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    public static final RouteMatch[] OPEN_ROUTES = new RouteMatch[]{
            new RouteMatch("/actuator/health/**", HttpMethod.GET),
            new RouteMatch("/health/**", HttpMethod.GET),
            new RouteMatch("/auth/**", null),
            new RouteMatch("/accommodations/**", HttpMethod.GET),
            new RouteMatch("/payments/success", HttpMethod.GET),
            new RouteMatch("/payments/cancel", HttpMethod.GET),
            new RouteMatch("/swagger-ui/**", null),
            new RouteMatch("/v3/api-docs/**", null),
    };

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            List<AuthenticationProvider> authenticationProviders) {
        return new ProviderManager(authenticationProviders);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        try {
            httpSecurity
                    .cors(cors -> cors.configurationSource((request) -> {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedMethods(List.of("GET",
                                "POST",
                                "PUT",
                                "DELETE"));
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        return corsConfiguration;
                    }))
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    ))
                    .addFilterBefore(jwtAuthenticationFilter,
                            UsernamePasswordAuthenticationFilter.class)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(Arrays.stream(OPEN_ROUTES)
                                    .map(RouteMatch::path)
                                    .toArray(String[]::new))
                            .permitAll()
                            .anyRequest()
                            .authenticated()
                    )
                    .addFilterBefore(exceptionHandlerFilter, LogoutFilter.class);
            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException("Failed while configuring the security filter chain");
        }
    }

    public record RouteMatch(String path, HttpMethod method) {
    }
}
