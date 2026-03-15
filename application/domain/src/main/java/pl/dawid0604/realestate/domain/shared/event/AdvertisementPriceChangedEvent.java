/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.event;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.Money;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.Objects;

public final class AdvertisementPriceChangedEvent extends AdvertisementEvent {
    private final Money oldPrice;
    private final Money newPrice;

    public AdvertisementPriceChangedEvent(
            final Identifier advertisementId, final Money oldPrice, final Money newPrice) {

        super(advertisementId);

        if (oldPrice == null) {
            throw new InvalidArgumentValueException("Old price cannot be null");
        }

        if (newPrice == null) {
            throw new InvalidArgumentValueException("New price cannot be null");
        }

        if (Objects.equals(oldPrice, newPrice)) {
            throw new InvalidArgumentValueException("Prices cannot be same");
        }

        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }
}
