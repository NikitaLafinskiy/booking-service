package com.booking.bookingservice.domain.booking.dto;

import com.booking.bookingservice.validator.booking.CheckInBeforeCheckOut;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@CheckInBeforeCheckOut(checkInDateField = "checkInDate", checkOutDateField = "checkOutDate")
public class MutateBookingRequestDto {
    @NotNull
    private Long accommodationId;

    @NotNull
    private Long userId;

    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private String checkInDate;

    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private String checkOutDate;
}
