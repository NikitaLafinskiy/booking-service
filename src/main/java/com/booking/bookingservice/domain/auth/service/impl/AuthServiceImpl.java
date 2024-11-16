package com.booking.bookingservice.domain.auth.service.impl;

import com.booking.bookingservice.domain.auth.dto.LoginResponseDto;
import com.booking.bookingservice.domain.auth.dto.LoginUserRequestDto;
import com.booking.bookingservice.domain.auth.dto.RegisterUserRequestDto;
import com.booking.bookingservice.domain.auth.mapper.AuthMapper;
import com.booking.bookingservice.domain.auth.service.AuthService;
import com.booking.bookingservice.domain.token.service.TokenService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.domain.user.model.User;
import com.booking.bookingservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final TokenService tokenService;

    @Override
    public LoginResponseDto login(LoginUserRequestDto loginUserRequestDto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUserRequestDto.getEmail(),
                loginUserRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String accessToken = tokenService.generateToken(authentication,
                TokenService.TokenType.ACCESS);
        String refreshToken = tokenService.generateToken(authentication,
                TokenService.TokenType.REFRESH);
        tokenService.saveRefreshToken(refreshToken, (UserDto) authentication.getPrincipal());
        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public void register(RegisterUserRequestDto registerUserRequestDto) {
        User user = authMapper.toUserFromRegisterUserRequestDto(registerUserRequestDto);
        userRepository.save(user);
    }
}
