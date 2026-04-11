/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Locale;

public enum UserType {
    PRIVATE_OWNER,
    AGENCY,
    DEVELOPER;

    public static UserType of(final String value) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Value cannot be blank");
        }

        return UserType.valueOf(value.toUpperCase(Locale.forLanguageTag("pl-PL")));
    }
}
