package com.booking.bookingservice.validator.enummatch;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = EnumMatchValidator.class)
public @interface EnumMatch {
    String message() default "The value provided is not allowed.";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    Class<? extends Enum<?>> enumClass();
}
