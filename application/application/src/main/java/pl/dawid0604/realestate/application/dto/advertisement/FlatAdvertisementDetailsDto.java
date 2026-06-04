/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.advertisement;

import static java.util.Collections.emptySet;

import java.math.BigDecimal;
import java.util.Set;

public record FlatAdvertisementDetailsDto(
        String slug,
        String title,
        String description,
        BigDecimal price,
        BigDecimal area,
        BigDecimal pricePerSquareMeter,
        String localityFullName,
        String status,
        Owner owner,
        String createdAt,
        boolean isFeatured,
        Set<AdvertisementPhotoDto> photos,
        Set<Claim> claims,
        String buildingType,
        Integer numberOfRooms,
        Integer floor,
        Integer floors,
        Integer builtYear)
        implements AdvertisementDetailsDto {

    public FlatAdvertisementDetailsDto {
        photos = photos != null ? Set.copyOf(photos) : emptySet();
        claims = claims != null ? Set.copyOf(claims) : emptySet();
    }

    @Override
    public Set<AdvertisementPhotoDto> photos() {
        return Set.copyOf(photos);
    }

    @Override
    public Set<Claim> claims() {
        return Set.copyOf(claims);
    }
}
