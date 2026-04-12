/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import java.util.Optional;
import java.util.UUID;

public interface LocalityRepository {
    boolean existsById(UUID localityId);

    Optional<String> getFullName(UUID localityId);
}
