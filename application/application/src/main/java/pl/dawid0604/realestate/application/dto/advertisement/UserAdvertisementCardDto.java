/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.advertisement;

import pl.dawid0604.realestate.domain.AdvertisementStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public sealed interface UserAdvertisementCardDto
        permits UserCommercialAdvertisementCardDto,
                UserFlatAdvertisementCardDto,
                UserHouseAdvertisementCardDto,
                UserPlotAdvertisementCardDto {

    String slug();

    String title();

    BigDecimal price();

    BigDecimal area();

    BigDecimal pricePerSquareMeter();

    String localityFullName();

    AdvertisementStatus status();

    Instant createdAt();

    boolean isFeatured();

    Set<AdvertisementPhotoDto> photos();
}
