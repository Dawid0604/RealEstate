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

@Getter
@Schema(description = "Update flat advertisement action")
public final class UpdateFlatAdvertisementRequest extends UpdateAdvertisementRequest {

    @ValidNumberOfRooms
    @Schema(description = "Advertisement number of rooms", example = "3")
    private Integer numberOfRooms;

    @ValidFloor
    @Schema(description = "Advertisement floor", example = "4")
    private Integer floor;

    @ValidFloors
    @Schema(description = "Advertisement floors", example = "5")
    private Integer floors;

    @ValidBuiltYear
    @Schema(description = "Advertisement built year", example = "1998")
    private Integer builtYear;

    @ValidTypeOfMarket
    @Schema(description = "Advertisement type of market")
    private TypeOfMarket typeOfMarket;

    @ValidBuildingType
    @Schema(description = "Advertisement building type")
    private FlatBuildingType buildingType;
}
