/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record AdvertisementClaim(String key, String value) {
    public AdvertisementClaim {
        if (isBlank(key)) {
            throw new InvalidArgumentValueException("Key cannot be blank");
        }

        if (isBlank(value)) {
            throw new InvalidArgumentValueException("Value cannot be blank");
        }
    }
}
