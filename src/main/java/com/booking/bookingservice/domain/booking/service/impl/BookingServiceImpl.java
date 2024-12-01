package com.booking.bookingservice.domain.booking.service.impl;

import com.booking.bookingservice.domain.accommodation.model.Accommodation;
import com.booking.bookingservice.domain.accommodation.repository.AccommodationRepository;
import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.MutateBookingRequestDto;
import com.booking.bookingservice.domain.booking.mapper.BookingMapper;
import com.booking.bookingservice.domain.booking.model.Booking;
import com.booking.bookingservice.domain.booking.repository.BookingRepository;
import com.booking.bookingservice.domain.booking.service.BookingService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.domain.user.model.User;
import com.booking.bookingservice.domain.user.repository.UserRepository;
import com.booking.bookingservice.exception.EntityNotFoundException;
import com.booking.bookingservice.exception.InvalidInputException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBooking(MutateBookingRequestDto mutateBookingRequestDto,
                                    UserDto userDto) {
        Accommodation accommodation = accommodationRepository.findByIdWithBookings(
                mutateBookingRequestDto.getAccommodationId())
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with an id of "
                        + mutateBookingRequestDto.getAccommodationId()
                        + " not found"));
        accommodation.getBookings().forEach(booking -> {
            if (isBookingOverlapping(
                    mutateBookingRequestDto.getCheckInDate(),
                    mutateBookingRequestDto.getCheckOutDate(),
                    booking)) {
                throw new InvalidInputException("Booking overlaps with another booking");
            }
        });
        Booking booking = bookingMapper.toEntity(mutateBookingRequestDto);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
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

    @Override
    public List<BookingDto> getUserBookings(Long userId, Booking.BookingStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with an id of "
                        + userId
                        + " not found"));
        return bookingRepository.findBookingsByUser_Id(user.getId())
                .stream()
                .filter(booking -> booking.getStatus()
                        .equals(status))
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public List<BookingDto> getMyBookings(UserDto userDto) {
        return bookingRepository.findBookingsByUser_Id(userDto.getId())
                .stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public BookingDto updateBooking(Long id, MutateBookingRequestDto mutateBookingRequestDto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with an id of "
                        + id
                        + " not found"));
        if (!Objects.equals(
                booking.getUser().getId(),
                mutateBookingRequestDto.getUserId())) {
            throw new InvalidInputException("The user owning a booking can not be changed");
        }

        if (!Objects.equals(
                booking.getAccommodation().getId(),
                mutateBookingRequestDto.getAccommodationId())) {
            throw new InvalidInputException("To change the accommodation of a booking, "
                    + "delete the booking and create a new one");
        }

        bookingMapper.updateBooking(booking, mutateBookingRequestDto);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }
}
