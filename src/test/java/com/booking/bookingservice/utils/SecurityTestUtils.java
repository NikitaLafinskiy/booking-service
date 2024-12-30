package com.booking.bookingservice.utils;

import com.booking.bookingservice.domain.token.service.TokenService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityTestUtils {
    public static UserDto setUpAdminUserDto() {
        return new UserDto()
                .setId(1L)
                .setEmail("admin@booking.com")
                .setFirstName("Admin")
                .setLastName("User");
    }

    public static Collection<? extends GrantedAuthority> setUpAdminAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public static String setUpAccessToken(TokenService tokenService) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        setUpAdminUserDto(),
                        null,
                        setUpAdminAuthorities());
        return tokenService.generateToken(authentication, TokenService.TokenType.ACCESS);
    }

    public static String setUpRefreshToken(TokenService tokenService) {
        UserDto userDto = setUpAdminUserDto();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDto,
                        null,
                        setUpAdminAuthorities());
        String refreshToken = tokenService.generateToken(authentication,
                TokenService.TokenType.REFRESH);
        tokenService.saveRefreshToken(refreshToken, userDto.getEmail());
        return refreshToken;
    }
}
