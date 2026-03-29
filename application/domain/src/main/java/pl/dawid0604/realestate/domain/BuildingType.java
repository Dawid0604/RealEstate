/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

public sealed interface BuildingType
        permits CommercialBuildingType, FlatBuildingType, HouseBuildingType, PlotBuildingType {

    static <T extends Enum<T> & BuildingType> T of(final Class<T> type, final String value) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Value cannot be blank");
        }

        try {
            return Enum.valueOf(type, value.toUpperCase());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value '" + value + "' for " + type.getSimpleName());
        }
    }
}
