/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import java.util.Map;
import java.util.UUID;

public interface LocalityRepository {
    boolean existsById(UUID localityId);

    Map<UUID, String> getFullNamesInBatch(Iterable<UUID> localityIds);
}
