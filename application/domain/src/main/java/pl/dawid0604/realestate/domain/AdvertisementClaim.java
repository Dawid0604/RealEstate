/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record AdvertisementClaim(String key, String value) {
    private static final int MAX_KEY_LENGTH = 100;
    private static final int MAX_VALUE_LENGTH = 1024;

    public AdvertisementClaim {
        if (isBlank(key)) {
            throw new InvalidArgumentValueException("Key cannot be blank");
        }

        if (isBlank(value)) {
            throw new InvalidArgumentValueException("Value cannot be blank");
        }

        if (key.length() > MAX_KEY_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Key cannot be longer than " + MAX_KEY_LENGTH + " characters");
        }

        if (value.length() > MAX_VALUE_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Value cannot be longer than " + MAX_VALUE_LENGTH + " characters");
        }
    }
}
