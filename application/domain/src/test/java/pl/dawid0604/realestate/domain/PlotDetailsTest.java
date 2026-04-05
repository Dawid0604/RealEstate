/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class PlotDetailsTest {

    @Test
    @DisplayName("Should throw exception when area is null")
    void shouldThrowExceptionWhenAreaIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new PlotDetails(null, null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Area cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when buildingType is null")
    void shouldThrowExceptionWhenBuildingTypeIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new PlotDetails(getValidArea(), null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("BuildingType cannot be null");
    }

    @Test
    @DisplayName("Should set claims with default value when they are null")
    void shouldSetClaimsWithDefaultValueWhenTheyAreNull() {
        // Given
        // When
        final PlotDetails instance =
                new PlotDetails(getValidArea(), getValidPlotBuildingType(), null);

        // Then
        Assertions.assertThat(instance.getClaims()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should claims are immutable")
    void shouldClaimsAreImmutable() {
        // Given
        final Set<AdvertisementClaim> claims = new HashSet<>();

        // When
        final PlotDetails instance =
                new PlotDetails(getValidArea(), getValidPlotBuildingType(), claims);

        // If claims are copied at initialization time, the following should not be added to the
        // instance claims
        claims.add(new AdvertisementClaim("x,", "y"));

        // Then
        Assertions.assertThat(instance.getClaims() != claims).isTrue();
    }

    @Test
    @DisplayName("Should set type of market with default value")
    void shouldSetTypeOfMarketWithDefaultValue() {
        // Given
        // When
        final PlotDetails instance =
                new PlotDetails(getValidArea(), getValidPlotBuildingType(), null);

        // Then
        Assertions.assertThat(instance.getTypeOfMarket()).isEqualTo(TypeOfMarket.SECONDARY);
    }

    private static Area getValidArea() {
        return new Area(null);
    }

    private static PlotBuildingType getValidPlotBuildingType() {
        return PlotBuildingType.RECREATIONAL;
    }
}
