/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.shared.Page;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.CommercialAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.FlatAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.HouseAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.PlotAdvertisementDetailsProjection;

import java.util.Optional;
import java.util.Set;

public interface AdvertisementRepository {
    void save(Advertisement advertisement);

    Optional<Advertisement> findBySlug(String slug);

    Optional<FlatAdvertisementDetailsProjection> findFlatDetails(String slug);

    Optional<HouseAdvertisementDetailsProjection> findHouseDetails(String slug);

    Optional<CommercialAdvertisementDetailsProjection> findCommercialDetails(String slug);

    Optional<PlotAdvertisementDetailsProjection> findPlotDetails(String slug);

    Set<AdvertisementClaimProjection> findFlatClaims(String slug);

    Set<AdvertisementClaimProjection> findHouseClaims(String slug);

    Set<AdvertisementClaimProjection> findCommercialClaims(String slug);

    Set<AdvertisementClaimProjection> findPlotClaims(String slug);

    Page<AdvertisementCardProjection> findAdvertisementsByUser(
            Set<AdvertisementStatus> statuses, String email, int page, int pageSize);
}
