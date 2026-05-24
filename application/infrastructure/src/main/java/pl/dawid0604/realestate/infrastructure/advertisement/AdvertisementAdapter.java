/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static org.apache.commons.lang3.StringUtils.isBlank;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.Page;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserAdvertisementCardProjection;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class AdvertisementAdapter implements AdvertisementRepository {
    private final AdvertisementMapper advertisementMapper;
    private final AdvertisementJpaRepository advertisementJpaRepository;

    @Override
    @Transactional
    public void save(final Advertisement advertisement) {
        Objects.requireNonNull(advertisement, "Advertisement cannot be null");

        final AdvertisementEntity<?, ?> entity =
                switch (advertisement.getAdvertisementType()) {
                    case FLAT -> advertisementMapper.toFlatEntity(advertisement);
                    case HOUSE -> advertisementMapper.toHouseEntity(advertisement);
                    case COMMERCIAL -> advertisementMapper.toCommercialEntity(advertisement);
                    case PLOT -> advertisementMapper.toPlotEntity(advertisement);
                };

        advertisementJpaRepository.save(entity);
    }

    @Override
    public void clearClaims(final Advertisement advertisement) {
        Objects.requireNonNull(advertisement, "Advertisement cannot be null");

        switch (advertisement.getAdvertisementType()) {
            case FLAT -> advertisementJpaRepository.clearFlatClaims(advertisement.getId());
            case HOUSE -> advertisementJpaRepository.clearHouseClaims(advertisement.getId());
            case PLOT -> advertisementJpaRepository.clearPlotClaims(advertisement.getId());
            case COMMERCIAL ->
                    advertisementJpaRepository.clearCommercialClaims(advertisement.getId());

            default -> throw new IllegalStateException("Unexpected type of advertisement type");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Advertisement> findBySlug(
            final String slug, final AdvertisementType advertisementType) {

        verifyValidSlug(slug);
        Objects.requireNonNull(advertisementType, "AdvertisementType cannot be null");

        return advertisementJpaRepository
                .findBySlug(slug, advertisementType)
                .map(advertisementMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdvertisementDetailsProjection> findDetails(
            final String slug, final AdvertisementType advertisementType) {

        verifyValidSlug(slug);
        Objects.requireNonNull(advertisementType, "AdvertisementType cannot be null");
        return advertisementJpaRepository.findDetails(slug, advertisementType);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<AdvertisementClaimProjection> findClaims(
            final UUID id, final AdvertisementType advertisementType) {

        Objects.requireNonNull(id, "Id cannot be null");
        Objects.requireNonNull(advertisementType, "AdvertisementType cannot be null");
        return advertisementJpaRepository.findClaims(id, advertisementType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAdvertisementCardProjection> findAdvertisementsByUser(
            final Set<AdvertisementStatus> statuses,
            final UUID userId,
            final int page,
            final int pageSize) {

        Objects.requireNonNull(userId, "UserId cannot be null");
        return asDomainPage(
                advertisementJpaRepository.findAdvertisementsByUser(
                        statuses, userId, page, pageSize));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdvertisementCardProjection> findByCriteria(
            final SearchAdvertisementsCriteria criteria) {

        Objects.requireNonNull(criteria, "Criteria cannot be null");
        return asDomainPage(advertisementJpaRepository.findByCriteria(criteria));
    }

    private static <T> Page<T> asDomainPage(final org.springframework.data.domain.Page<T> page) {
        return Page.of(
                page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    private static void verifyValidSlug(final String value) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Slug cannot be blank");
        }
    }
}
