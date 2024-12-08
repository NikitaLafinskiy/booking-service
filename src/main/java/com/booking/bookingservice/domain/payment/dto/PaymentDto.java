package com.booking.bookingservice.domain.payment.dto;

import com.booking.bookingservice.domain.payment.model.Payment;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentDto {
    private Long id;
    private Long bookingId;
    private Long stripeSessionId;
    private Payment.PaymentStatus paymentStatus;
    private BigDecimal amountToPay;
    private Long userId;
}
