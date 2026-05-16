package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidAdvertisementType;
import pl.dawid0604.realestate.api.validation.ValidSlug;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;

@Schema(description = "Advertisement deletion request")
public record DeleteAdvertisementRequest(
        @Schema(description = "Slug", example = "anyslug-123-qwe") @ValidSlug String slug,
        @Schema(description = "Advertisement type") @ValidAdvertisementType
                AdvertisementType type) {}
