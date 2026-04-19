/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.photo;

import static lombok.AccessLevel.PACKAGE;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.realestate.domain.port.out.PhotoRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class PhotoAdapter implements PhotoRepository {
    private final PhotoJpaRepository repository;

    @Override
    public Map<UUID, Set<PhotoProjection>> findAdvertisementsPhotosInBatch(
            final Iterable<UUID> ids, final AdvertisementType advertisementType) {

        return Map.of();
    }
}
