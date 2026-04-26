/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
interface LocalityJpaRepository extends JpaRepository<LocalityEntity, UUID> {

    interface FullNameProjection {
        UUID getId();

        String getName();
    }

    @Query("SELECT e FROM #{#entityName} e WHERE e.id IN :localityIds")
    List<FullNameProjection> findAllFullNamesByIdIn(Iterable<UUID> localityIds);
}
