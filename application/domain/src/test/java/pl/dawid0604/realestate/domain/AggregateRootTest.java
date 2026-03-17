/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.shared.event.DomainEvent;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.List;

class AggregateRootTest {
    private AggregateRoot instance;

    @BeforeEach
    void setUp() {
        instance = new AggregateRoot() {};
    }

    @Test
    @DisplayName("Should add event")
    void shouldAddEvent() {
        // Given
        final DomainEvent event = new DomainEvent() {};

        // When
        instance.addEvent(event);

        // Then
        Assertions.assertThat(instance.getEvents()).containsExactly(event);
    }

    @Test
    @DisplayName("Should throw exception while adding when event is null")
    void shouldThrowExceptionWhileAddingWhenEventIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> instance.addEvent(null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Event cannot be null");
    }

    @Test
    void shouldReturnCopiedEventListAtGetter() {
        // Given
        final DomainEvent event = new DomainEvent() {};
        final DomainEvent event2 = new DomainEvent() {};

        // When
        final List<DomainEvent> beforeAddEvents = instance.getEvents();
        instance.addEvent(event);
        instance.addEvent(event2);

        // Then
        final List<DomainEvent> events = instance.getEvents();
        Assertions.assertThat(events).isNotEqualTo(beforeAddEvents);
    }
}
