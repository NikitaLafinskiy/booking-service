package com.booking.bookingservice.domain.payment.repository;

import com.booking.bookingservice.domain.payment.model.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @EntityGraph(attributePaths = {"user", "booking", "session"})
    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId")
    Optional<Payment> findByUserIdWithDetails(Long userId);
}
