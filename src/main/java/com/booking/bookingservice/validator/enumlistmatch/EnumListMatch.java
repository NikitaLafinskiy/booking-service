package com.booking.bookingservice.validator.enumlistmatch;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = EnumListMatchValidator.class)
public @interface EnumListMatch {
    String message() default "The values provided are not allowed.";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    Class<? extends Enum<?>> enumClass();
}
