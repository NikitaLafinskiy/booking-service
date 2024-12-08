package com.booking.bookingservice.utils;

import com.booking.bookingservice.domain.payment.dto.CreatePaymentRequestDto;
import com.booking.bookingservice.domain.payment.dto.PaymentDto;
import com.booking.bookingservice.domain.payment.model.Payment;
import java.math.BigDecimal;

public class PaymentTestUtils {
    public static CreatePaymentRequestDto setUpCreatePaymentRequestDto() {
        return new CreatePaymentRequestDto()
                .setBookingId(1L);
    }

    public static PaymentDto setUpPaymentDto() {
        return new PaymentDto()
                .setId(1L)
                .setBookingId(1L)
                .setStripeSessionId(1L)
                .setPaymentStatus(Payment.PaymentStatus.PENDING)
                .setAmountToPay(BigDecimal.valueOf(600L))
                .setUserId(2L);
    }
}
