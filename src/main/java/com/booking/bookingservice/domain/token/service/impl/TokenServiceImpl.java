package com.booking.bookingservice.domain.token.service.impl;

import com.booking.bookingservice.domain.token.model.RefreshToken;
import com.booking.bookingservice.domain.token.repository.RefreshTokenRepository;
import com.booking.bookingservice.domain.token.service.TokenService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.domain.user.model.User;
import com.booking.bookingservice.domain.user.repository.UserRepository;
import com.booking.bookingservice.exception.EntityNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    private static final String EMAIL_CLAIM = "email";
    private static final String FIRST_NAME_CLAIM = "firstName";
    private static final String LAST_NAME_CLAIM = "lastName";
    private static final String AUTHORITIES_CLAIM = "authorities";

    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public TokenServiceImpl(@Value("${jwt.access-secret}") String accessSecret,
                            @Value("${jwt.refresh-secret}") String refreshSecret,
                            RefreshTokenRepository refreshTokenRepository,
                            UserRepository userRepository) {
        accessSecretKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public void saveRefreshToken(String token, UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
                new EntityNotFoundException(
                        "User with an email of "
                                + userDto.getEmail()
                                + " not found"));
        refreshTokenRepository.save(new RefreshToken(token, user));
    }

    public String generateToken(Authentication authentication,
                                        TokenType tokenType) {
        UserDto principal = (UserDto) authentication.getPrincipal();
        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .subject(principal.getId().toString())
                .claim(EMAIL_CLAIM, principal.getEmail())
                .claim(FIRST_NAME_CLAIM, principal.getFirstName())
                .claim(LAST_NAME_CLAIM, principal.getLastName())
                .claim(AUTHORITIES_CLAIM, authorities)
                .signWith(tokenType == TokenType.ACCESS ? accessSecretKey : refreshSecretKey)
                .compact();
    }

    public boolean validateToken(String token,
                                 TokenType tokenType) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(tokenType == TokenType.ACCESS ? accessSecretKey : refreshSecretKey)
                .build()
                .parseSignedClaims(token);
        return claimsJws.getPayload().getExpiration().after(new Date());
    }

    public UserDto getUserFromToken(String token, TokenService.TokenType tokenType) {
        return getAttribute(token, tokenType, claims -> {
            UserDto user = new UserDto();
            user.setId(Long.parseLong(claims.getSubject()));
            user.setEmail(claims.get(EMAIL_CLAIM, String.class));
            user.setFirstName(claims.get(FIRST_NAME_CLAIM, String.class));
            user.setLastName(claims.get(LAST_NAME_CLAIM, String.class));
            return user;
        });
    }

    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(
            String token,
            TokenType tokenType) {
        return getAttribute(token, tokenType, claims -> {
            List<String> authorities = claims.get(AUTHORITIES_CLAIM, List.class);
            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        });
    }

    private <T> T getAttribute(String token,
                                      TokenService.TokenType tokenType,
                                      Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(tokenType == TokenType.ACCESS ? accessSecretKey : refreshSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
