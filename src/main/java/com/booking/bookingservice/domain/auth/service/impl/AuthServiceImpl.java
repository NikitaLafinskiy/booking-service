package com.booking.bookingservice.domain.auth.service.impl;

import com.booking.bookingservice.domain.auth.dto.LoginResponseDto;
import com.booking.bookingservice.domain.auth.dto.LoginUserRequestDto;
import com.booking.bookingservice.domain.auth.dto.RefreshTokenRequestDto;
import com.booking.bookingservice.domain.auth.dto.RegisterUserRequestDto;
import com.booking.bookingservice.domain.auth.mapper.AuthMapper;
import com.booking.bookingservice.domain.auth.service.AuthService;
import com.booking.bookingservice.domain.token.service.TokenService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.domain.user.mapper.UserMapper;
import com.booking.bookingservice.domain.user.model.Role;
import com.booking.bookingservice.domain.user.model.User;
import com.booking.bookingservice.domain.user.repository.RoleRepository;
import com.booking.bookingservice.domain.user.repository.UserRepository;
import com.booking.bookingservice.exception.EntityNotFoundException;
import com.booking.bookingservice.exception.UnauthorizedException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    public static final Role.RoleType DEFAULT_USER_ROLE = Role.RoleType.CUSTOMER;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public LoginResponseDto login(LoginUserRequestDto loginUserRequestDto) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUserRequestDto.getEmail(),
                loginUserRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        Authentication authenticationWithAuthorities = new UsernamePasswordAuthenticationToken(
                userMapper.toDto((User) authentication.getPrincipal()),
                null,
                ((User) authentication.getPrincipal()).getAuthorities());
        String accessToken = tokenService.generateToken(authenticationWithAuthorities,
                TokenService.TokenType.ACCESS);
        String refreshToken = tokenService.generateToken(authenticationWithAuthorities,
                TokenService.TokenType.REFRESH);
        tokenService.saveRefreshToken(refreshToken,
                ((User) authentication.getPrincipal()).getEmail());
        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public UserDto register(RegisterUserRequestDto registerUserRequestDto) {
        userRepository.findByEmail(registerUserRequestDto.getEmail()).ifPresent(user -> {
            throw new EntityNotFoundException("User with email "
                    + user.getEmail()
                    + " already exists");
        });
        User user = authMapper.toUserFromRegisterUserRequestDto(registerUserRequestDto);
        user.setRoles(Set.of(roleRepository.findByRoleType(DEFAULT_USER_ROLE)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public LoginResponseDto refresh(RefreshTokenRequestDto refreshTokenRequestDto) {
        String refreshToken = refreshTokenRequestDto.getRefreshToken();

        boolean isValid;
        try {
            isValid = tokenService.validateToken(refreshToken, TokenService.TokenType.REFRESH);
        } catch (Exception e) {
            isValid = false;
        }
        if (!isValid) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        Authentication authentication = tokenService.getAuthentication(refreshToken,
                TokenService.TokenType.REFRESH);
        String newAccessToken = tokenService.generateToken(authentication,
                TokenService.TokenType.ACCESS);
        String newRefreshToken = tokenService.generateToken(authentication,
                TokenService.TokenType.REFRESH);

        tokenService.deleteRefreshToken(refreshToken);
        tokenService.saveRefreshToken(newRefreshToken,
                ((UserDto) authentication.getPrincipal()).getEmail());

        return new LoginResponseDto(newAccessToken, newRefreshToken);
    }
}
