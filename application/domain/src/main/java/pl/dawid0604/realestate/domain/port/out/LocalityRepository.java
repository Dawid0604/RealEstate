/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import pl.dawid0604.realestate.domain.Locality;
import pl.dawid0604.realestate.domain.shared.locality.projection.LocalityProjection;

public interface LocalityRepository {
    boolean existsById(UUID localityId);

    boolean existsByName(String name);

    Map<UUID, String> getFullNamesInBatch(Iterable<UUID> localityIds);

    List<LocalityProjection> findAll();

    Optional<LocalityProjection> findById(UUID localityId);

    void save(Locality locality);
}
