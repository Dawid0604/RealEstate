/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.Page;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchCommercialAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchFlatAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchHouseAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchPlotAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserAdvertisementCardProjection;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class AdvertisementAdapter implements AdvertisementRepository {
    private final FlatAdvertisementJpaRepository flatJpaRepository;
    private final HouseAdvertisementJpaRepository houseJpaRepository;
    private final CommercialAdvertisementJpaRepository commercialJpaRepository;
    private final PlotAdvertisementJpaRepository plotJpaRepository;
    private final AdvertisementMapper advertisementMapper;

    @Override
    @Transactional
    public void save(final Advertisement advertisement) {}

    @Override
    @Transactional(readOnly = true)
    public Optional<Advertisement> findBySlug(
            final String slug, final AdvertisementType advertisementType) {

        return switch (advertisementType) {
            case FLAT -> flatJpaRepository.findBySlug(slug).map(advertisementMapper::toDomain);

            case HOUSE -> houseJpaRepository.findBySlug(slug).map(advertisementMapper::toDomain);

            case COMMERCIAL ->
                    commercialJpaRepository.findBySlug(slug).map(advertisementMapper::toDomain);

            case PLOT -> plotJpaRepository.findBySlug(slug).map(advertisementMapper::toDomain);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdvertisementDetailsProjection> findDetails(
            final String slug, final AdvertisementType advertisementType) {

        return switch (advertisementType) {
            case FLAT -> flatJpaRepository.findDetailsBySlug(slug);
            case HOUSE -> houseJpaRepository.findDetailsBySlug(slug);
            case COMMERCIAL -> commercialJpaRepository.findDetailsBySlug(slug);
            case PLOT -> plotJpaRepository.findDetailsBySlug(slug);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public Set<AdvertisementClaimProjection> findClaims(
            final UUID id, final AdvertisementType advertisementType) {

        return Set.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAdvertisementCardProjection> findAdvertisementsByUser(
            final Set<AdvertisementStatus> statuses,
            final String email,
            final int page,
            final int pageSize) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdvertisementCardProjection> findByCriteria(
            final SearchAdvertisementsCriteria criteria) {

        return switch (criteria) {
            case SearchCommercialAdvertisementsCriteria commercialCriteria -> null;
            case SearchFlatAdvertisementsCriteria flatCriteria -> null;
            case SearchHouseAdvertisementsCriteria houseCriteria -> null;
            case SearchPlotAdvertisementsCriteria plotCriteria -> null;
        };
    }
}
