/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.event;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public abstract sealed class AdvertisementEvent extends DomainEvent
        permits AdvertisementPriceChangedEvent, AdvertisementStatusChangedEvent {

    private final Identifier advertisementId;

    protected AdvertisementEvent(final Identifier advertisementId) {
        if (advertisementId == null) {
            throw new InvalidArgumentValueException("AdvertisementId cannot be null");
        }

        this.advertisementId = advertisementId;
    }

    public final Identifier getAdvertisementId() {
        return advertisementId;
    }
}
