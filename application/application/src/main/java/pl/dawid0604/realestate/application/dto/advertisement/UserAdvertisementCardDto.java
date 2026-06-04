/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.advertisement;

import java.math.BigDecimal;
import java.util.Set;

import pl.dawid0604.realestate.domain.AdvertisementStatus;

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

    String createdAt();

    boolean isFeatured();

    Set<AdvertisementPhotoDto> photos();
}
