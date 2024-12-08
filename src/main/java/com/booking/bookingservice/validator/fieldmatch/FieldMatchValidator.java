package com.booking.bookingservice.validator.fieldmatch;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstField();
        secondFieldName = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object firstFieldValue = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
            Object secondFieldValue = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);
            return Objects.equals(firstFieldValue, secondFieldValue);
        } catch (Exception e) {
            return false;
        }
    }
}
