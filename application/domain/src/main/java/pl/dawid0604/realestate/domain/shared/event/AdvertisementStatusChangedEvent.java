/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.event;

import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class AdvertisementStatusChangedEvent extends AdvertisementEvent {
    private final AdvertisementStatus oldStatus;
    private final AdvertisementStatus newStatus;

    public AdvertisementStatusChangedEvent(
            final Identifier advertisementId,
            final AdvertisementStatus oldStatus,
            final AdvertisementStatus newStatus) {

        super(advertisementId);

        if (oldStatus == null) {
            throw new InvalidArgumentValueException("Old status cannot be null");
        }

        if (newStatus == null) {
            throw new InvalidArgumentValueException("New status cannot be null");
        }

        if (oldStatus == newStatus) {
            throw new InvalidArgumentValueException("Old status and new status cannot be the same");
        }

        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}
