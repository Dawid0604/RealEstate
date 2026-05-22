/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import pl.dawid0604.realestate.api.validation.ValidBuiltYear;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Search flat advertisements request")
public record SearchFlatAdvertisementsRequest(
        @Schema(description = "Area from value", example = "34.5")
                @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
                BigDecimal areaFrom,
        @Schema(description = "Area to value", example = "44.5")
                @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
                BigDecimal areaTo,
        @Schema(description = "Price from value", example = "250000")
                @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
                BigDecimal priceFrom,
        @Schema(description = "Price to value", example = "350000")
                @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
                BigDecimal priceTo,
        @Schema(description = "PricePerSquareMeter from value", example = "3500")
                @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
                BigDecimal pricePerSquareMeterFrom,
        @Schema(description = "PricePerSquareMeter to value", example = "4500")
                @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
                BigDecimal pricePerSquareMeterTo,
        @Schema(description = "Building types") Set<FlatBuildingType> types,
        @Schema(description = "Locality id", example = "019e2325-d92b-70ad-94e3-609123e34a79")
                @NotNull(message = "Value cannot be null") UUID localityId,
        @Schema(description = "Date from value", example = "2026-01-01") LocalDate dateFrom,
        @Schema(description = "Date to value", example = "2026-02-02") LocalDate dateTo,
        @Schema(description = "Type of markets") Set<TypeOfMarket> typeOfMarkets,
        @Schema(description = "Floor from value", example = "1")
                @PositiveOrZero(message = "Value cannot be lower than 0")
                Integer floorFrom,
        @Schema(description = "Floor to value", example = "2")
                @PositiveOrZero(message = "Value cannot be lower than 0")
                Integer floorTo,
        @Schema(description = "Floors from value", example = "3")
                @PositiveOrZero(message = "Value cannot be lower than 0")
                Integer floorsFrom,
        @Schema(description = "Floors to value", example = "4")
                @PositiveOrZero(message = "Value cannot be lower than 0")
                Integer floorsTo,
        @Schema(description = "Number of rooms from value", example = "3")
                @PositiveOrZero(message = "Value cannot be lower than 0")
                Integer numberOfRoomsFrom,
        @Schema(description = "Number of rooms to value", example = "4")
                @PositiveOrZero(message = "Value cannot be lower than 0")
                Integer numberOfRoomsTo,
        @ValidBuiltYear @Schema(description = "Built year from value", example = "2011")
                Integer builtYearFrom,
        @Schema(description = "Built year to value", example = "2012") Integer builtYearTo) {}
