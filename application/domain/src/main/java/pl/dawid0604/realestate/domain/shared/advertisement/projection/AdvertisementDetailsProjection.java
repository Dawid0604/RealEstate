/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement.projection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public sealed interface AdvertisementDetailsProjection
        permits CommercialAdvertisementDetailsProjection,
                FlatAdvertisementDetailsProjection,
                HouseAdvertisementDetailsProjection,
                PlotAdvertisementDetailsProjection {

    String getSlug();

    String getTitle();

    String getDescription();

    BigDecimal getPrice();

    BigDecimal getArea();

    BigDecimal getPricePerSquareMeter();

    UUID getLocalityId();

    String getStatus();

    String getOwnerEmail();

    Instant getCreatedAt();

    boolean isFeatured();
}
