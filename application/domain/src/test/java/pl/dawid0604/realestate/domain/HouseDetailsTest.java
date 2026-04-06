/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class HouseDetailsTest {

    @Test
    @DisplayName("Should throw exception when area is null")
    void shouldThrowExceptionWhenAreaIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> new HouseDetails(null, null, null, null, null, null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Area cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when buildingType is null")
    void shouldThrowExceptionWhenBuildingTypeIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> new HouseDetails(getValidArea(), null, null, null, null, null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("BuildingType cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when numberOfRooms is null")
    void shouldThrowExceptionWhenNumberOfRoomsIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(
                        () ->
                                new HouseDetails(
                                        getValidArea(),
                                        getValidHouseBuildingType(),
                                        null,
                                        null,
                                        null,
                                        null,
                                        null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("NumberOfRooms cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when floors is null")
    void shouldThrowExceptionWhenFloorsIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(
                        () ->
                                new HouseDetails(
                                        getValidArea(),
                                        getValidHouseBuildingType(),
                                        null,
                                        getValidNumberOfRooms(),
                                        null,
                                        null,
                                        null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Floors cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when builtYear is null")
    void shouldThrowExceptionWhenBuiltYearIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(
                        () ->
                                new HouseDetails(
                                        getValidArea(),
                                        getValidHouseBuildingType(),
                                        null,
                                        getValidNumberOfRooms(),
                                        getValidFloors(),
                                        null,
                                        null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("BuiltYear cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when typeOfMarket is null")
    void shouldThrowExceptionWhenTypeOfMarketIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(
                        () ->
                                new HouseDetails(
                                        getValidArea(),
                                        getValidHouseBuildingType(),
                                        null,
                                        getValidNumberOfRooms(),
                                        getValidFloors(),
                                        getValidBuiltYear(),
                                        null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("TypeOfMarket cannot be null");
    }

    @Test
    @DisplayName("Should set claims with default value when they are null")
    void shouldSetClaimsWithDefaultValueWhenTheyAreNull() {
        // Given
        // When
        final HouseDetails instance =
                new HouseDetails(
                        getValidArea(),
                        getValidHouseBuildingType(),
                        null,
                        getValidNumberOfRooms(),
                        getValidFloors(),
                        getValidBuiltYear(),
                        getValidTypeOfMarket());

        // Then
        Assertions.assertThat(instance.getClaims()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should claims are immutable")
    void shouldClaimsAreImmutable() {
        // Given
        final Set<AdvertisementClaim> claims = new HashSet<>();

        // When
        final HouseDetails instance =
                new HouseDetails(
                        getValidArea(),
                        getValidHouseBuildingType(),
                        claims,
                        getValidNumberOfRooms(),
                        getValidFloors(),
                        getValidBuiltYear(),
                        getValidTypeOfMarket());

        // If claims are copied at initialization time, the following should not be added to the
        // instance claims
        claims.add(new AdvertisementClaim("x,", "y"));

        // Then
        Assertions.assertThat(instance.getClaims() != claims).isTrue();
    }

    @Test
    @DisplayName("Should create instance successfully and return same values")
    void shouldCreateInstanceSuccessfullyAndReturnSameValues() {
        // Given
        final Set<AdvertisementClaim> claims = Set.of(new AdvertisementClaim("x", "y"));

        // When
        final HouseDetails instance =
                new HouseDetails(
                        getValidArea(),
                        getValidHouseBuildingType(),
                        claims,
                        getValidNumberOfRooms(),
                        getValidFloors(),
                        getValidBuiltYear(),
                        getValidTypeOfMarket());

        // Then
        Assertions.assertThat(instance.getArea()).isEqualTo(getValidArea());
        Assertions.assertThat(instance.getBuildingType()).isEqualTo(getValidHouseBuildingType());
        Assertions.assertThat(instance.getClaims()).isEqualTo(claims);
        Assertions.assertThat(instance.getNumberOfRooms()).isEqualTo(getValidNumberOfRooms());
        Assertions.assertThat(instance.getFloors()).isEqualTo(getValidFloors());
        Assertions.assertThat(instance.getBuiltYear()).isEqualTo(getValidBuiltYear());
        Assertions.assertThat(instance.getTypeOfMarket()).isEqualTo(getValidTypeOfMarket());
    }

    private static Area getValidArea() {
        return new Area(null);
    }

    private static NumberOfRooms getValidNumberOfRooms() {
        return new NumberOfRooms(null);
    }

    private static Floor getValidFloors() {
        return new Floor(null);
    }

    private static BuiltYear getValidBuiltYear() {
        return new BuiltYear(2011);
    }

    private static HouseBuildingType getValidHouseBuildingType() {
        return HouseBuildingType.DETACHED;
    }

    private static TypeOfMarket getValidTypeOfMarket() {
        return TypeOfMarket.PRIMARY;
    }
}
