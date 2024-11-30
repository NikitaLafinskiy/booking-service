package com.booking.bookingservice.domain.accommodation.dto;

import com.booking.bookingservice.domain.accommodation.model.Accommodation;
import com.booking.bookingservice.validator.amenity.ValidAmenityName;
import com.booking.bookingservice.validator.enummatch.EnumMatch;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class MutateAccommodationRequestDto {
    @NotBlank
    @Size(min = 3, max = 255)
    private String location;

    @NotBlank
    @EnumMatch(enumClass = Accommodation.AccommodationType.class)
    private String type;

    @NotBlank
    @Size(min = 3, max = 255)
    private String size;

    @ValidAmenityName
    private List<String> amenities;

    @Min(10)
    @Max(999999)
    private BigDecimal dailyRate;

    @Min(0)
    @Max(3)
    private Integer availability;
}
