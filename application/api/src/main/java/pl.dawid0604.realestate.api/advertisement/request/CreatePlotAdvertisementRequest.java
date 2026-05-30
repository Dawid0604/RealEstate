/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

import pl.dawid0604.realestate.api.validation.ValidBuildingType;
import pl.dawid0604.realestate.domain.PlotBuildingType;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Schema(description = "Create plot advertisement action")
public final class CreatePlotAdvertisementRequest extends CreateAdvertisementRequest {

    @ValidBuildingType
    @Schema(description = "Advertisement plot type")
    private final PlotBuildingType buildingType;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public CreatePlotAdvertisementRequest(
            final String title,
            final String description,
            final BigDecimal price,
            final UUID localityId,
            final String userEmail,
            final Set<AdvertisementPhotoRequest> photos,
            final BigDecimal area,
            final Map<String, String> claims,
            final Boolean featured,
            final PlotBuildingType buildingType) {

        super(title, description, price, localityId, userEmail, photos, area, claims, featured);
        this.buildingType = buildingType;
    }
}
