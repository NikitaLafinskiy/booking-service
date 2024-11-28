package com.booking.bookingservice.domain.accommodation.service.impl;

import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.CreateAccommodationRequestDto;
import com.booking.bookingservice.domain.accommodation.mapper.AccommodationMapper;
import com.booking.bookingservice.domain.accommodation.model.Accommodation;
import com.booking.bookingservice.domain.accommodation.repository.AccommodationRepository;
import com.booking.bookingservice.domain.accommodation.service.AccommodationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;

    @Override
    @Transactional
    public AccommodationDto createAccommodation(
            CreateAccommodationRequestDto createAccommodationRequestDto) {
        Accommodation accommodation = accommodationMapper.toAccommodation(
                createAccommodationRequestDto);
        accommodationRepository.save(accommodation);
        return accommodationMapper.toDto(accommodation);
    }
}
