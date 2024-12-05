package com.booking.bookingservice.domain.payment.model;

import com.booking.bookingservice.domain.booking.model.Booking;
import com.booking.bookingservice.domain.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @OneToOne(mappedBy = "payment", optional = false, cascade = CascadeType.ALL)
    private StripeSession stripeSession;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private BigDecimal amountToPay;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Payment(Booking booking,
                   BigDecimal amountToPay,
                   User user,
                   StripeSession stripeSession) {
        this.booking = booking;
        this.amountToPay = amountToPay;
        this.user = user;
        this.paymentStatus = PaymentStatus.PENDING;
        this.stripeSession = stripeSession;
        this.stripeSession.setPayment(this);
    }

    public enum PaymentStatus {
        PENDING,
        PAID,
        CANCELLED
    }
}
