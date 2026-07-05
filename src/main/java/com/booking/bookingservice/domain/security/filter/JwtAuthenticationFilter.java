package com.booking.bookingservice.domain.security.filter;

import com.booking.bookingservice.config.SecurityConfig;
import com.booking.bookingservice.domain.token.service.TokenService;
import com.booking.bookingservice.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String PATH_PREFIX = "/api/";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final TokenService tokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher matcher = new AntPathMatcher();
        String requestPath = request.getRequestURI();

        return Arrays.stream(SecurityConfig.OPEN_ROUTES)
                .anyMatch((routeMatch) -> {
                    if (routeMatch.method() != null
                            && !(routeMatch.method().name().equals(request.getMethod()))) {
                        return false;
                    }
                    return matcher.match(PATH_PREFIX + routeMatch.path(), requestPath);
                });
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractAccessToken(request);
        if (accessToken == null) {
            handleAuthenticationFailure("Authentication token is missing");
        }

        try {
            if (tokenService.validateToken(accessToken, TokenService.TokenType.ACCESS)) {
                processValidAccessToken(accessToken);
                filterChain.doFilter(request, response);
            }
        } catch (Exception exception) {
            handleAuthenticationFailure("Access token is invalid or expired");
        }
    }

    private void processValidAccessToken(String accessToken) {
        Authentication authentication = tokenService.getAuthentication(accessToken,
                TokenService.TokenType.ACCESS);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleAuthenticationFailure(String message) {
        throw new JwtAuthenticationException(message, HttpStatus.UNAUTHORIZED);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX))
                ? token.substring(TOKEN_PREFIX.length()) : null;
    }
}
