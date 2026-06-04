/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.regex.Pattern;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record FullName(String firstName, String lastName) {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 128;
    private static final Pattern VALID_VALUE_PATTERN = Pattern.compile("^[\\p{L}\\s'-]+$");

    public FullName {
        if (firstName != null) {
            firstName = firstName.strip();
        }

        if (lastName != null) {
            lastName = lastName.strip();
        }

        validateName(firstName, "FirstName");
        validateName(lastName, "LastName");
    }

    private static void validateName(final String name, final String fieldName) {
        if (isBlank(name)) {
            throw new InvalidArgumentValueException(fieldName + " cannot be blank");
        }

        if (name.length() < MIN_LENGTH) {
            throw new InvalidArgumentValueException(
                    fieldName + " cannot be less than " + MIN_LENGTH + " characters");
        }

        if (name.length() > MAX_LENGTH) {
            throw new InvalidArgumentValueException(
                    fieldName + " cannot be longer than " + MAX_LENGTH + " characters");
        }

        if (!VALID_VALUE_PATTERN.matcher(name).matches()) {
            throw new InvalidArgumentValueException(fieldName + " is invalid");
        }
    }
}
