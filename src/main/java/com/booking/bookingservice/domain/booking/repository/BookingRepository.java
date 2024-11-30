package com.booking.bookingservice.domain.booking.repository;

import com.booking.bookingservice.domain.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    void deleteBookingsByAccommodation_Id(Long accommodationId);
}
