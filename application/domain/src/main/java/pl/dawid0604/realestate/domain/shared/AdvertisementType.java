/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Locale;

public enum AdvertisementType {
    FLAT,
    HOUSE,
    COMMERCIAL,
    PLOT;

    public static AdvertisementType of(final String value) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Value cannot be blank");
        }

        return AdvertisementType.valueOf(value.toUpperCase(Locale.forLanguageTag("pl-PL")));
    }
}
