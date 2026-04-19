/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface LocalityJpaRepository extends JpaRepository<LocalityEntity, UUID> {

    interface FullNameProjection {
        UUID getId();

        String getFullName();
    }

    @Query(
            nativeQuery = true,
            value =
                    """
                        SELECT e.id as id, e.full_name as full_name
                        FROM localities e
                        WHERE e.id ANY(:localityIds)
                    """)
    List<FullNameProjection> findAllFullNamesByIdIn(Iterable<UUID> localityIds);
}
