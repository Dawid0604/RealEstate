/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

import pl.dawid0604.realestate.api.validation.ValidBuildingType;
import pl.dawid0604.realestate.domain.PlotBuildingType;

@Getter
@Schema(description = "Create plot advertisement action")
public final class CreatePlotAdvertisementRequest extends CreateAdvertisementRequest {

    @ValidBuildingType
    @Schema(description = "Advertisement plot type")
    private PlotBuildingType buildingType;
}
