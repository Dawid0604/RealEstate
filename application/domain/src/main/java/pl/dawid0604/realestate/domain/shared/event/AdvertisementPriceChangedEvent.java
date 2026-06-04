/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.event;

import java.util.Objects;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.Price;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class AdvertisementPriceChangedEvent extends AdvertisementEvent {
    private final Price oldPrice;
    private final Price newPrice;

    public AdvertisementPriceChangedEvent(
            final Identifier advertisementId, final Price oldPrice, final Price newPrice) {

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

    public Price getOldPrice() {
        return oldPrice;
    }

    public Price getNewPrice() {
        return newPrice;
    }
}
