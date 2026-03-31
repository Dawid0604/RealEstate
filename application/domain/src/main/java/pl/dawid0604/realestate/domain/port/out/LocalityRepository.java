package pl.dawid0604.realestate.domain.port.out;

import java.util.UUID;

public interface LocalityRepository {
    boolean existsById(UUID localityId);
}
