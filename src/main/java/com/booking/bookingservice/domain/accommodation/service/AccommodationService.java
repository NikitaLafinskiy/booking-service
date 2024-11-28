package com.booking.bookingservice.domain.accommodation.service;

import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.CreateAccommodationRequestDto;

public interface AccommodationService {
    AccommodationDto createAccommodation(
            CreateAccommodationRequestDto createAccommodationRequestDto);
}
