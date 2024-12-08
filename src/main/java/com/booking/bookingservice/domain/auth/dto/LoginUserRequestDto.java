package com.booking.bookingservice.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginUserRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;
}
