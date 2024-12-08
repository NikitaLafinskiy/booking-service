package com.booking.bookingservice.utils;

import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.CreateBookingRequestDto;
import com.booking.bookingservice.domain.booking.model.Booking;
import java.time.LocalDate;

public class BookingTestUtils {
    public static CreateBookingRequestDto setUpExistantCreateBookingRequestDto() {
        return new CreateBookingRequestDto()
                .setUserId(2L)
                .setAccommodationId(1L)
                .setCheckInDate(LocalDate.parse("2025-12-10"))
                .setCheckOutDate(LocalDate.parse("2025-12-15"));
    }

    public static BookingDto setUpExistantBookingDto() {
        return new BookingDto()
                .setId(1L)
                .setUserId(2L)
                .setAccommodationId(1L)
                .setCheckInDate(LocalDate.parse("2025-12-10"))
                .setCheckOutDate(LocalDate.parse("2025-12-15"))
                .setStatus(Booking.BookingStatus.CONFIRMED);
    }

    public static CreateBookingRequestDto setUpNonExistantCreateBookingRequestDto() {
        return new CreateBookingRequestDto()
                .setUserId(2L)
                .setAccommodationId(1L)
                .setCheckInDate(LocalDate.parse("2026-12-10"))
                .setCheckOutDate(LocalDate.parse("2026-12-15"));
    }

    public static BookingDto setUpNonExistantBookingDto() {
        return new BookingDto()
                .setId(1L)
                .setUserId(2L)
                .setAccommodationId(1L)
                .setCheckInDate(LocalDate.parse("2026-12-10"))
                .setCheckOutDate(LocalDate.parse("2026-12-15"))
                .setStatus(Booking.BookingStatus.CONFIRMED);
    }
}
