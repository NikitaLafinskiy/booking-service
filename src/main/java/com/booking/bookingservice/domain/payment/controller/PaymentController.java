package com.booking.bookingservice.domain.payment.controller;

import com.booking.bookingservice.domain.payment.dto.CreatePaymentRequestDto;
import com.booking.bookingservice.domain.payment.dto.PaymentDto;
import com.booking.bookingservice.domain.payment.service.PaymentService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    public static final int REDIRECT_STATUS_CODE = 303;
    public static final String SUCCESS_ROUTE = "/payments/success";
    public static final String CANCEL_ROUTE = "/payments/cancel";

    private final PaymentService paymentService;

    @Value("${app.client-domain}")
    private String clientDomain;

    @Operation(summary = "Get payment details",
            description = "Get payment details by user id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Payment details retrieved successfully"),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized"),
            })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public PaymentDto getPayment(@RequestParam Long userId,
                                 Authentication authentication) {
        return paymentService.getPayment(userId, authentication);
    }

    @Operation(summary = "Create a payment",
            description = "Create a payment with payment details",
            responses = {
                    @ApiResponse(responseCode = "303",
                            description = "Payment initiated, redirecting to a payment gateway"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid request body"),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized"),
            })
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> createPayment(
            @AuthenticationPrincipal UserDto userDto,
            @RequestBody CreatePaymentRequestDto createPaymentRequestDto) {
        List<String> paymentDetails = paymentService.createPayment(
                createPaymentRequestDto,
                userDto);
        String paymentDto = paymentDetails.getFirst();
        String paymentUrl = paymentDetails.getLast();

        return ResponseEntity
                .status(HttpStatusCode.valueOf(REDIRECT_STATUS_CODE))
                .location(URI.create(paymentUrl))
                .body(paymentDto);
    }

    @Operation(summary = "Payment success",
            description = "Handle payment success",
            responses = {
                    @ApiResponse(responseCode = "303",
                            description = "Redirect to a success page"),
                    @ApiResponse(responseCode = "500",
                            description = "Something went wrong while processing a payment"),
            })
    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam String sessionId) {
        String paymentDto = paymentService.paymentSuccess(sessionId);
        return ResponseEntity.status(HttpStatusCode.valueOf(REDIRECT_STATUS_CODE))
                .location(URI.create(clientDomain + SUCCESS_ROUTE))
                .body(paymentDto);
    }

    @Operation(summary = "Payment cancel",
            description = "Handle payment cancel",
            responses = {
                    @ApiResponse(responseCode = "303",
                            description = "Redirect to a cancel page"),
                    @ApiResponse(responseCode = "500",
                            description = "Something went wrong while canceling a payment"),
            })
    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel(@RequestParam String sessionId) {
        String paymentDto = paymentService.paymentCancel(sessionId);
        return ResponseEntity.status(HttpStatusCode.valueOf(REDIRECT_STATUS_CODE))
                .location(URI.create(clientDomain + CANCEL_ROUTE))
                .body(paymentDto);
    }
}
