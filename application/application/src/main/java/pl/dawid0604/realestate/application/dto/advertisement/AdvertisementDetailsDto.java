/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.advertisement;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public sealed interface AdvertisementDetailsDto
        permits CommercialAdvertisementDetailsDto,
                FlatAdvertisementDetailsDto,
                HouseAdvertisementDetailsDto,
                PlotAdvertisementDetailsDto {

    String slug();

    String title();

    String description();

    BigDecimal price();

    BigDecimal area();

    BigDecimal pricePerSquareMeter();

    String localityFullName();

    String status();

    Owner owner();

    Instant createdAt();

    boolean isFeatured();

    Set<AdvertisementPhotoDto> photos();

    Set<Claim> claims();

    record Owner(
            UUID id,
            String fullName,
            String avatarUrl,
            String type,
            String contactPhoneNumber,
            String contactEmail) {}

    record Claim(String claimKey, String claimValue) {}
}
