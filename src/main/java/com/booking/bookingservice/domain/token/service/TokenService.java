package com.booking.bookingservice.domain.token.service;

import com.booking.bookingservice.domain.user.dto.UserDto;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public interface TokenService {
    void saveRefreshToken(String token, String user);

    String generateToken(Authentication authentication, TokenService.TokenType tokenType);

    boolean validateToken(String token, TokenService.TokenType tokenType);

    UserDto getUserDtoFromToken(String token, TokenService.TokenType tokenType);

    Collection<? extends GrantedAuthority> getAuthoritiesFromToken(
            String token,
            TokenService.TokenType tokenType);

    enum TokenType {
        ACCESS,
        REFRESH
    }
}
