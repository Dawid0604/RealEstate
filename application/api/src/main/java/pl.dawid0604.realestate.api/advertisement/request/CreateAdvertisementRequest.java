/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import static lombok.AccessLevel.PROTECTED;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.NoArgsConstructor;

import pl.dawid0604.realestate.api.validation.ValidArea;
import pl.dawid0604.realestate.api.validation.ValidEmail;
import pl.dawid0604.realestate.api.validation.ValidLocalityId;
import pl.dawid0604.realestate.api.validation.ValidPrice;
import pl.dawid0604.realestate.api.validation.ValidTitle;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = PROTECTED)
abstract sealed class CreateAdvertisementRequest
        permits CreateCommercialAdvertisementRequest,
                CreateFlatAdvertisementRequest,
                CreateHouseAdvertisementRequest,
                CreatePlotAdvertisementRequest {

    @ValidTitle
    @Schema(description = "Advertisement title", example = "Any exampled title")
    private String title;

    @ValidTitle
    @Schema(description = "Advertisement description", example = "Any exampled description")
    private String description;

    @ValidPrice
    @Schema(description = "Advertisement price", example = "250000")
    private BigDecimal price;

    @Schema(
            description = "Advertisement locality",
            example = "019e2325-d92b-70ad-94e3-609123e34a79")
    @ValidLocalityId
    private UUID localityId;

    @ValidEmail
    @Schema(description = "User email", example = "anyMail@mail.com")
    private String userEmail;

    @Schema(description = "Advertisement photos")
    private Set<AdvertisementPhotoRequest> photos;

    @Schema(description = "Advertisement area", example = "100.25")
    @ValidArea
    private BigDecimal area;

    @Schema(description = "Advertisement remaining data")
    private Map<String, String> claims;

    @Schema(description = "Advertisement featured state", example = "true")
    private Boolean featured;
}
