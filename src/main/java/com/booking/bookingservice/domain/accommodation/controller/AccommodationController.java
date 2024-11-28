package com.booking.bookingservice.domain.accommodation.controller;

import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.CreateAccommodationRequestDto;
import com.booking.bookingservice.domain.accommodation.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public AccommodationDto createAccommodation(
            @RequestBody @Valid CreateAccommodationRequestDto createAccommodationRequestDto) {
        return accommodationService.createAccommodation(createAccommodationRequestDto);
    }
}
