/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import static lombok.AccessLevel.NONE;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

import pl.dawid0604.realestate.domain.PlotBuildingType;

import java.util.Set;

@Getter
@Schema(description = "Search plot advertisements request")
public final class SearchPlotAdvertisementsRequest extends SearchAdvertisementsRequest {

    @Getter(NONE)
    @Schema(description = "Building types")
    private Set<PlotBuildingType> types;

    public Set<PlotBuildingType> getTypes() {
        return Set.copyOf(types);
    }
}
