package com.booking.bookingservice.domain.booking.dto;

import com.booking.bookingservice.domain.booking.model.Booking;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingDto {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long accommodationId;
    private Long userId;
    private Booking.BookingStatus status;
}
