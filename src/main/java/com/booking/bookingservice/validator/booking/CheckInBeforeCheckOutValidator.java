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
        try {
            LocalDate checkInDate = (LocalDate) new BeanWrapperImpl(value)
                    .getPropertyValue(checkInDateField);
            LocalDate checkOutDate = (LocalDate) new BeanWrapperImpl(value)
                    .getPropertyValue(checkOutDateField);
            if (checkInDate == null || checkOutDate == null) {
                return false;
            } else {
                return checkInDate.isBefore(checkOutDate);
            }
        } catch (Exception e) {
            return false;
        }
    }
}
