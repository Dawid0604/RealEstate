/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static lombok.AccessLevel.PACKAGE;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor(access = PACKAGE)
class AdvertisementAdapter implements AdvertisementRepository {

    @Override
    public void save(final Advertisement advertisement) {}

    @Override
    public Optional<Advertisement> findBySlug(final String slug) {
        return Optional.empty();
    }

    @Override
    public Optional<AdvertisementDetailsProjection> findDetails(
            final String slug, final AdvertisementType advertisementType) {
        return Optional.empty();
    }

    @Override
    public Set<AdvertisementClaimProjection> findClaims(
            final UUID id, final AdvertisementType advertisementType) {
        return Set.of();
    }

    @Override
    public Page<UserAdvertisementCardProjection> findAdvertisementsByUser(
            final Set<AdvertisementStatus> statuses,
            final String email,
            final int page,
            final int pageSize) {
        return null;
    }

    @Override
    public Page<AdvertisementCardProjection> findByCriteria(
            final SearchAdvertisementsCriteria criteria) {
        return null;
    }
}
