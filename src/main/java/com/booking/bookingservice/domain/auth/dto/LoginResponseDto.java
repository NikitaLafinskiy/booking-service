package com.booking.bookingservice.domain.auth.dto;

public record LoginResponseDto(String accessToken, String refreshToken) {
}
