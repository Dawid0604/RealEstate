/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement.projection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import pl.dawid0604.realestate.domain.AdvertisementStatus;

public sealed interface UserAdvertisementCardProjection
        permits UserCommercialAdvertisementCardProjection,
                UserFlatAdvertisementCardProjection,
                UserHouseAdvertisementCardProjection,
                UserPlotAdvertisementCardProjection {

    UUID getId();

    String getSlug();

    String getTitle();

    BigDecimal getPrice();

    BigDecimal getArea();

    BigDecimal getPricePerSquareMeter();

    String getStatus();

    Instant getCreatedAt();

    UUID getLocalityId();

    boolean isFeatured();

    AdvertisementStatus status();

    String type();
}
