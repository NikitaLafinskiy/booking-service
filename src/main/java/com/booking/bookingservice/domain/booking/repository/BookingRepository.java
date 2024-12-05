package com.booking.bookingservice.domain.booking.repository;

import com.booking.bookingservice.domain.booking.model.Booking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByUser_Id(Long userId);

    @EntityGraph(attributePaths = {"accommodation", "user"})
    @Query("SELECT b FROM Booking b WHERE b.id = :bookingId")
    Optional<Booking> findByIdWithDetails(Long bookingId);

    @EntityGraph(attributePaths = {"accommodation", "user"})
    @Query("SELECT b FROM Booking b WHERE b.status = :status")
    List<Booking> findBoookingsWithDetailsByStatus(Booking.BookingStatus status);
}
