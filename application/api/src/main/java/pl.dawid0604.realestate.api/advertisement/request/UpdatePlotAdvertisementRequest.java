/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

import pl.dawid0604.realestate.api.validation.ValidBuildingType;
import pl.dawid0604.realestate.domain.PlotBuildingType;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Getter
@Schema(description = "Update plot advertisement action")
public final class UpdatePlotAdvertisementRequest extends UpdateAdvertisementRequest {

    @ValidBuildingType
    @Schema(description = "Advertisement building type")
    private final PlotBuildingType plotType;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public UpdatePlotAdvertisementRequest(
            final String slug,
            final String title,
            final String description,
            final BigDecimal price,
            final UUID localityId,
            final String userEmail,
            final BigDecimal area,
            final Map<String, String> claims,
            final Boolean featured,
            final PlotBuildingType plotType) {

        super(slug, title, description, price, localityId, userEmail, area, claims, featured);
        this.plotType = plotType;
    }
}
