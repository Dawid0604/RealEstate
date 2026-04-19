/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public final class ValidBuiltYearValidatorImpl
        implements ConstraintValidator<ValidBuiltYear, Integer> {

    private static final int MAX_YEAR_TOLERANCE = 2;
    private static final int MIN_YEAR = 1800;

    @Override
    public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value < MIN_YEAR) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("BuiltYear cannot be before " + MIN_YEAR)
                    .addConstraintViolation();

            return false;
        }

        final int currentYear = LocalDate.now().getYear();
        final int maxYear = currentYear + MAX_YEAR_TOLERANCE;

        if (value > maxYear) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("BuiltYear cannot be after " + maxYear)
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
