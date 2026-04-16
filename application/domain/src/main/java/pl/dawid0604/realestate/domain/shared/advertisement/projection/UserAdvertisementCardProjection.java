/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement.projection;

import pl.dawid0604.realestate.domain.AdvertisementStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public sealed interface UserAdvertisementCardProjection
        permits UserCommercialAdvertisementCardProjection,
                UserFlatAdvertisementCardProjection,
                UserHouseAdvertisementCardProjection,
                UserPlotAdvertisementCardProjection {

    String getSlug();

    String getTitle();

    BigDecimal getPrice();

    BigDecimal getArea();

    BigDecimal getPricePerSquareMeter();

    BigDecimal getStatus();

    Instant getCreatedAt();

    UUID getLocalityId();

    boolean isFeatured();

    AdvertisementStatus status();

    String type();
}
