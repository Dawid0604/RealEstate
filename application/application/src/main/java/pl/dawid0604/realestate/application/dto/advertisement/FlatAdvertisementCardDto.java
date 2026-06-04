/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.advertisement;

import static java.util.Collections.emptySet;

import java.math.BigDecimal;
import java.util.Set;

import pl.dawid0604.realestate.domain.UserType;

public record FlatAdvertisementCardDto(
        String slug,
        String title,
        BigDecimal price,
        BigDecimal area,
        BigDecimal pricePerSquareMeter,
        String localityFullName,
        String status,
        String createdAt,
        boolean isFeatured,
        Set<AdvertisementPhotoDto> photos,
        UserType userType,
        String buildingType,
        Integer numberOfRooms,
        Integer floor,
        Integer floors,
        Integer builtYear)
        implements AdvertisementCardDto {

    public FlatAdvertisementCardDto {
        photos = photos != null ? Set.copyOf(photos) : emptySet();
    }

    @Override
    public Set<AdvertisementPhotoDto> photos() {
        return Set.copyOf(photos);
    }
}
