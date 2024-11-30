package com.booking.bookingservice.validator.amenity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = AmenityNameValidator.class)
public @interface ValidAmenityName {
    Class<? extends Payload>[] payload() default {};

    String message() default "Invalid amenity name";

    Class<?>[] groups() default {};
}
