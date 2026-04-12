/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Locale;

public enum AdvertisementStatus {
    ACTIVE,
    INACTIVE,
    SOLD,
    DELETED;

    public static AdvertisementStatus of(final String value) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Value cannot be blank");
        }

        return AdvertisementStatus.valueOf(value.toUpperCase(Locale.forLanguageTag("pl-PL")));
    }
}
