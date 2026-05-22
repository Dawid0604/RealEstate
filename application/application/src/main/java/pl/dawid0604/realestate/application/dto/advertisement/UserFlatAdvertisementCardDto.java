/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.advertisement;

import static java.util.Collections.emptySet;

import java.math.BigDecimal;
import java.util.Set;

import pl.dawid0604.realestate.domain.AdvertisementStatus;

public record UserFlatAdvertisementCardDto(
        String slug,
        String title,
        BigDecimal price,
        BigDecimal area,
        BigDecimal pricePerSquareMeter,
        String localityFullName,
        AdvertisementStatus status,
        String createdAt,
        boolean isFeatured,
        Set<AdvertisementPhotoDto> photos,
        String buildingType,
        Integer numberOfRooms,
        Integer floor,
        Integer floors,
        Integer builtYear)
        implements UserAdvertisementCardDto {

    public UserFlatAdvertisementCardDto {
        photos = photos != null ? Set.copyOf(photos) : emptySet();
    }

    @Override
    public Set<AdvertisementPhotoDto> photos() {
        return Set.copyOf(photos);
    }
}
