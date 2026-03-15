/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record Locality(String voivodeship, String city) {

    public Locality {
        if (isBlank(voivodeship)) {
            throw new InvalidArgumentValueException("Voivodeship cannot be blank");
        }

        if (isBlank(city)) {
            throw new InvalidArgumentValueException("City cannot be blank");
        }
    }
}
