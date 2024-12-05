package com.booking.bookingservice.domain.payment.service.impl;

import com.booking.bookingservice.domain.booking.model.Booking;
import com.booking.bookingservice.domain.booking.repository.BookingRepository;
import com.booking.bookingservice.domain.payment.dto.CreatePaymentRequestDto;
import com.booking.bookingservice.domain.payment.dto.PaymentDto;
import com.booking.bookingservice.domain.payment.mapper.PaymentMapper;
import com.booking.bookingservice.domain.payment.model.Payment;
import com.booking.bookingservice.domain.payment.model.StripeSession;
import com.booking.bookingservice.domain.payment.repository.PaymentRepository;
import com.booking.bookingservice.domain.payment.repository.StripeSessionRepository;
import com.booking.bookingservice.domain.payment.service.PaymentService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.exception.EntityNotFoundException;
import com.booking.bookingservice.exception.PaymentException;
import com.booking.bookingservice.exception.UnauthorizedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public static final Long CENT_MULTIPLIER = 100L;
    public static final String SUCCESS_PAYMENT_STATUS = "paid";
    public static final String SUCCESS_ROUTE = "/payments/success";
    public static final String CANCEL_ROUTE = "/payments/cancel";

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final BookingRepository bookingRepository;
    private final ObjectMapper objectMapper;
    private final StripeSessionRepository stripeSessionRepository;

    @Value("${app.server-domain}")
    private String serverDomain;

    @Value("${stripe.default-currency}")
    private String defaultCurrency;

    @Value("${stripe.secret-key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public PaymentDto getPayment(Long paymentUserId, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().contains("ADMIN"));
        if (!isAdmin
                && !((UserDto) authentication.getPrincipal())
                .getId()
                .equals(paymentUserId)) {
            throw new UnauthorizedException("You are not allowed to access this payment");
        }

        Payment payment = paymentRepository.findByUserIdWithDetails(paymentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Payment for a user with an id of "
                        + paymentUserId
                        + " not found"));
        return paymentMapper.toDto(payment);
    }

    @Override
    @Transactional
    public List<String> createPayment(CreatePaymentRequestDto createPaymentRequestDto,
                                      UserDto userDto) {
        try {
            Booking booking = bookingRepository.findByIdWithDetails(
                            createPaymentRequestDto.getBookingId())
                    .orElseThrow(() -> new EntityNotFoundException("Booking with an id of "
                            + createPaymentRequestDto.getBookingId()
                            + " not found"));
            BigDecimal dailyRate = booking.getAccommodation().getDailyRate();
            long rentalDays = booking.getCheckInDate()
                    .until(booking.getCheckOutDate(), ChronoUnit.DAYS);

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(defaultCurrency)
                            .setUnitAmount(dailyRate.multiply(
                                            BigDecimal.valueOf(CENT_MULTIPLIER))
                                    .longValue())
                            .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName("Accommodation Rental - "
                                                    + booking.getAccommodation().getId())
                                            .build())
                            .build();

            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl(serverDomain
                                    + SUCCESS_ROUTE
                                    + "?sessionId={CHECKOUT_SESSION_ID}")
                            .setCancelUrl(serverDomain
                                    + CANCEL_ROUTE
                                    + "?sessionId={CHECKOUT_SESSION_ID}")
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(rentalDays)
                                            .setPriceData(priceData)
                                            .build())
                            .build();
            Session session = Session.create(params);

            StripeSession stripeSession = new StripeSession(session.getUrl(), session.getId());
            Payment payment = new Payment(booking,
                    dailyRate.multiply(BigDecimal.valueOf(rentalDays)),
                    booking.getUser(),
                    stripeSession);
            paymentRepository.save(payment);
            PaymentDto paymentDto = paymentMapper.toDto(payment);
            Map<String, Object> paymentMap = paymentMapper.dtoToMap(paymentDto);

            return List.of(objectMapper.writeValueAsString(paymentMap), session.getUrl());
        } catch (StripeException | JsonProcessingException e) {
            throw new PaymentException("Something went wrong while creating a payment, "
                    + e.getMessage());
        }
    }

    @Override
    public String paymentSuccess(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            if (!session.getPaymentStatus().equals(SUCCESS_PAYMENT_STATUS)) {
                throw new PaymentException("Payment was not successful");
            }
            StripeSession stripeSession = stripeSessionRepository
                    .findSessionBySessionIdWithPayment(sessionId)
                    .orElseThrow(() -> new EntityNotFoundException("Session with an id of "
                            + sessionId
                            + " not found"));
            Payment payment = stripeSession.getPayment();
            payment.setPaymentStatus(Payment.PaymentStatus.PAID);
            paymentRepository.save(payment);
            PaymentDto paymentDto = paymentMapper.toDto(payment);
            Map<String, Object> paymentMap = paymentMapper.dtoToMap(paymentDto);

            return objectMapper.writeValueAsString(paymentMap);
        } catch (StripeException | JsonProcessingException e) {
            throw new PaymentException("Something went wrong while processing a payment, "
                    + e.getMessage());
        }
    }

    @Override
    public String paymentCancel(String sessionId) {
        try {
            StripeSession stripeSession = stripeSessionRepository
                    .findSessionBySessionIdWithPayment(sessionId)
                    .orElseThrow(() -> new EntityNotFoundException("Session with an id of "
                            + sessionId
                            + " not found"));
            Payment payment = stripeSession.getPayment();
            payment.setPaymentStatus(Payment.PaymentStatus.CANCELLED);
            paymentRepository.save(payment);

            PaymentDto paymentDto = paymentMapper.toDto(payment);
            Map<String, Object> paymentMap = paymentMapper.dtoToMap(paymentDto);

            return objectMapper.writeValueAsString(paymentMap);
        } catch (JsonProcessingException e) {
            throw new PaymentException("Something went wrong while canceling a payment "
                    + e.getMessage());
        }
    }
}
