package com.booking.bookingservice.domain.accommodation.controller;

import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.MutateAccommodationRequestDto;
import com.booking.bookingservice.domain.accommodation.service.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Create an accommodation", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Accommodation created successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request body"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public AccommodationDto createAccommodation(
            @RequestBody @Valid MutateAccommodationRequestDto mutateAccommodationRequestDto) {
        return accommodationService.createAccommodation(mutateAccommodationRequestDto);
    }

    @Operation(summary = "Get all accommodations", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Accommodations retrieved successfully"),
    })
    @GetMapping
    public List<AccommodationDto> getAccommodations(Pageable pageable) {
        return accommodationService.getAccommodations(pageable);
    }

    @Operation(summary = "Get an accommodation", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Accommodation retrieved successfully"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
            @ApiResponse(responseCode = "404",
                    description = "Accommodation not found"),
    })
    @GetMapping("/{id}")
    public AccommodationDto getAccommodation(@PathVariable Long id) {
        return accommodationService.getAccommodation(id);
    }

    @Operation(summary = "Update an accommodation", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Accommodation updated successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request body"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
            @ApiResponse(responseCode = "404",
                    description = "Accommodation not found"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AccommodationDto updateAccommodation(
            @RequestBody @Valid MutateAccommodationRequestDto mutateAccommodationRequestDto,
            @PathVariable Long id) {
        return accommodationService.updateAccommodation(id, mutateAccommodationRequestDto);
    }

    @Operation(summary = "Delete an accommodation", responses = {
            @ApiResponse(responseCode = "204",
                    description = "Accommodation deleted successfully"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
            @ApiResponse(responseCode = "404",
                    description = "Accommodation not found"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccommodation(@PathVariable Long id) {
        accommodationService.deleteAccommodation(id);
    }
}
