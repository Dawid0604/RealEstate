/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.projection.advertisement;

import pl.dawid0604.realestate.domain.AdvertisementStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public sealed interface AdvertisementCardProjection
        permits CommercialAdvertisementCardProjection,
                FlatAdvertisementCardProjection,
                HouseAdvertisementCardProjection,
                PlotAdvertisementCardProjection {

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
