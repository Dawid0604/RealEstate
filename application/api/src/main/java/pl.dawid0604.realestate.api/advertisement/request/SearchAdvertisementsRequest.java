/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public abstract sealed class SearchAdvertisementsRequest
        permits SearchCommercialAdvertisementsRequest,
                SearchFlatAdvertisementsRequest,
                SearchHouseAdvertisementsRequest,
                SearchPlotAdvertisementsRequest {

    @Schema(description = "Area from value", example = "34.5")
    @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
    private final BigDecimal areaFrom;

    @Schema(description = "Area to value", example = "44.5")
    @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
    private final BigDecimal areaTo;

    @Schema(description = "Price from value", example = "250000")
    @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
    private final BigDecimal priceFrom;

    @Schema(description = "Price to value", example = "350000")
    @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
    private final BigDecimal priceTo;

    @Schema(description = "PricePerSquareMeter from value", example = "3500")
    @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
    private final BigDecimal pricePerSquareMeterFrom;

    @Schema(description = "PricePerSquareMeter to value", example = "4500")
    @DecimalMin(value = "0.01", message = "Value must be greater than 0.01")
    private final BigDecimal pricePerSquareMeterTo;

    @Schema(description = "Locality id", example = "019e2325-d92b-70ad-94e3-609123e34a79")
    @NotNull(message = "Value cannot be null") private final UUID localityId;

    @Schema(description = "Date from value", example = "2026-01-01")
    private final LocalDate dateFrom;

    @Schema(description = "Date to value", example = "2026-02-02")
    private final LocalDate dateTo;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    protected SearchAdvertisementsRequest(
            final BigDecimal areaFrom,
            final BigDecimal areaTo,
            final BigDecimal priceFrom,
            final BigDecimal priceTo,
            final BigDecimal pricePerSquareMeterFrom,
            final BigDecimal pricePerSquareMeterTo,
            final UUID localityId,
            final LocalDate dateFrom,
            final LocalDate dateTo) {

        this.areaFrom = areaFrom;
        this.areaTo = areaTo;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.pricePerSquareMeterFrom = pricePerSquareMeterFrom;
        this.pricePerSquareMeterTo = pricePerSquareMeterTo;
        this.localityId = localityId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
