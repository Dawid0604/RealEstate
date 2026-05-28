/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

import static java.util.Collections.emptyMap;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;

import pl.dawid0604.realestate.api.validation.ValidArea;
import pl.dawid0604.realestate.api.validation.ValidEmail;
import pl.dawid0604.realestate.api.validation.ValidLocalityId;
import pl.dawid0604.realestate.api.validation.ValidPrice;
import pl.dawid0604.realestate.api.validation.ValidSlug;
import pl.dawid0604.realestate.api.validation.ValidTitle;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = PROTECTED)
abstract sealed class UpdateAdvertisementRequest
        permits UpdateCommercialAdvertisementRequest,
                UpdateFlatAdvertisementRequest,
                UpdateHouseAdvertisementRequest,
                UpdatePlotAdvertisementRequest {

    @ValidSlug
    @Schema(description = "Slug", example = "anyslug-123-qwe")
    private final String slug;

    @ValidTitle
    @Schema(description = "Advertisement title", example = "Any exampled title")
    private final String title;

    @ValidTitle
    @Schema(description = "Advertisement description", example = "Any exampled description")
    private final String description;

    @ValidPrice
    @Schema(description = "Advertisement price", example = "250000")
    private final BigDecimal price;

    @Schema(
            description = "Advertisement locality",
            example = "019e2325-d92b-70ad-94e3-609123e34a79")
    @ValidLocalityId
    private final UUID localityId;

    @ValidEmail
    @Schema(description = "User email", example = "anyMail@mail.com")
    private final String userEmail;

    @ValidArea
    @Schema(description = "Advertisement area", example = "100.25")
    private final BigDecimal area;

    @Getter(NONE)
    @Schema(description = "Advertisement remaining data")
    private final Map<String, String> claims;

    @Schema(description = "Advertisement featured state", example = "true")
    private final Boolean featured;

    public Map<String, String> getClaims() {
        return claims != null ? Map.copyOf(claims) : emptyMap();
    }
}
