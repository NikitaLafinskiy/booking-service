package com.booking.bookingservice.validator.enumlistmatch;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class EnumListMatchValidator implements ConstraintValidator<EnumListMatch, List<String>> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumListMatch constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        try {
            return values.stream()
                    .allMatch(value -> Arrays.stream(enumClass.getEnumConstants())
                            .anyMatch(e -> e.name().equals(value)));
        } catch (Exception e) {
            return false;
        }
    }
}
