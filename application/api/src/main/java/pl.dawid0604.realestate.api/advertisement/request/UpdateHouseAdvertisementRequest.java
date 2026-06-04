/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

import pl.dawid0604.realestate.api.validation.ValidBuildingType;
import pl.dawid0604.realestate.api.validation.ValidBuiltYear;
import pl.dawid0604.realestate.api.validation.ValidFloors;
import pl.dawid0604.realestate.api.validation.ValidNumberOfRooms;
import pl.dawid0604.realestate.api.validation.ValidTypeOfMarket;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Getter
@Schema(description = "Update house advertisement action")
public final class UpdateHouseAdvertisementRequest extends UpdateAdvertisementRequest {

    @ValidNumberOfRooms
    @Schema(description = "Advertisement number of rooms", example = "3")
    private final Integer numberOfRooms;

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
    private final HouseBuildingType buildingType;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public UpdateHouseAdvertisementRequest(
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
            final Integer floors,
            final Integer builtYear,
            final TypeOfMarket typeOfMarket,
            final HouseBuildingType buildingType) {

        super(slug, title, description, price, localityId, userEmail, area, claims, featured);
        this.numberOfRooms = numberOfRooms;
        this.floors = floors;
        this.builtYear = builtYear;
        this.typeOfMarket = typeOfMarket;
        this.buildingType = buildingType;
    }
}
