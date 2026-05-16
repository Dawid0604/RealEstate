package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidPhotoPosition;
import pl.dawid0604.realestate.api.validation.ValidUrl;

public record AdvertisementPhotoRequest(
        @ValidUrl @Schema(description = "Photo url", example = "https://anyPhoto.com/1") String url,
        @ValidPhotoPosition @Schema(description = "Photo position", example = "1")
                Integer position) {}
