/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record Description(String value) {
    private static final int MAX_LENGTH = 5000;

    public Description {
        if (isBlank(value)) {
            value = EMPTY;
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Description cannot be longer than " + MAX_LENGTH + " characters");
        }
    }
}
