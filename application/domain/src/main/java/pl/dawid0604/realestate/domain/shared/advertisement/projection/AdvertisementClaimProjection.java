/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement.projection;

import java.util.UUID;

public interface AdvertisementClaimProjection {
    UUID getId();

    String getClaimKey();

    String getClaimValue();
}
