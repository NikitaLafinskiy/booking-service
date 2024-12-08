package com.booking.bookingservice.payment.controller;

import static com.booking.bookingservice.utils.PaymentTestUtils.setUpCreatePaymentRequestDto;
import static com.booking.bookingservice.utils.PaymentTestUtils.setUpPaymentDto;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.booking.bookingservice.config.WithJwtMockUser;
import com.booking.bookingservice.domain.payment.dto.CreatePaymentRequestDto;
import com.booking.bookingservice.domain.payment.dto.PaymentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Sql(scripts = {"classpath:scripts/clear-tables.sql"})
@Sql(scripts = {"classpath:scripts/user/insert-user.sql",
        "classpath:scripts/accommodation/insert-2-accommodations.sql",
        "classpath:scripts/booking/insert-2-bookings.sql"})
public class PaymentControllerTest {
    public static final int AMOUNT_TO_PAY_ROUNDING_MODE = 0;

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = webAppContextSetup(context)
                .build();
    }

    @Test
    @DisplayName("""
            Given a valid request body
            When POST request is sent to /payments
            Then return a 303 status code with a payment dto and a redirect URL
            """)
    @WithJwtMockUser
    void createPayment_validRequestBody_returnRedirect() throws Exception {
        // Given
        PaymentDto expected = setUpPaymentDto();
        CreatePaymentRequestDto createPaymentRequestDto = setUpCreatePaymentRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(createPaymentRequestDto);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isSeeOther())
                .andReturn();

        // Then
        PaymentDto actual = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                PaymentDto.class);
        String redirectUrl = mvcResult.getResponse()
                .getHeader(HttpHeaders.LOCATION);

        actual.setAmountToPay(actual.getAmountToPay()
                .setScale(AMOUNT_TO_PAY_ROUNDING_MODE));
        assertNotNull(redirectUrl);
        assertTrue(EqualsBuilder.reflectionEquals(expected,
                actual,
                "id"));
    }
}
