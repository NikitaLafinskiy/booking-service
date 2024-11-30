package com.booking.bookingservice.domain.accommodation.repository;

import com.booking.bookingservice.domain.accommodation.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    void deleteAmenitiesByAccommodation_Id(Long accommodationId);
}
