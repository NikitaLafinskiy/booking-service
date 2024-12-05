package com.booking.bookingservice.domain.payment.repository;

import com.booking.bookingservice.domain.payment.model.StripeSession;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StripeSessionRepository extends JpaRepository<StripeSession, Long> {
    @EntityGraph(attributePaths = {"payment"})
    @Query("SELECT s FROM StripeSession s WHERE s.sessionId = :sessionId")
    Optional<StripeSession> findSessionBySessionIdWithPayment(String sessionId);
}
