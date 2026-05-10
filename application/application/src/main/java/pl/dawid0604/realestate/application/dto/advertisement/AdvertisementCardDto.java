/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.advertisement;

import pl.dawid0604.realestate.domain.UserType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public sealed interface AdvertisementCardDto
        permits CommercialAdvertisementCardDto,
                FlatAdvertisementCardDto,
                HouseAdvertisementCardDto,
                PlotAdvertisementCardDto {

    String slug();

    String title();

    BigDecimal price();

    BigDecimal area();

    BigDecimal pricePerSquareMeter();

    String localityFullName();

    UserType userType();

    Instant createdAt();

    boolean isFeatured();

    Set<AdvertisementPhotoDto> photos();
}
