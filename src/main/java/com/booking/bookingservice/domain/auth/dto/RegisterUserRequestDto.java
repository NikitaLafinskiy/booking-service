package com.booking.bookingservice.domain.auth.dto;

import com.booking.bookingservice.validator.fieldmatch.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@FieldMatch(
        firstField = "password",
        secondField = "confirmPassword",
        message = "The password fields must match")
@Accessors(chain = true)
public class RegisterUserRequestDto {
    @Email
    @NotBlank
    private String email;

    @Size(min = 3, max = 90)
    @NotBlank
    private String firstName;

    @Size(min = 3, max = 90)
    @NotBlank
    private String lastName;

    @Size(min = 8, max = 255)
    @NotBlank
    private String password;

    @Size(min = 8, max = 255)
    @NotBlank
    private String confirmPassword;
}
