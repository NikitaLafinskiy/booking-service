package com.booking.bookingservice.domain.accommodation.mapper;

import com.booking.bookingservice.config.MapperConfig;
import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.MutateAccommodationRequestDto;
import com.booking.bookingservice.domain.accommodation.model.Accommodation;
import com.booking.bookingservice.domain.accommodation.model.Amenity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    @Mapping(target = "amenities", ignore = true)
    Accommodation toEntity(MutateAccommodationRequestDto mutateAccommodationRequestDto);

    @AfterMapping
    default void setAccommodationAmenities(
            MutateAccommodationRequestDto mutateAccommodationRequestDto,
            @MappingTarget Accommodation accommodation) {
        Set<Amenity> amenities = mutateAccommodationRequestDto.getAmenities().stream()
                .map(amenity -> new Amenity(amenity, accommodation))
                .collect(Collectors.toSet());
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

    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateAccommodation(MutateAccommodationRequestDto mutateAccommodationRequestDto,
                             @MappingTarget Accommodation accommodation);

    @AfterMapping
    default void setUpdatedAccommodationAmenities(
            MutateAccommodationRequestDto mutateAccommodationRequestDto,
            @MappingTarget Accommodation accommodation) {
        accommodation.setAmenities(mutateAccommodationRequestDto
                .getAmenities()
                .stream()
                .map(amenity -> new Amenity(amenity, accommodation))
                .collect(Collectors.toSet()));
    }

    @Named("accommodationById")
    default Accommodation accommodationById(Long id) {
        return Optional.ofNullable(id)
                .map(Accommodation::new)
                .orElse(null);
    }
}
