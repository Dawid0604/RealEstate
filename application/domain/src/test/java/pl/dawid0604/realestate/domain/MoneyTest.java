/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class MoneyTest {

    @Test
    @DisplayName("Should create instance successfully with null price")
    void shouldCreateInstanceSuccessfullyWithNullPrice() {
        // Given
        // When
        final Money instance = new Money(null, MoneyCurrency.PLN);

        // Then
        Assertions.assertThat(instance.value()).isNull();
    }

    @Test
    @DisplayName("Should create instance successfully with boundary price")
    void shouldCreateInstanceSuccessfullyWithBoundaryPrice() {
        // Given
        final BigDecimal price = BigDecimal.valueOf(10_000);

        // When
        // Then
        Assertions.assertThatCode(() -> new Money(price, MoneyCurrency.PLN))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should create instance successfully and set value scale")
    void shouldCreateInstanceSuccessfullyAndSetValueScale() {
        // Given
        final BigDecimal price = BigDecimal.valueOf(10_000);

        // When
        final Money money = new Money(price, MoneyCurrency.PLN);

        // Then
        Assertions.assertThat(money.value()).matches(v -> v.scale() == 2);
    }

    @Test
    @DisplayName("Should create instance successfully and return same value and currency")
    void shouldCreateInstanceSuccessfullyAndReturnSameValueAndCurrency() {
        // Given
        final BigDecimal price = BigDecimal.valueOf(2_500_000);
        final MoneyCurrency currency = MoneyCurrency.PLN;

        // When
        final Money instance = new Money(price, currency);

        // Then
        Assertions.assertThat(instance.value()).isEqualByComparingTo(price);
        Assertions.assertThat(instance.currency()).isEqualTo(currency);
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when price is invalid")
    @MethodSource("shouldThrowExceptionWhenPriceIsInvalidDataProvider")
    void shouldThrowExceptionWhenPriceIsInvalid(final BigDecimal price) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Money(price, MoneyCurrency.PLN))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageStartingWith("Value cannot be less than ");
    }

    @Test
    @DisplayName("Should throw exception when currency is null")
    void shouldThrowExceptionWhenCurrencyIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Money(BigDecimal.valueOf(2_500_00), null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Currency cannot be null");
    }

    private static Stream<Arguments> shouldThrowExceptionWhenPriceIsInvalidDataProvider() {

        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-999)),
                Arguments.of(BigDecimal.valueOf(0)),
                Arguments.of(BigDecimal.valueOf(999)),
                Arguments.of(BigDecimal.valueOf(9999)));
    }
}
