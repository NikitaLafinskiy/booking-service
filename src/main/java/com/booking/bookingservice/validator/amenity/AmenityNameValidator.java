package com.booking.bookingservice.validator.amenity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class AmenityNameValidator implements ConstraintValidator<ValidAmenityName, List<String>> {
    @Override
    public boolean isValid(List<String> amenities,
                           ConstraintValidatorContext constraintValidatorContext) {
        return amenities.stream().allMatch(this::isValidAmenity);
    }

    private boolean isValidAmenity(String amenity) {
        try {
            return amenity.length() > 1 && amenity.length() < 255;
        } catch (Exception e) {
            return false;
        }
    }
}
