/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static lombok.AccessLevel.PACKAGE;

import jakarta.annotation.Nonnull;

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
    public void save(@Nonnull final Advertisement advertisement) {
        advertisementJpaRepository.save(advertisementMapper.toEntity(advertisement));
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public Optional<Advertisement> findBySlug(
            @Nonnull final String slug, @Nonnull final AdvertisementType advertisementType) {

        return advertisementJpaRepository
                .findBySlug(slug, advertisementType)
                .map(advertisementMapper::toDomain);
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public Optional<AdvertisementDetailsProjection> findDetails(
            @Nonnull final String slug, @Nonnull final AdvertisementType advertisementType) {

        return advertisementJpaRepository.findDetails(slug, advertisementType);
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public Set<AdvertisementClaimProjection> findClaims(
            @Nonnull final UUID id, @Nonnull final AdvertisementType advertisementType) {

        return advertisementJpaRepository.findClaims(id, advertisementType);
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public Page<UserAdvertisementCardProjection> findAdvertisementsByUser(
            @Nonnull final Set<AdvertisementStatus> statuses,
            @Nonnull final String email,
            final int page,
            final int pageSize) {

        return asDomainPage(
                advertisementJpaRepository.findAdvertisementsByUser(
                        statuses, email, page, pageSize));
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public Page<AdvertisementCardProjection> findByCriteria(
            @Nonnull final SearchAdvertisementsCriteria criteria) {

        return asDomainPage(advertisementJpaRepository.findByCriteria(criteria));
    }

    @Nonnull
    private static <T> Page<T> asDomainPage(
            @Nonnull final org.springframework.data.domain.Page<T> page) {

        return Page.of(
                page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
