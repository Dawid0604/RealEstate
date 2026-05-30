/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.shared.event.DomainEvent;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

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
    @DisplayName("Should return copied event list at getter")
    void shouldReturnCopiedEventListAtGetter() {
        // Given
        final DomainEvent event = new DomainEvent() {};
        final DomainEvent event2 = new DomainEvent() {};

        // When
        final Set<DomainEvent> beforeAddEvents = instance.getEvents();
        instance.addEvent(event);
        instance.addEvent(event2);

        // Then
        final Set<DomainEvent> events = instance.getEvents();
        Assertions.assertThat(events).isNotEqualTo(beforeAddEvents);
    }

    @Test
    @DisplayName("Should throw exception when value is null")
    void shouldThrowExceptionWhenValueIsNull() {
        // Given
        final String fieldName = "xyz";

        // When
        // Then
        Assertions.assertThatThrownBy(() -> AggregateRoot.requireNonNull(null, fieldName))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage(fieldName + " cannot be null");
    }

    @Test
    @DisplayName("Should not throw exception when value is not null")
    void shouldNotThrowExceptionWhenValueIsNotNull() {
        // Given
        final String fieldName = "xyz";
        final Object value = new Object();

        // When
        // Then
        Assertions.assertThatCode(() -> AggregateRoot.requireNonNull(value, fieldName))
                .doesNotThrowAnyException();
    }
}
