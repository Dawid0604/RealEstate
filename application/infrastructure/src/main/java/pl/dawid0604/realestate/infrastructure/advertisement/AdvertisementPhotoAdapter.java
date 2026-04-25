/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static java.util.stream.Collectors.groupingBy;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.dawid0604.realestate.domain.port.out.AdvertisementPhotoRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class AdvertisementPhotoAdapter implements AdvertisementPhotoRepository {
    private final FlatAdvertisementPhotoJpaRepository flatJpaRepository;
    private final HouseAdvertisementPhotoJpaRepository houseJpaRepository;
    private final CommercialAdvertisementPhotoJpaRepository commercialJpaRepository;
    private final PlotAdvertisementPhotoJpaRepository plotJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, Set<PhotoProjection>> findPhotosInBatch(
            final Iterable<UUID> ids, final AdvertisementType advertisementType) {

        final List<PhotoProjection> photos =
                switch (advertisementType) {
                    case FLAT -> flatJpaRepository.findPhotosByAdvertisementIdIn(ids);
                    case HOUSE -> houseJpaRepository.findPhotosByAdvertisementIdIn(ids);
                    case COMMERCIAL -> commercialJpaRepository.findPhotosByAdvertisementIdIn(ids);
                    case PLOT -> plotJpaRepository.findPhotosByAdvertisementIdIn(ids);
                };

        return photos.stream()
                .collect(groupingBy(PhotoProjection::getAdvertisementId, Collectors.toSet()));
    }
}
