package com.booking.bookingservice.domain.accommodation.mapper;

import com.booking.bookingservice.config.MapperConfig;
import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.CreateAccommodationRequestDto;
import com.booking.bookingservice.domain.accommodation.model.Accommodation;
import com.booking.bookingservice.domain.accommodation.model.Amenity;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    @Mapping(target = "amenities", ignore = true)
    Accommodation toAccommodation(CreateAccommodationRequestDto createAccommodationRequestDto);

    @AfterMapping
    default void setAccommodationAmenities(
            CreateAccommodationRequestDto createAccommodationRequestDto,
            @MappingTarget Accommodation accommodation) {
        List<Amenity> amenities = createAccommodationRequestDto.getAmenities().stream()
                .map(amenity -> new Amenity(amenity, accommodation))
                .toList();
        accommodation.setAmenities(amenities);
    }

    @Mapping(target = "amenities", ignore = true)
    AccommodationDto toDto(Accommodation accommodation);

    @AfterMapping
    default void setDtoAmenities(Accommodation accommodation,
                                 @MappingTarget AccommodationDto accommodationDto) {
        List<String> amenities = accommodation.getAmenities()
                .stream()
                .map(Amenity::getName)
                .toList();
        accommodationDto.setAmenities(amenities);
    }
}
