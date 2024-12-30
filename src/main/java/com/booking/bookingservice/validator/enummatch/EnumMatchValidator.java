package com.booking.bookingservice.validator.enummatch;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumMatchValidator implements ConstraintValidator<EnumMatch, String> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumMatch constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equals(value));
        } catch (Exception e) {
            return false;
        }
    }
}
