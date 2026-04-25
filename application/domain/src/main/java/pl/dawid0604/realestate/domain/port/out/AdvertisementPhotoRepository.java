/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;

public interface AdvertisementPhotoRepository {

    Map<UUID, Set<PhotoProjection>> findPhotosInBatch(
            Iterable<UUID> ids, AdvertisementType advertisementType);
}
