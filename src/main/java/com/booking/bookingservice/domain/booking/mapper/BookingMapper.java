package com.booking.bookingservice.domain.booking.mapper;

import com.booking.bookingservice.config.MapperConfig;
import com.booking.bookingservice.domain.accommodation.mapper.AccommodationMapper;
import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.CreateBookingRequestDto;
import com.booking.bookingservice.domain.booking.dto.UpdateBookingRequestDto;
import com.booking.bookingservice.domain.booking.model.Booking;
import com.booking.bookingservice.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {AccommodationMapper.class, UserMapper.class})
public interface BookingMapper {
    @Mapping(target = "accommodation",
            source = "accommodationId",
            qualifiedByName = "accommodationById")
    @Mapping(target = "user",
            source = "userId",
            qualifiedByName = "userById")
    Booking toEntity(CreateBookingRequestDto createBookingRequestDto);

    @Mapping(target = "accommodationId",
            source = "accommodation.id")
    @Mapping(target = "userId",
            source = "user.id")
    BookingDto toDto(Booking booking);

    void updateBooking(@MappingTarget Booking booking,
                       UpdateBookingRequestDto updateBookingRequestDto);
}
