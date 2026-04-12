/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.advertisement;

import static java.util.Collections.emptySet;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record UserPlotAdvertisementCardDto(
        String slug,
        String title,
        BigDecimal price,
        BigDecimal area,
        BigDecimal pricePerSquareMeter,
        String localityFullName,
        String status,
        Instant createdAt,
        boolean isFeatured,
        Set<AdvertisementPhotoDto> photos,
        String plotType)
        implements UserAdvertisementCardDto {

    public UserPlotAdvertisementCardDto {
        photos = photos != null ? Set.copyOf(photos) : emptySet();
    }

    @Override
    public Set<AdvertisementPhotoDto> photos() {
        return Set.copyOf(photos);
    }
}
