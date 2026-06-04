/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import static lombok.AccessLevel.NONE;

import static java.util.Collections.emptySet;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

import pl.dawid0604.realestate.domain.PlotBuildingType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Schema(description = "Search plot advertisements request")
public final class SearchPlotAdvertisementsRequest extends SearchAdvertisementsRequest {

    @Getter(NONE)
    @Schema(description = "Building types")
    private final Set<PlotBuildingType> types;

    public Set<PlotBuildingType> getTypes() {
        return Set.copyOf(types);
    }

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public SearchPlotAdvertisementsRequest(
            final BigDecimal areaFrom,
            final BigDecimal areaTo,
            final BigDecimal priceFrom,
            final BigDecimal priceTo,
            final BigDecimal pricePerSquareMeterFrom,
            final BigDecimal pricePerSquareMeterTo,
            final UUID localityId,
            final LocalDate dateFrom,
            final LocalDate dateTo,
            final Set<PlotBuildingType> types) {

        super(
                areaFrom,
                areaTo,
                priceFrom,
                priceTo,
                pricePerSquareMeterFrom,
                pricePerSquareMeterTo,
                localityId,
                dateFrom,
                dateTo);

        this.types = types != null ? Set.copyOf(types) : emptySet();
    }
}
