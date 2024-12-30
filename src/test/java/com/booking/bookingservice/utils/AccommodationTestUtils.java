package com.booking.bookingservice.utils;

import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.MutateAccommodationRequestDto;
import com.booking.bookingservice.domain.accommodation.model.Accommodation;
import java.math.BigDecimal;
import java.util.List;

public class AccommodationTestUtils {
    public static List<AccommodationDto> setUpAccommodationDtos() {
        AccommodationDto accommodationDto1 = new AccommodationDto()
                .setId(1L)
                .setLocation("123 Elm Street, Springfield")
                .setType("HOUSE")
                .setSize("Large")
                .setAmenities(List.of("Parking", "Pool", "WiFi"))
                .setDailyRate(new BigDecimal("120.00"))
                .setAvailability(10);

        AccommodationDto accommodationDto2 = new AccommodationDto()
                .setId(2L)
                .setLocation("456 Oak Avenue, Shelby ville")
                .setType("APARTMENT")
                .setSize("Medium")
                .setAmenities(List.of("Air Conditioning", "Elevator Access", "Gym"))
                .setDailyRate(new BigDecimal("80.00"))
                .setAvailability(15);

        return List.of(accommodationDto1, accommodationDto2);
    }

    public static AccommodationDto setUpAccommodationDto() {
        return new AccommodationDto().setLocation("New York")
                .setType(Accommodation.AccommodationType.HOUSE.name())
                .setSize("Large")
                .setAmenities(List.of("Gym", "Pool", "WiFi"))
                .setDailyRate(BigDecimal.valueOf(150))
                .setAvailability(2);
    }

    public static MutateAccommodationRequestDto setUpMutateAccommodationRequestDto() {
        return new MutateAccommodationRequestDto().setLocation("New York")
                .setType(Accommodation.AccommodationType.HOUSE.name())
                .setSize("Large")
                .setAmenities(List.of("WiFi", "Pool", "Gym"))
                .setDailyRate(BigDecimal.valueOf(150))
                .setAvailability(2);
    }
}
