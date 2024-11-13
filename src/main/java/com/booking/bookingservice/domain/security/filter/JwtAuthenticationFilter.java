package com.booking.bookingservice.domain.security.filter;

import com.booking.bookingservice.domain.token.service.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    private final TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractAccessToken(request);

        if (accessToken != null && tokenUtil.validateAccessToken(accessToken)) {
            // todo: handle the authentication
        } else {
            String refreshToken = extractRefreshToken(request);
            if (refreshToken != null && tokenUtil.validateRefreshToken(refreshToken)) {
                // todo: refresh the tokens and respond with a 401 error
            } else {
                // todo: respond with an error and set the status code to be used by the exception handler filter
            }
        }
    }

    private String extractAccessToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX))
                ? token.substring(TOKEN_PREFIX.length()) : null;
    }

    private String extractRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(REFRESH_TOKEN_HEADER);
        return (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX))
                ? token.substring(TOKEN_PREFIX.length()) : null;
    }
}
