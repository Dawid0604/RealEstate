/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import static lombok.AccessLevel.NONE;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.PositiveOrZero;

import lombok.Getter;

import pl.dawid0604.realestate.api.validation.ValidBuiltYear;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.util.Set;

@Getter
@SuppressWarnings("CPD-START")
@Schema(description = "Search house advertisements request")
public final class SearchHouseAdvertisementsRequest extends SearchAdvertisementsRequest {

    @Getter(NONE)
    @Schema(description = "Building types")
    private Set<HouseBuildingType> types;

    @Getter(NONE)
    @Schema(description = "Type of markets")
    private Set<TypeOfMarket> typeOfMarkets;

    @Schema(description = "Floors from value", example = "3")
    private @PositiveOrZero(message = "Value cannot be lower than 0") Integer floorsFrom;

    @Schema(description = "Floors to value", example = "4")
    private @PositiveOrZero(message = "Value cannot be lower than 0") Integer floorsTo;

    @Schema(description = "Number of rooms from value", example = "3")
    private @PositiveOrZero(message = "Value cannot be lower than 0") Integer numberOfRoomsFrom;

    @Schema(description = "Number of rooms to value", example = "4")
    private @PositiveOrZero(message = "Value cannot be lower than 0") Integer numberOfRoomsTo;

    @ValidBuiltYear
    @Schema(description = "Built year from value", example = "2011")
    private Integer builtYearFrom;

    @Schema(description = "Built year to value", example = "2012")
    private Integer builtYearTo;

    public Set<HouseBuildingType> getTypes() {
        return Set.copyOf(types);
    }

    public Set<TypeOfMarket> getTypeOfMarkets() {
        return Set.copyOf(typeOfMarkets);
    }
}
