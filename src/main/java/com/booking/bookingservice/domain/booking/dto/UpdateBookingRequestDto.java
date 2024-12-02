package com.booking.bookingservice.domain.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class UpdateBookingRequestDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private LocalDate checkInDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private LocalDate checkOutDate;
}
