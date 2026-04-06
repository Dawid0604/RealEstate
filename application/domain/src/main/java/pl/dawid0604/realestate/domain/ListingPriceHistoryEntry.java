/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.time.Instant;
import java.util.Objects;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class ListingPriceHistoryEntry {
    private final Identifier id;
    private final Money oldPrice;
    private final Instant date;

    private ListingPriceHistoryEntry(
            final Identifier id, final Money oldPrice, final Money newPrice, final Instant date) {

        requireNonNull(id, "Id");
        requireNonNull(oldPrice, "Old price");
        requireNonNull(newPrice, "New price");
        requireNonNull(date, "Date");

        if (date.isAfter(Instant.now())) {
            throw new InvalidArgumentValueException("Date cannot be in the future");
        }

        if (Objects.equals(oldPrice, newPrice)) {
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

    public Identifier getId() {
        return id;
    }

    public Money getOldPrice() {
        return oldPrice;
    }

    public Instant getDate() {
        return date;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof final ListingPriceHistoryEntry that && Objects.equals(that.id, id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
