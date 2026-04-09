/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PagedResultTest {

    @Nested
    final class EmptyMethodTests {

        @ParameterizedTest
        @CsvSource(
                value = {
                    "0,25", "0,1", "1,25", "1,1", "0,25", "0,50", "0,75", "0,100",
                })
        @DisplayName("Should create instance and return same values")
        void shouldCreateInstanceAndReturnSameValues(final int page, final int pageSize) {
            // Given
            // When
            final PagedResult<?> instance = PagedResult.empty(page, pageSize);

            // Then
            Assertions.assertThat(instance).isNotNull();
            Assertions.assertThat(instance.getPage()).isEqualTo(page);
            Assertions.assertThat(instance.getPageSize()).isEqualTo(pageSize);
            Assertions.assertThat(instance.getTotalElements()).isEqualTo(0);
            Assertions.assertThat(instance.getTotalPages()).isEqualTo(0);
            Assertions.assertThat(instance.getItems()).isEmpty();
            Assertions.assertThat(instance.hasNext()).isFalse();
            Assertions.assertThat(instance.hasPrevious()).isEqualTo(page > 0);
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        @DisplayName("Should throw exception when page is invalid")
        void shouldThrowExceptionWhenPageIsInvalid(final Integer page) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> PagedResult.empty(page, 25))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Page cannot be negative");
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1, 0})
        @DisplayName("Should throw exception when pageSize is invalid")
        void shouldThrowExceptionWhenPageSizeIsInvalid(final Integer pageSize) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> PagedResult.empty(1, pageSize))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessageStartingWith("Page size must be between ");
        }
    }

    @Test
    @DisplayName("Should set empty list when items list is null")
    void shouldSetEmptyListWhenItemsListIsNull() {
        // Given
        // When
        final PagedResult<String> instance = PagedResult.of(null, 0, 25, 0);

        // Then
        Assertions.assertThat(instance.getItems()).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    @DisplayName("Should throw exception when page is invalid")
    void shouldThrowExceptionWhenPageIsInvalid(final int page) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> PagedResult.of(List.of(), page, 25, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page cannot be negative");
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    @DisplayName("Should throw exception when pageSize is invalid")
    void shouldThrowExceptionWhenPageSizeIsInvalid(final int pageSize) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> PagedResult.of(List.of(), 1, pageSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Page size must be between ");
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    @DisplayName("Should throw exception when total elements is invalid")
    void shouldThrowExceptionWhenTotalElementsIsInvalid(final int totalElements) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> PagedResult.of(List.of(), 1, 25, totalElements))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Total elements cannot be negative");
    }

    @ParameterizedTest
    @ValueSource(ints = {101, 250, 1000})
    @DisplayName("Should throw exception when page size exceeded limit")
    void shouldThrowExceptionWhenPageSizeExceededLimit(final int pageSize) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> PagedResult.of(List.of(), 1, pageSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Page size must be between ");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 100})
    @DisplayName("Should create instance with boundary page size value")
    void shouldCreateInstanceWithBoundaryPageSizeValue(final int pageSize) {
        // Given
        // When
        final PagedResult<String> instance = PagedResult.of(List.of(), 0, pageSize, 0);

        // Then
        Assertions.assertThat(instance.getPageSize()).isEqualTo(pageSize);
    }

    @Test
    @DisplayName("Should create instance")
    void shouldCreateInstance() {
        // Given
        final List<String> items = List.of("a", "b", "c");
        final int page = 2;
        final int pageSize = 100;
        final int totalElements = 25;

        // When
        final PagedResult<String> instance = PagedResult.of(items, page, pageSize, totalElements);

        // Then
        Assertions.assertThat(instance).isNotNull();
        Assertions.assertThat(instance.getPage()).isEqualTo(page);
        Assertions.assertThat(instance.getPageSize()).isEqualTo(pageSize);
        Assertions.assertThat(instance.getTotalElements()).isEqualTo(totalElements);
        Assertions.assertThat(instance.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(instance.getItems()).isEqualTo(items);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,25,50,true", "2,25,50,false", "0,25,50,true", "0,25,0,false"})
    @DisplayName("Should return proper hasNext value")
    void shouldReturnProperHasNextValue(
            final int page,
            final int pageSize,
            final int totalElements,
            final boolean expectedValue) {

        // Given
        final List<String> items = List.of("a", "b", "c");

        // When
        final PagedResult<String> instance = PagedResult.of(items, page, pageSize, totalElements);

        // Then
        Assertions.assertThat(instance.hasNext()).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @CsvSource(value = {"0,false", "1,true", "10,true", "100,true"})
    @DisplayName("Should return proper hasPrevious value")
    void shouldReturnProperHasNextValue(final int page, final boolean expectedValue) {

        // Given
        final List<String> items = List.of("a", "b", "c");

        // When
        final PagedResult<String> instance = PagedResult.of(items, page, 25, 50);

        // Then
        Assertions.assertThat(instance.hasPrevious()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("should return immutable list at getter")
    void shouldReturnImmutableListAtGetter() {
        // Given
        final List<String> items = List.of("a", "b", "c");
        final int page = 2;
        final int pageSize = 100;
        final int totalElements = 25;

        // When
        final PagedResult<String> instance = PagedResult.of(items, page, pageSize, totalElements);

        // Then
        Assertions.assertThatCode(() -> instance.getItems().add("d"))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
