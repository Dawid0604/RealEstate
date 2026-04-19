/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.LinkedList;
import java.util.List;

import pl.dawid0604.realestate.domain.shared.event.DomainEvent;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public abstract class AggregateRoot {
    private final List<DomainEvent> events = new LinkedList<>();

    public final void addEvent(final DomainEvent event) {
        if (event == null) {
            throw new InvalidArgumentValueException("Event cannot be null");
        }

        events.add(event);
    }

    public final List<DomainEvent> getEvents() {
        return List.copyOf(events);
    }

    protected static void requireNonNull(final Object field, final String name) {
        if (field == null) {
            throw new InvalidArgumentValueException(name + " cannot be null");
        }
    }
}
