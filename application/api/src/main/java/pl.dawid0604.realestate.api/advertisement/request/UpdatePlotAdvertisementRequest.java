package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidArea;
import pl.dawid0604.realestate.api.validation.ValidBuildingType;
import pl.dawid0604.realestate.api.validation.ValidEmail;
import pl.dawid0604.realestate.api.validation.ValidLocalityId;
import pl.dawid0604.realestate.api.validation.ValidPrice;
import pl.dawid0604.realestate.api.validation.ValidSlug;
import pl.dawid0604.realestate.api.validation.ValidTitle;
import pl.dawid0604.realestate.domain.PlotBuildingType;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Update house advertisement action")
public record UpdatePlotAdvertisementRequest(
        @Schema(description = "Slug", example = "anyslug-123-qwe") @ValidSlug String slug,
        @Schema(description = "Advertisement title", example = "Any exampled title") @ValidTitle
                String title,
        @Schema(description = "Advertisement description", example = "Any exampled description")
                @ValidTitle
                String description,
        @Schema(description = "Advertisement price", example = "250000") @ValidPrice
                BigDecimal price,
        @Schema(
                        description = "Advertisement locality",
                        example = "019e2325-d92b-70ad-94e3-609123e34a79")
                @ValidLocalityId
                UUID localityId,
        @Schema(description = "User email", example = "anyMail@mail.com") @ValidEmail
                String userEmail,
        @Schema(description = "Advertisement photos") Set<AdvertisementPhotoRequest> photos,
        @Schema(description = "Advertisement building type") @ValidBuildingType
                PlotBuildingType buildingType,
        @Schema(description = "Advertisement area", example = "100.25") @ValidArea BigDecimal area,
        @Schema(description = "Advertisement remaining data") Map<String, String> claims,
        @Schema(description = "Advertisement featured state", example = "true") Boolean featured) {}
