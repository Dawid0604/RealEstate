/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.apache.commons.lang3.StringUtils;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record Description(String value) {
    private static final int MAX_LENGTH = 5000;

    public Description {
        if (value != null) {
            value = value.strip();
        }

        if (StringUtils.length(value) > MAX_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Description cannot be longer than " + MAX_LENGTH + " characters");
        }

        value = isBlank(value) ? null : value;
    }
}
