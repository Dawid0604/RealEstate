/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidAdvertisementType;
import pl.dawid0604.realestate.api.validation.ValidSlug;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;

@Schema(description = "Advertisement sold request")
public record SetAsSoldAdvertisementRequest(
        @Schema(description = "Slug", example = "anyslug-123-qwe") @ValidSlug String slug,
        @Schema(description = "Advertisement type") @ValidAdvertisementType
                AdvertisementType type) {}
