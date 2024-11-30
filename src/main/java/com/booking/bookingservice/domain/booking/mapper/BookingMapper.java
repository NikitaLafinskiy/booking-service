package com.booking.bookingservice.domain.booking.mapper;

import com.booking.bookingservice.config.MapperConfig;
import com.booking.bookingservice.domain.booking.dto.MutateBookingRequestDto;
import com.booking.bookingservice.domain.booking.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BookingMapper {
    @Mapping()
    Booking toEntity(MutateBookingRequestDto mutateBookingRequestDto);
}
