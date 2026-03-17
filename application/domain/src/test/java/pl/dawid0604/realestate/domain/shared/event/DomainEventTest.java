/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.event;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DomainEventTest {
    private DomainEvent event;

    @BeforeEach
    void init() {
        event = new DomainEvent() {};
    }

    @Test
    @DisplayName("Should set OccurredAt properly")
    void shouldSetOccurredAtProperly() {
        // Given
        // When
        // Then
        Assertions.assertThat(event.getOccurredAt()).isNotNull();
    }
}
