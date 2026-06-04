/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.regex.Pattern;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record PhoneNumber(String value) {
    private static final Pattern VALID_VALUE_PATTERN = Pattern.compile("^\\+?[0-9\\s\\-()]{7,15}$");

    public PhoneNumber {
        if (value != null) {
            value = value.strip();
        }

        if (isBlank(value)) {
            throw new InvalidArgumentValueException("Value cannot be blank");
        }

        if (!VALID_VALUE_PATTERN.matcher(value).matches()) {
            throw new InvalidArgumentValueException("Value is invalid");
        }
    }
}
