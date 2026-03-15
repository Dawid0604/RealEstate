/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.math.BigDecimal;
import java.time.Instant;

public final class ListingPriceHistoryEntry {
    private final Identifier id;
    private final Money oldPrice;
    private final Instant date;

    private ListingPriceHistoryEntry(
            final Identifier id, final Money oldPrice, final Money newPrice, final Instant date) {

        requireNonNull(id, "Id");
        requireNonNull(date, "Date");
        requireNonNull(newPrice, "NewPrice");
        requireNonNull(oldPrice, "OldPrice");

        if (date.isAfter(Instant.now())) {
            throw new InvalidArgumentValueException("Date cannot be in the future");
        }

        if (oldPrice.value().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidArgumentValueException("Old price cannot be negative");
        }

        if (newPrice.value().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidArgumentValueException("New price cannot be negative");
        }

        if (oldPrice.value().compareTo(newPrice.value()) == 0) {
            throw new InvalidArgumentValueException("New price cannot be equal to old price");
        }

        this.id = id;
        this.oldPrice = oldPrice;
        this.date = date;
    }

    private static void requireNonNull(final Object field, final String name) {
        if (field == null) {
            throw new InvalidArgumentValueException(name + " cannot be null");
        }
    }

    public static ListingPriceHistoryEntry create(final Money oldPrice, final Money newPrice) {

        return new ListingPriceHistoryEntry(
                Identifier.generate(), oldPrice, newPrice, Instant.now());
    }

    public static ListingPriceHistoryEntry of(
            final Identifier id, final Money oldPrice, final Money newPrice, final Instant date) {

        return new ListingPriceHistoryEntry(id, oldPrice, newPrice, date);
    }
}
