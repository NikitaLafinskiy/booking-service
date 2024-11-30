package com.booking.bookingservice.domain.accommodation.controller;

import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.MutateAccommodationRequestDto;
import com.booking.bookingservice.domain.accommodation.service.AccommodationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public AccommodationDto createAccommodation(
            @RequestBody @Valid MutateAccommodationRequestDto mutateAccommodationRequestDto) {
        return accommodationService.createAccommodation(mutateAccommodationRequestDto);
    }

    @GetMapping
    public List<AccommodationDto> getAccommodations(Pageable pageable) {
        return accommodationService.getAccommodations(pageable);
    }

    @GetMapping("/{id}")
    public AccommodationDto getAccommodation(@PathVariable Long id) {
        return accommodationService.getAccommodation(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AccommodationDto updateAccommodation(
            @RequestBody @Valid MutateAccommodationRequestDto mutateAccommodationRequestDto,
            @PathVariable Long id) {
        return accommodationService.updateAccommodation(id, mutateAccommodationRequestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccommodation(@PathVariable Long id) {
        accommodationService.deleteAccommodation(id);
    }
}
