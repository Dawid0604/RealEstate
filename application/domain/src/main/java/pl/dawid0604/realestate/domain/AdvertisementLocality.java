/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record AdvertisementLocality(Identifier id) {

    public AdvertisementLocality {
        if (id == null) {
            throw new InvalidArgumentValueException("Id cannot be null");
        }
    }
}
