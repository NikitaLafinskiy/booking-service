package com.booking.bookingservice.domain.payment.mapper;

import com.booking.bookingservice.config.MapperConfig;
import com.booking.bookingservice.domain.payment.dto.PaymentDto;
import com.booking.bookingservice.domain.payment.model.Payment;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "stripeSessionId", source = "stripeSession.id")
    PaymentDto toDto(Payment payment);

    default Map<String, Object> dtoToMap(PaymentDto paymentDto) {
        return Map.of(
                "id", paymentDto.getId(),
                "bookingId", paymentDto.getBookingId(),
                "stripeSessionId", paymentDto.getStripeSessionId(),
                "paymentStatus", paymentDto.getPaymentStatus(),
                "amountToPay", paymentDto.getAmountToPay(),
                "userId", paymentDto.getUserId()
        );
    }
}
