package com.booking.bookingservice.domain.booking.service.impl;

import com.booking.bookingservice.domain.accommodation.model.Accommodation;
import com.booking.bookingservice.domain.accommodation.repository.AccommodationRepository;
import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.MutateBookingRequestDto;
import com.booking.bookingservice.domain.booking.model.Booking;
import com.booking.bookingservice.domain.booking.repository.BookingRepository;
import com.booking.bookingservice.domain.booking.service.BookingService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.exception.EntityNotFoundException;
import com.booking.bookingservice.exception.InvalidInputException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;

    @Override
    public BookingDto createBooking(MutateBookingRequestDto mutateBookingRequestDto,
                                    UserDto userDto) {
        // todo: tie the booking to a user and an accommodation
        Accommodation accommodation = accommodationRepository.findByIdWithBookings(
                mutateBookingRequestDto.getAccommodationId())
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with an id of "
                        + mutateBookingRequestDto.getAccommodationId()
                        + " not found"));
        accommodation.getBookings().forEach(booking -> {
            if (isBookingOverlapping(
                    LocalDate.parse(mutateBookingRequestDto.getCheckInDate()),
                    LocalDate.parse(mutateBookingRequestDto.getCheckOutDate()),
                    booking)) {
                throw new InvalidInputException("Booking overlaps with another booking");
            }
        });

        return null;
    }

    private boolean isBookingOverlapping(
            LocalDate checkInDate, LocalDate checkOutDate, Booking booking) {
        return (checkInDate
                .isBefore(booking.getCheckOutDate())
            && checkInDate.isAfter(booking.getCheckInDate()))
                || (checkOutDate
                .isAfter(booking.getCheckInDate())
            && checkOutDate.isBefore(booking.getCheckOutDate()));
    }
}
