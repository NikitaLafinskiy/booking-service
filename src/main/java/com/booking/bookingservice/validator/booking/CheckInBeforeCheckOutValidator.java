package com.booking.bookingservice.validator.booking;

import jakarta.validation.ConstraintValidator;
import java.time.LocalDate;
import org.springframework.beans.BeanWrapperImpl;

public class CheckInBeforeCheckOutValidator
        implements ConstraintValidator<CheckInBeforeCheckOut, Object> {
    private String checkInDateField;
    private String checkOutDateField;

    @Override
    public void initialize(CheckInBeforeCheckOut constraintAnnotation) {
        checkInDateField = constraintAnnotation.checkInDateField();
        checkOutDateField = constraintAnnotation.checkOutDateField();
    }

    @Override
    public boolean isValid(Object value, jakarta.validation.ConstraintValidatorContext context) {
        String checkInValue = (String) new BeanWrapperImpl(value)
                .getPropertyValue(checkInDateField);
        String checkOutValue = (String) new BeanWrapperImpl(value)
                .getPropertyValue(checkOutDateField);
        if (checkInValue == null || checkOutValue == null) {
            return false;
        } else {
            LocalDate checkInDate = LocalDate.parse(checkInValue);
            LocalDate checkOutDate = LocalDate.parse(checkOutValue);
            return checkInDate.isBefore(checkOutDate);
        }
    }
}
