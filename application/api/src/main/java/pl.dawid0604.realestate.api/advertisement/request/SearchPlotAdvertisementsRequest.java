/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import pl.dawid0604.realestate.domain.PlotBuildingType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Search plot advertisements request")
public record SearchPlotAdvertisementsRequest(
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
        @Schema(description = "Plot types") Set<PlotBuildingType> types,
        @NotNull(message = "Value cannot be null") @Schema(
                        description = "Locality id",
                        example = "019e2325-d92b-70ad-94e3-609123e34a79")
                UUID localityId,
        @Schema(description = "Date from value", example = "2026-01-01") LocalDate dateFrom,
        @Schema(description = "Date to value", example = "2026-02-02") LocalDate dateTo) {}
