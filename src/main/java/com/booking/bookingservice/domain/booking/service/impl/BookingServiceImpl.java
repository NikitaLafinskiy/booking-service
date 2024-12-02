package com.booking.bookingservice.domain.booking.service.impl;

import com.booking.bookingservice.domain.accommodation.model.Accommodation;
import com.booking.bookingservice.domain.accommodation.repository.AccommodationRepository;
import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.CreateBookingRequestDto;
import com.booking.bookingservice.domain.booking.dto.UpdateBookingRequestDto;
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
    public BookingDto createBooking(CreateBookingRequestDto createBookingRequestDto,
                                    UserDto userDto) {
        Accommodation accommodation = accommodationRepository.findByIdWithBookings(
                createBookingRequestDto.getAccommodationId())
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with an id of "
                        + createBookingRequestDto.getAccommodationId()
                        + " not found"));
        Booking booking = bookingMapper.toEntity(createBookingRequestDto);
        accommodation.getBookings().forEach(presentBooking -> {
            if (isBookingOverlapping(
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    presentBooking)) {
                throw new InvalidInputException("Booking overlaps with another booking");
            }
        });
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
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
    public BookingDto updateBooking(Long id,
                                    UpdateBookingRequestDto updateBookingRequestDto,
                                    UserDto userDto) {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with an id of "
                        + id
                        + " not found"));

        if (!(userDto.getId().equals(booking.getUser().getId()))) {
            throw new InvalidInputException("You can not update another customer's booking");
        }

        bookingMapper.updateBooking(booking, updateBookingRequestDto);
        Accommodation accommodation = accommodationRepository.findByIdWithBookings(
                booking.getAccommodation()
                        .getId())
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with an id of "
                        + booking.getAccommodation()
                        .getId()
                        + " not found"));
        accommodation.getBookings().forEach(presentBooking -> {
            if (isBookingOverlapping(
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    presentBooking)
                    && !booking.getId()
                            .equals(presentBooking.getId())) {
                throw new InvalidInputException("Booking overlaps with another booking");
            }
        });
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with an id of "
                        + id
                        + " not found"));
        bookingRepository.delete(booking);
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
