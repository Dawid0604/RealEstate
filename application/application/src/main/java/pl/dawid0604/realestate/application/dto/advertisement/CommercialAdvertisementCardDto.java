/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.advertisement;

import static java.util.Collections.emptySet;

import pl.dawid0604.realestate.domain.UserType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record CommercialAdvertisementCardDto(
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
        UserType userType,
        String buildingType,
        Integer numberOfRooms,
        Integer floor,
        Integer floors,
        Integer builtYear)
        implements AdvertisementCardDto {

    public CommercialAdvertisementCardDto {
        photos = photos != null ? Set.copyOf(photos) : emptySet();
    }

    @Override
    public Set<AdvertisementPhotoDto> photos() {
        return Set.copyOf(photos);
    }
}
