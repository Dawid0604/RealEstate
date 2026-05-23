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

@Getter
@Schema(description = "Create house advertisement action")
public final class CreateHouseAdvertisementRequest extends CreateAdvertisementRequest {

    @Schema(description = "Advertisement number of rooms", example = "3")
    @ValidNumberOfRooms
    private Integer numberOfRooms;

    @Schema(description = "Advertisement floors", example = "5")
    @ValidFloors
    private Integer floors;

    @Schema(description = "Advertisement built year", example = "1998")
    @ValidBuiltYear
    private Integer builtYear;

    @Schema(description = "Advertisement type of market")
    @ValidTypeOfMarket
    private TypeOfMarket typeOfMarket;

    @Schema(description = "Advertisement building type")
    @ValidBuildingType
    private HouseBuildingType buildingType;
}
