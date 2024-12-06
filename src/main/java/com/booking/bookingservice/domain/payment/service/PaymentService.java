package com.booking.bookingservice.domain.payment.service;

import com.booking.bookingservice.domain.payment.dto.CreatePaymentRequestDto;
import com.booking.bookingservice.domain.payment.dto.PaymentDto;
import com.booking.bookingservice.domain.user.dto.UserDto;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface PaymentService {
    PaymentDto getPayment(Long paymentUserId, Authentication authentication);

    List<String> createPayment(CreatePaymentRequestDto createPaymentRequestDto, UserDto userDto);

    String paymentSuccess(String sessionId);

    String paymentCancel(String sessionId);
}
