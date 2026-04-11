/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.listener;

import static org.mockito.Mockito.mock;

import org.apache.commons.lang3.NotImplementedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.domain.shared.event.AdvertisementPriceChangedEvent;
import pl.dawid0604.realestate.domain.shared.event.AdvertisementStatusChangedEvent;

@ExtendWith(MockitoExtension.class)
class AdvertisementEventListenerTest {

    @Test
    @DisplayName("Should throw NotImplementedException when handling price changed event")
    void shouldThrowNotImplementedExceptionWhenHandlingPriceChangedEvent() {
        // Given
        final AdvertisementEventListener listener = new AdvertisementEventListener();

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> listener.onPriceChanged(mock(AdvertisementPriceChangedEvent.class)))
                .isInstanceOf(NotImplementedException.class);
    }

    @Test
    @DisplayName("Should throw NotImplementedException when handling status changed")
    void shouldThrowNotImplementedExceptionWhenHandlingStatusChangedEvent() {
        // Given
        final AdvertisementEventListener listener = new AdvertisementEventListener();

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> listener.onStatusChanged(mock(AdvertisementStatusChangedEvent.class)))
                .isInstanceOf(NotImplementedException.class);
    }
}
