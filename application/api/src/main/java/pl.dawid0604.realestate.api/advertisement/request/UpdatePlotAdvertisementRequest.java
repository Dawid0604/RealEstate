/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

import pl.dawid0604.realestate.api.validation.ValidBuildingType;
import pl.dawid0604.realestate.domain.PlotBuildingType;

@Getter
@Schema(description = "Update plot advertisement action")
public final class UpdatePlotAdvertisementRequest extends UpdateAdvertisementRequest {

    @ValidBuildingType
    @Schema(description = "Advertisement building type")
    private PlotBuildingType plotType;
}
