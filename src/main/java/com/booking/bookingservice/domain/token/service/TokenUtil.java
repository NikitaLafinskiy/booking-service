package com.booking.bookingservice.domain.token.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenUtil {
    private static SecretKey accessSecretKey;
    private static SecretKey refreshSecretKey;

    public TokenUtil(@Value("${jwt.access-secret}") String accessSecret,
                     @Value("${jwt.refresh-secret}") String refreshSecret) {
        accessSecretKey = Keys.hmacShaKeyFor(accessSecret.getBytes());
        refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());
    }

    // generate a token
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, accessSecretKey);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshSecretKey);
    }

    private String generateToken(Authentication authentication,
                                        SecretKey secretKey) {
        return Jwts.builder()
                .issuedAt(new Date())
                // todo: add more claims
                .signWith(secretKey)
                .compact();
    }

    // isValid token
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecretKey);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecretKey);
    }

    private boolean validateToken(String token,
                                  SecretKey secretKey) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
        return claimsJws.getPayload().getIssuedAt().before(new Date());
    }

    // get attribute
    // todo: add more attribute retrieval methods

    private <T> T getAttributeFromAccessToken(String token,
                                                     Function<Claims, T> claimsResolver) {
        return getAttribute(token, accessSecretKey, claimsResolver);
    }

    private <T> T getAttributeFromRefreshToken(String token, Function<Claims,
            T> claimsResolver) {
        return getAttribute(token, refreshSecretKey, claimsResolver);
    }

    private <T> T getAttribute(String token,
                                      SecretKey secretKey,
                                      Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }
}
