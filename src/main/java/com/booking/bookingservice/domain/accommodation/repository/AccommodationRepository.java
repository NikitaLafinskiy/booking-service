package com.booking.bookingservice.domain.accommodation.repository;

import com.booking.bookingservice.domain.accommodation.model.Accommodation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @EntityGraph(attributePaths = "amenities")
    @Query("SELECT a FROM Accommodation a")
    List<Accommodation> findAllWithAmenities(Pageable pageable);

    @EntityGraph(attributePaths = "amenities")
    @Query("SELECT a FROM Accommodation  a WHERE a.id = :id")
    Optional<Accommodation> findByIdWithAmenities(Long id);

    @EntityGraph(attributePaths = "bookings")
    @Query("SELECT a FROM Accommodation a WHERE a.id = :id")
    Optional<Accommodation> findByIdWithBookings(Long id);

    @EntityGraph(attributePaths = {"amenities", "bookings"})
    @Query("SELECT a FROM Accommodation a WHERE a.id = :id")
    Optional<Accommodation> findByIdWithAmenitiesAndBookings(Long id);
}
