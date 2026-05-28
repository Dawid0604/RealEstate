/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

import pl.dawid0604.realestate.api.validation.ValidBuildingType;
import pl.dawid0604.realestate.api.validation.ValidBuiltYear;
import pl.dawid0604.realestate.api.validation.ValidFloor;
import pl.dawid0604.realestate.api.validation.ValidFloors;
import pl.dawid0604.realestate.api.validation.ValidNumberOfRooms;
import pl.dawid0604.realestate.api.validation.ValidTypeOfMarket;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Getter
@Schema(description = "Update commercial advertisement action")
public final class UpdateCommercialAdvertisementRequest extends UpdateAdvertisementRequest {

    @ValidNumberOfRooms
    @Schema(description = "Advertisement number of rooms", example = "3")
    private final Integer numberOfRooms;

    @ValidFloor
    @Schema(description = "Advertisement floor", example = "4")
    private final Integer floor;

    @ValidFloors
    @Schema(description = "Advertisement floors", example = "5")
    private final Integer floors;

    @ValidBuiltYear
    @Schema(description = "Advertisement built year", example = "1998")
    private final Integer builtYear;

    @ValidTypeOfMarket
    @Schema(description = "Advertisement type of market")
    private final TypeOfMarket typeOfMarket;

    @ValidBuildingType
    @Schema(description = "Advertisement building type")
    private final CommercialBuildingType buildingType;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public UpdateCommercialAdvertisementRequest(
            final String slug,
            final String title,
            final String description,
            final BigDecimal price,
            final UUID localityId,
            final String userEmail,
            final BigDecimal area,
            final Map<String, String> claims,
            final Boolean featured,
            final Integer numberOfRooms,
            final Integer floor,
            final Integer floors,
            final Integer builtYear,
            final TypeOfMarket typeOfMarket,
            final CommercialBuildingType buildingType) {

        super(slug, title, description, price, localityId, userEmail, area, claims, featured);
        this.numberOfRooms = numberOfRooms;
        this.floor = floor;
        this.floors = floors;
        this.builtYear = builtYear;
        this.typeOfMarket = typeOfMarket;
        this.buildingType = buildingType;
    }
}
