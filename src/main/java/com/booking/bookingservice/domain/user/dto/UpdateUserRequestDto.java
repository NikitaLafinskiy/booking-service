package com.booking.bookingservice.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequestDto {
    @Email
    @NotBlank
    private String email;

    @Size(min = 3, max = 90)
    @NotBlank
    private String firstName;

    @Size(min = 3, max = 90)
    @NotBlank
    private String lastName;
}
