package com.booking.bookingservice.domain.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentRequestDto {
    @NotNull
    private Long bookingId;
}
