package com.booking.bookingservice.domain.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePaymentRequestDto {
    @NotNull
    private Long bookingId;
}
