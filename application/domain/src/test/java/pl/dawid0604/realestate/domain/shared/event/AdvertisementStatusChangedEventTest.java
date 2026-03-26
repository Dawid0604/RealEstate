/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.event;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class AdvertisementStatusChangedEventTest {

    @Test
    @DisplayName("Should throw exception when advertisementId is null")
    void shouldThrowExceptionWhenAdvertisementIdIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new AdvertisementStatusChangedEvent(null, null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("AdvertisementId cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when old status is null")
    void shouldThrowExceptionWhenOldStatusIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> new AdvertisementStatusChangedEvent(getValidId(), null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Old status cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when new status is null")
    void shouldThrowExceptionWhenNewStatusIsNull() {
        // Given
        final AdvertisementStatus oldStatus = AdvertisementStatus.ACTIVE;

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> new AdvertisementStatusChangedEvent(getValidId(), oldStatus, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("New status cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when new statuses are same")
    void shouldThrowExceptionWhenNewStatusesAreSame() {
        // Given
        final AdvertisementStatus oldStatus = AdvertisementStatus.SOLD;
        final AdvertisementStatus newStatus = AdvertisementStatus.SOLD;

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () ->
                                new AdvertisementStatusChangedEvent(
                                        getValidId(), oldStatus, newStatus))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Statuses cannot be same");
    }

    @Test
    @DisplayName("Should create instance successfully and get same values")
    void shouldCreateInstanceSuccessfullyAndGetSameValues() {
        // Given
        final AdvertisementStatus oldStatus = AdvertisementStatus.ACTIVE;
        final AdvertisementStatus newStatus = AdvertisementStatus.INACTIVE;
        final Identifier advertisementId = getValidId();

        // When
        final AdvertisementStatusChangedEvent instance =
                new AdvertisementStatusChangedEvent(advertisementId, oldStatus, newStatus);

        // Then
        Assertions.assertThat(instance.getOldStatus()).isEqualTo(oldStatus);
        Assertions.assertThat(instance.getNewStatus()).isEqualTo(newStatus);
        Assertions.assertThat(instance.getAdvertisementId()).isEqualTo(advertisementId);
    }

    private static Identifier getValidId() {
        return Identifier.generate();
    }
}
