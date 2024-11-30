package com.booking.bookingservice.validator.booking;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = CheckInBeforeCheckOutValidator.class)
public @interface CheckInBeforeCheckOut {
    String message() default "Check-in date must be before check-out date";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String checkInDateField();

    String checkOutDateField();
}
