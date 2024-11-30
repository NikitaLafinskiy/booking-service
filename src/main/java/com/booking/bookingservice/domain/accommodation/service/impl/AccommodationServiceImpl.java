package com.booking.bookingservice.domain.accommodation.service.impl;

import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.MutateAccommodationRequestDto;
import com.booking.bookingservice.domain.accommodation.mapper.AccommodationMapper;
import com.booking.bookingservice.domain.accommodation.model.Accommodation;
import com.booking.bookingservice.domain.accommodation.repository.AccommodationRepository;
import com.booking.bookingservice.domain.accommodation.repository.AmenityRepository;
import com.booking.bookingservice.domain.accommodation.service.AccommodationService;
import com.booking.bookingservice.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final AmenityRepository amenityRepository;

    @Override
    @Transactional
    public AccommodationDto createAccommodation(
            MutateAccommodationRequestDto mutateAccommodationRequestDto) {
        Accommodation accommodation = accommodationMapper.toAccommodation(
                mutateAccommodationRequestDto);
        accommodationRepository.save(accommodation);
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    public List<AccommodationDto> getAccommodations(Pageable pageable) {
        return accommodationRepository.findAllWithAmenities(pageable)
                .stream()
                .map(accommodationMapper::toDto)
                .toList();
    }

    @Override
    public AccommodationDto getAccommodation(Long id) {
        Accommodation accommodation = accommodationRepository.findByIdWithAmenities(id)
                .orElseThrow(() ->
                new EntityNotFoundException("Accommodation with an id of "
                        + id
                        + " not found"));
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    @Transactional
    public AccommodationDto updateAccommodation(
            Long id,
            MutateAccommodationRequestDto mutateAccommodationRequestDto) {
        Accommodation accommodation = accommodationRepository.findByIdWithAmenities(id)
                .orElseThrow(() ->
                new EntityNotFoundException("Accommodation with an id of "
                        + id
                        + " not found"));
        amenityRepository.deleteAmenitiesByAccommodation_Id(accommodation.getId());
        accommodationMapper.updateAccommodation(mutateAccommodationRequestDto, accommodation);
        accommodationRepository.save(accommodation);
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    @Transactional
    public void deleteAccommodation(Long id) {
        Accommodation accommodation = accommodationRepository.findByIdWithAmenities(id)
                .orElseThrow(() ->
                new EntityNotFoundException("Accommodation with an id of "
                        + id
                        + " not found"));
        amenityRepository.deleteAmenitiesByAccommodation_Id(accommodation.getId());
        accommodationRepository.delete(accommodation);
    }
}
