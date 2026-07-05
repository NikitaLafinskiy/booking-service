package com.booking.bookingservice.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RefreshTokenRequestDto {
    @NotBlank
    private String refreshToken;
}
