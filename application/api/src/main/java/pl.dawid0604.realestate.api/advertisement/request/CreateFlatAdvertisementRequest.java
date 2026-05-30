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
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Schema(description = "Create flat advertisement action")
public final class CreateFlatAdvertisementRequest extends CreateAdvertisementRequest {

    @Schema(description = "Advertisement number of rooms", example = "3")
    @ValidNumberOfRooms
    private final Integer numberOfRooms;

    @Schema(description = "Advertisement floor", example = "4")
    @ValidFloor
    private final Integer floor;

    @Schema(description = "Advertisement floors", example = "5")
    @ValidFloors
    private final Integer floors;

    @Schema(description = "Advertisement built year", example = "1998")
    @ValidBuiltYear
    private final Integer builtYear;

    @Schema(description = "Advertisement type of market")
    @ValidTypeOfMarket
    private final TypeOfMarket typeOfMarket;

    @Schema(description = "Advertisement building type")
    @ValidBuildingType
    private final FlatBuildingType buildingType;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public CreateFlatAdvertisementRequest(
            final String title,
            final String description,
            final BigDecimal price,
            final UUID localityId,
            final String userEmail,
            final Set<AdvertisementPhotoRequest> photos,
            final BigDecimal area,
            final Map<String, String> claims,
            final Boolean featured,
            final Integer numberOfRooms,
            final Integer floor,
            final Integer floors,
            final Integer builtYear,
            final TypeOfMarket typeOfMarket,
            final FlatBuildingType buildingType) {

        super(title, description, price, localityId, userEmail, photos, area, claims, featured);
        this.numberOfRooms = numberOfRooms;
        this.floor = floor;
        this.floors = floors;
        this.builtYear = builtYear;
        this.typeOfMarket = typeOfMarket;
        this.buildingType = buildingType;
    }
}
