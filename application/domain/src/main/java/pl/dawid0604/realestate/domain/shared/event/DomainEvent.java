/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.event;

import java.time.Instant;

public abstract sealed class DomainEvent permits AdvertisementEvent {
    private final Instant occurredAt;

    protected DomainEvent() {
        this.occurredAt = Instant.now();
    }

    public final Instant getOccurredAt() {
        return occurredAt;
    }
}
