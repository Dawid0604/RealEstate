/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.math.BigDecimal;
import java.time.Instant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class ListingPriceHistoryEntryTest {

    @Nested
    final class CreateTests {

        @Test
        @DisplayName("Should throw exception when old price is null")
        void shouldThrowExceptionWhenOldPriceIsNull() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> ListingPriceHistoryEntry.create(null, null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Old price cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when new price is null")
        void shouldThrowExceptionWhenNewPriceIsNull() {
            // Given
            final Money oldPrice = new Money(BigDecimal.valueOf(1_500_000), MoneyCurrency.PLN);
            // When
            // Then
            Assertions.assertThatThrownBy(() -> ListingPriceHistoryEntry.create(oldPrice, null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("New price cannot be null");
        }

        @Test
        @DisplayName("Should append current date")
        void shouldAppendCurrentDate() {
            // Given
            final Money oldPrice = new Money(BigDecimal.valueOf(1_500_000), MoneyCurrency.PLN);
            final Money newPrice = new Money(BigDecimal.valueOf(2_500_000), MoneyCurrency.PLN);

            // When
            final ListingPriceHistoryEntry instance =
                    ListingPriceHistoryEntry.create(oldPrice, newPrice);

            // Then
            Assertions.assertThat(instance.getDate())
                    .isNotNull()
                    .matches(
                            d -> d.truncatedTo(SECONDS).equals(Instant.now().truncatedTo(SECONDS)));
        }

        @Test
        @DisplayName("Should generate id")
        void shouldGenerateId() {
            // Given
            final Money oldPrice = new Money(BigDecimal.valueOf(1_500_000), MoneyCurrency.PLN);
            final Money newPrice = new Money(BigDecimal.valueOf(2_500_000), MoneyCurrency.PLN);

            // When
            final ListingPriceHistoryEntry instance =
                    ListingPriceHistoryEntry.create(oldPrice, newPrice);

            // Then
            Assertions.assertThat(instance.getId()).isNotNull();
        }
    }

    @Test
    @DisplayName("Should throw exception when id is null")
    void shouldThrowExceptionWhenIdIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> ListingPriceHistoryEntry.of(null, null, null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Id cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when date is null")
    void shouldThrowExceptionWhenDateIsNull() {
        // Given
        final Money oldPrice = new Money(BigDecimal.valueOf(1_500_000), MoneyCurrency.PLN);
        final Money newPrice = new Money(BigDecimal.valueOf(2_500_000), MoneyCurrency.PLN);

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () ->
                                ListingPriceHistoryEntry.of(
                                        Identifier.generate(), oldPrice, newPrice, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Date cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when date is from the future")
    void shouldThrowExceptionWhenDateIsFromTheFuture() {
        // Given
        final Money oldPrice = new Money(BigDecimal.valueOf(1_500_000), MoneyCurrency.PLN);
        final Money newPrice = new Money(BigDecimal.valueOf(2_500_000), MoneyCurrency.PLN);
        final Instant date = Instant.now().plus(25, DAYS);

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () ->
                                ListingPriceHistoryEntry.of(
                                        Identifier.generate(), oldPrice, newPrice, date))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Date cannot be in the future");
    }

    @Test
    @DisplayName("Should throw exception when old price and new price are same")
    void shouldThrowExceptionWhenOldPriceAndNewPriceAreSame() {
        // Given
        final Money oldPrice = new Money(BigDecimal.valueOf(1_500_000), MoneyCurrency.PLN);
        final Money newPrice = new Money(BigDecimal.valueOf(1_500_000), MoneyCurrency.PLN);

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () ->
                                ListingPriceHistoryEntry.of(
                                        Identifier.generate(), oldPrice, newPrice, Instant.now()))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("New price cannot be equal to old price");
    }

    @Test
    @DisplayName("Should create instance successfully and return same values")
    void shouldCreateInstanceSuccessfullyAndReturnSameValues() {
        // Given
        final Money oldPrice = new Money(BigDecimal.valueOf(1_500_000), MoneyCurrency.PLN);
        final Money newPrice = new Money(BigDecimal.valueOf(2_500_000), MoneyCurrency.PLN);
        final Instant date = Instant.now();
        final Identifier id = Identifier.generate();

        // When
        final ListingPriceHistoryEntry instance =
                ListingPriceHistoryEntry.of(id, oldPrice, newPrice, date);

        // Then
        Assertions.assertThat(instance.getId()).isEqualTo(id);
        Assertions.assertThat(instance.getOldPrice()).isEqualTo(oldPrice);
        Assertions.assertThat(instance.getDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("Should instances are equal")
    void shouldInstancesAreEqual() {
        // Given
        final Money oldPrice = new Money(BigDecimal.valueOf(1_500_000), MoneyCurrency.PLN);
        final Money newPrice = new Money(BigDecimal.valueOf(2_500_000), MoneyCurrency.PLN);
        final Instant date = Instant.now();
        final Identifier id = Identifier.generate();

        // When
        final ListingPriceHistoryEntry instance =
                ListingPriceHistoryEntry.of(id, oldPrice, newPrice, date);

        final ListingPriceHistoryEntry instance2 =
                ListingPriceHistoryEntry.of(
                        instance.getId(), instance.getOldPrice(), newPrice, instance.getDate());

        // Then
        Assertions.assertThat(instance).isEqualTo(instance2);
    }

    @Test
    @DisplayName("Should instances are different")
    void shouldInstancesAreDifferent() {
        // Given
        final Money oldPrice = new Money(BigDecimal.valueOf(1_500_000), MoneyCurrency.PLN);
        final Money newPrice = new Money(BigDecimal.valueOf(2_500_000), MoneyCurrency.PLN);
        final Instant date = Instant.now();
        final Identifier id = Identifier.generate();

        // When
        final ListingPriceHistoryEntry instance =
                ListingPriceHistoryEntry.of(id, oldPrice, newPrice, date);

        final ListingPriceHistoryEntry instance2 =
                ListingPriceHistoryEntry.of(
                        Identifier.generate(),
                        instance.getOldPrice(),
                        newPrice,
                        instance.getDate());

        // Then
        Assertions.assertThat(instance).isNotEqualTo(instance2);
    }
}
