/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PagedResultTest {

    @ParameterizedTest
    @CsvSource(
            value = {
                "0,0", "0,1", "1,0", "0,0", "1,1", "0,25", "0,50", "0,75", "0,100",
            })
    @DisplayName("Should create empty instance and return same values")
    void shouldCreateEmptyInstanceAndReturnSameValues(final int page, final int pageSize) {
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
}
