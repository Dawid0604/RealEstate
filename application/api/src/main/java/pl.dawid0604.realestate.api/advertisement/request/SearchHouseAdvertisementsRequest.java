/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import static lombok.AccessLevel.NONE;

import static java.util.Collections.emptySet;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.PositiveOrZero;

import lombok.Getter;

import pl.dawid0604.realestate.api.validation.ValidBuiltYear;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@SuppressWarnings("CPD-START")
@Schema(description = "Search house advertisements request")
public final class SearchHouseAdvertisementsRequest extends SearchAdvertisementsRequest {

    @Getter(NONE)
    @Schema(description = "Building types")
    private final Set<HouseBuildingType> types;

    @Getter(NONE)
    @Schema(description = "Type of markets")
    private final Set<TypeOfMarket> typeOfMarkets;

    @Schema(description = "Floors from value", example = "3")
    private @PositiveOrZero(message = "Value cannot be lower than 0") final Integer floorsFrom;

    @Schema(description = "Floors to value", example = "4")
    private @PositiveOrZero(message = "Value cannot be lower than 0") final Integer floorsTo;

    @Schema(description = "Number of rooms from value", example = "3")
    private @PositiveOrZero(message = "Value cannot be lower than 0") final Integer
            numberOfRoomsFrom;

    @Schema(description = "Number of rooms to value", example = "4")
    private @PositiveOrZero(message = "Value cannot be lower than 0") final Integer numberOfRoomsTo;

    @ValidBuiltYear
    @Schema(description = "Built year from value", example = "2011")
    private final Integer builtYearFrom;

    @Schema(description = "Built year to value", example = "2012")
    private final Integer builtYearTo;

    public Set<HouseBuildingType> getTypes() {
        return Set.copyOf(types);
    }

    public Set<TypeOfMarket> getTypeOfMarkets() {
        return Set.copyOf(typeOfMarkets);
    }

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public SearchHouseAdvertisementsRequest(
            final BigDecimal areaFrom,
            final BigDecimal areaTo,
            final BigDecimal priceFrom,
            final BigDecimal priceTo,
            final BigDecimal pricePerSquareMeterFrom,
            final BigDecimal pricePerSquareMeterTo,
            final UUID localityId,
            final LocalDate dateFrom,
            final LocalDate dateTo,
            final Set<HouseBuildingType> types,
            final Set<TypeOfMarket> typeOfMarkets,
            final Integer floorsFrom,
            final Integer floorsTo,
            final Integer numberOfRoomsFrom,
            final Integer numberOfRoomsTo,
            final Integer builtYearFrom,
            final Integer builtYearTo) {

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
        this.typeOfMarkets = typeOfMarkets != null ? Set.copyOf(typeOfMarkets) : emptySet();
        this.floorsFrom = floorsFrom;
        this.floorsTo = floorsTo;
        this.numberOfRoomsFrom = numberOfRoomsFrom;
        this.numberOfRoomsTo = numberOfRoomsTo;
        this.builtYearFrom = builtYearFrom;
        this.builtYearTo = builtYearTo;
    }
}
