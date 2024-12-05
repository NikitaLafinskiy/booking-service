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
import com.booking.bookingservice.domain.notification.service.TelegramService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.domain.user.model.User;
import com.booking.bookingservice.domain.user.repository.UserRepository;
import com.booking.bookingservice.exception.EntityNotFoundException;
import com.booking.bookingservice.exception.InvalidInputException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    public static final int AVAILABILITY_INCREMENTOR = 1;

    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final TelegramService telegramService;

    @Override
    @Transactional
    public BookingDto createBooking(CreateBookingRequestDto createBookingRequestDto,
                                    UserDto userDto) {
        Accommodation accommodation = accommodationRepository.findByIdWithBookings(
                createBookingRequestDto.getAccommodationId())
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with an id of "
                        + createBookingRequestDto.getAccommodationId()
                        + " not found"));

        if (accommodation.getAvailability() <= 0) {
            throw new InvalidInputException("There are no available places left "
                    + "for the current booking.");
        }

        Booking booking = bookingMapper.toEntity(createBookingRequestDto);
        accommodation.getBookings().forEach(presentBooking -> {
            if (isBookingOverlapping(
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    presentBooking)) {
                throw new InvalidInputException("Booking overlaps with another booking");
            }
        });

        accommodation.setAvailability(accommodation.getAvailability() - AVAILABILITY_INCREMENTOR);
        accommodationRepository.save(accommodation);

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        User user = userRepository.findById(userDto.getId()).orElse(null);
        sendBookingNotification(booking,
                user,
                accommodation,
                false);

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
        bookingRepository.save(booking);
        sendBookingNotification(booking,
                booking.getUser(),
                booking.getAccommodation(),
                true);
        return bookingMapper.toDto(booking);
    }

    @Override
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with an id of "
                        + id
                        + " not found"));
        if (booking.getStatus()
                .equals(Booking.BookingStatus.CANCELLED)) {
            throw new InvalidInputException("The booking is already canceled.");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        Accommodation accommodation = booking.getAccommodation();
        accommodation.setAvailability(accommodation.getAvailability() + AVAILABILITY_INCREMENTOR);
        accommodationRepository.save(accommodation);

        if (booking.getUser().getTelegramChatId() != null) {
            telegramService.sendMessage(
                    String.format("""
                            Your booking has been canceled successfully:
                            Accommodation location: %s,
                            """, accommodation.getLocation()),
                    booking.getUser().getTelegramChatId());
        }
    }

    @Scheduled(cron = "0 0 2 * * ?", zone = "UTC")
    @Transactional
    public void expireBookings() {
        List<Booking> bookings = bookingRepository.findBoookingsWithDetailsByStatus(
                Booking.BookingStatus.CONFIRMED);
        bookings.forEach((booking) -> {
            if (isBookingExpired(booking)) {
                booking.setStatus(Booking.BookingStatus.EXPIRED);
                Accommodation accommodation = booking.getAccommodation();
                accommodation.setAvailability(accommodation.getAvailability() + 1);
                bookingRepository.save(booking);
                accommodationRepository.save(accommodation);

                telegramService.sendMessage(String.format("""
                                Your booking reservation has expired:
                                Accommodation: %s
                                """, accommodation.getLocation()),
                        booking.getUser().getTelegramChatId()
                );
            }
        });
    }

    private boolean isBookingExpired(Booking booking) {
        return booking.getCheckOutDate()
                .isBefore(LocalDate.now());
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

    private void sendBookingNotification(Booking booking,
                                         User user,
                                         Accommodation accommodation,
                                         boolean isUpdate) {
        if (user != null && user.getTelegramChatId() != null) {
            telegramService.sendMessage(
                    String.format("""
                            Your booking has been %s successfully:
                            Accommodation location: %s,
                            Check-in date: %s,
                            Check-out date: %s,
                            """,
                            isUpdate ? "updated" : "created",
                            accommodation.getLocation(),
                            booking.getCheckInDate(),
                            booking.getCheckOutDate()),
                    user.getTelegramChatId());
        }
    }
}
