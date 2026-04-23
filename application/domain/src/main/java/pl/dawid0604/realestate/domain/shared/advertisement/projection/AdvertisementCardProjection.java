/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement.projection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import pl.dawid0604.realestate.domain.AdvertisementStatus;

public sealed interface AdvertisementCardProjection
        permits CommercialAdvertisementCardProjection,
                FlatAdvertisementCardProjection,
                HouseAdvertisementCardProjection,
                PlotAdvertisementCardProjection {

    UUID getId();

    String getSlug();

    String getTitle();

    BigDecimal getPrice();

    BigDecimal getArea();

    BigDecimal getPricePerSquareMeter();

    AdvertisementStatus getStatus();

    Instant getCreatedAt();

    UUID getLocalityId();

    // TODO: to consider
    UUID getUserId();

    boolean isFeatured();
}
