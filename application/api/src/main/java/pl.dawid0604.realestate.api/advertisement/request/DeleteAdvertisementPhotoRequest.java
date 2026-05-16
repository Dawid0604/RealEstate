package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidAdvertisementType;
import pl.dawid0604.realestate.api.validation.ValidPhotoId;
import pl.dawid0604.realestate.api.validation.ValidSlug;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;

import java.util.UUID;

@Schema(description = "Delete advertisement photo action")
public record DeleteAdvertisementPhotoRequest(
        @Schema(description = "Slug", example = "anyslug-123-qwe") @ValidSlug String slug,
        @Schema(description = "Advertisement type") @ValidAdvertisementType AdvertisementType type,
        @Schema(description = "Photo id", example = "019e2325-d92b-70ad-94e3-609123e34a79")
                @ValidPhotoId
                UUID photoId) {}
