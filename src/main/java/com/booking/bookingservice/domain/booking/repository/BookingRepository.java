package com.booking.bookingservice.domain.booking.repository;

import com.booking.bookingservice.domain.booking.model.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByUser_Id(Long userId);
}
