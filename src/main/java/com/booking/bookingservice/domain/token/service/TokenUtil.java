package com.booking.bookingservice.domain.token.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

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
    public static String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, accessSecretKey);
    }

    public static String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshSecretKey);
    }

    private static String generateToken(Authentication authentication, SecretKey secretKey) {
        return Jwts.builder()
                .issuedAt(new Date())
                // todo: add more claims
                .signWith(secretKey)
                .compact();
    }

    // isValid token
    public static boolean isValidAccessToken(String token) {
        return isValidToken(token, accessSecretKey);
    }

    public static boolean isValidRefreshToken(String token) {
        return isValidToken(token, refreshSecretKey);
    }

    private static boolean isValidToken(String token, SecretKey secretKey) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
        return claimsJws.getPayload().getIssuedAt().before(new Date());
    }

    // get attribute
    // todo: add more attribute retrieval methods

    private static <T> T getAttributeFromAccessToken(String token, Function<Claims, T> claimsResolver) {
        return getAttribute(token, accessSecretKey, claimsResolver);
    }

    private static <T> T getAttributeFromRefreshToken(String token, Function<Claims, T> claimsResolver) {
        return getAttribute(token, refreshSecretKey, claimsResolver);
    }

    private static <T> T getAttribute(String token, SecretKey secretKey, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }
}
