/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record Title(String value) {
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 100;

    public Title {
        if (value != null) {
            value = value.strip();
        }

        if (isBlank(value)) {
            throw new InvalidArgumentValueException("Title cannot be blank");
        }

        if (value.length() < MIN_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Title cannot be shorter than " + MIN_LENGTH + " characters");
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Title cannot be longer than " + MAX_LENGTH + " characters");
        }
    }
}
