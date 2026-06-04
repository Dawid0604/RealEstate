/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.advertisement;

import java.math.BigDecimal;
import java.util.Set;

import pl.dawid0604.realestate.domain.UserType;

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

    String createdAt();

    boolean isFeatured();

    Set<AdvertisementPhotoDto> photos();
}
