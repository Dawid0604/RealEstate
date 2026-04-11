/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.event;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.MoneyCurrency;
import pl.dawid0604.realestate.domain.Price;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class AdvertisementPriceChangedEventTest {

    @Test
    @DisplayName("Should throw exception when advertisementId is null")
    void shouldThrowExceptionWhenAdvertisementIdIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new AdvertisementPriceChangedEvent(null, null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("AdvertisementId cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when old price is null")
    void shouldThrowExceptionWhenOldPriceIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> new AdvertisementPriceChangedEvent(getValidId(), null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Old price cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when new price is null")
    void shouldThrowExceptionWhenNewPriceIsNull() {
        // Given
        final Price oldPrice = new Price(BigDecimal.valueOf(2_500_00d), MoneyCurrency.PLN);

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> new AdvertisementPriceChangedEvent(getValidId(), oldPrice, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("New price cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when new prices are same")
    void shouldThrowExceptionWhenNewPricesAreSame() {
        // Given
        final Price oldPrice = new Price(BigDecimal.valueOf(2_500_00d), MoneyCurrency.PLN);
        final Price newPrice = new Price(oldPrice.value(), oldPrice.currency());

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> new AdvertisementPriceChangedEvent(getValidId(), oldPrice, newPrice))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Prices cannot be same");
    }

    @Test
    @DisplayName("Should create instance successfully and get same values")
    void shouldCreateInstanceSuccessfullyAndGetSameValues() {
        // Given
        final Price oldPrice = new Price(BigDecimal.valueOf(2_500_00d), MoneyCurrency.PLN);
        final Price newPrice = new Price(BigDecimal.valueOf(1_500_00d), MoneyCurrency.PLN);
        final Identifier advertisementId = getValidId();

        // When
        final AdvertisementPriceChangedEvent instance =
                new AdvertisementPriceChangedEvent(advertisementId, oldPrice, newPrice);

        // Then
        Assertions.assertThat(instance.getOldPrice()).isEqualTo(oldPrice);
        Assertions.assertThat(instance.getNewPrice()).isEqualTo(newPrice);
        Assertions.assertThat(instance.getAdvertisementId()).isEqualTo(advertisementId);
    }

    private static Identifier getValidId() {
        return Identifier.generate();
    }
}
