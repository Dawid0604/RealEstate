/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.event.DomainEvent;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AggregateRoot {
    private final Set<DomainEvent> events = new LinkedHashSet<>();

    public final void addEvent(final DomainEvent event) {
        if (event == null) {
            throw new InvalidArgumentValueException("Event cannot be null");
        }

        events.add(event);
    }

    public final Set<DomainEvent> getEvents() {
        return Set.copyOf(events);
    }

    protected static void requireNonNull(final Object field, final String name) {
        if (field == null) {
            throw new InvalidArgumentValueException(name + " cannot be null");
        }
    }
}
