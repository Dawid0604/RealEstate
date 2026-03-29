/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

public enum TypeOfMarket {
    SECONDARY,
    PRIMARY;

    public static TypeOfMarket of(final String value) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Value cannot be blank");
        }

        return TypeOfMarket.valueOf(value.toUpperCase());
    }
}
