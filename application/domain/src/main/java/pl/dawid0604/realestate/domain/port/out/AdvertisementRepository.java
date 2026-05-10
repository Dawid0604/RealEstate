/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.Page;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserAdvertisementCardProjection;

public interface AdvertisementRepository {
    void save(Advertisement advertisement);

    Optional<Advertisement> findBySlug(String slug, AdvertisementType advertisementType);

    Optional<AdvertisementDetailsProjection> findDetails(
            String slug, AdvertisementType advertisementType);

    Set<AdvertisementClaimProjection> findClaims(UUID id, AdvertisementType advertisementType);

    Page<UserAdvertisementCardProjection> findAdvertisementsByUser(
            Set<AdvertisementStatus> statuses, UUID userId, int page, int pageSize);

    Page<AdvertisementCardProjection> findByCriteria(SearchAdvertisementsCriteria criteria);
}
