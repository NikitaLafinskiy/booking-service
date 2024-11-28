package com.booking.bookingservice.domain.accommodation.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class AccommodationDto {
    private Long id;
    private String location;
    private String type;
    private String size;
    private List<String> amenities;
    private BigDecimal dailyRate;
    private Integer availability;
}
