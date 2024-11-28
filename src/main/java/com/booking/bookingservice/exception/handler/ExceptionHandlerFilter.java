package com.booking.bookingservice.exception.handler;

import com.booking.bookingservice.exception.JwtAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            Map<String, Object> errors = composeErrorResponse(e, e.getStatusCode().value());

            String jsonError = objectMapper.writeValueAsString(errors);
            response.setStatus(e.getStatusCode().value());
            response.setContentType("application/json");
            response.getWriter().write(jsonError);
        } catch (Exception e) {
            Map<String, Object> errors = composeErrorResponse(e,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            String jsonError = objectMapper.writeValueAsString(errors);
            response.setContentType("application/json");
            response.getWriter().write(jsonError);
        }
    }

    private Map<String, Object> composeErrorResponse(Exception e, int status) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", status);
        errors.put("timestamp", System.currentTimeMillis());
        errors.put("message", e.getLocalizedMessage());
        return errors;
    }
}
