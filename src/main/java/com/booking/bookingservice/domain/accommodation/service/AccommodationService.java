package com.booking.bookingservice.domain.accommodation.service;

import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.MutateAccommodationRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationDto createAccommodation(
            MutateAccommodationRequestDto mutateAccommodationRequestDto);

    List<AccommodationDto> getAccommodations(Pageable pageable);

    AccommodationDto getAccommodation(Long id);

    AccommodationDto updateAccommodation(
            Long id,
            MutateAccommodationRequestDto mutateAccommodationRequestDto);

    void deleteAccommodation(Long id);
}
