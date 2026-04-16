/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;

import java.util.Set;

public interface PhotoRepository {

    Set<PhotoProjection> findFlatAdvertisementPhotos(String slug);

    Set<PhotoProjection> findHouseAdvertisementPhotos(String slug);

    Set<PhotoProjection> findCommercialAdvertisementPhotos(String slug);

    Set<PhotoProjection> findPlotAdvertisementPhotos(String slug);
}
