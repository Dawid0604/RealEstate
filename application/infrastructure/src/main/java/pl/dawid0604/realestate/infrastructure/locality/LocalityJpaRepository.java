/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.dawid0604.realestate.domain.shared.locality.projection.LocalityProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface LocalityJpaRepository extends JpaRepository<LocalityEntity, UUID> {

    boolean existsByName(String name);

    @Query("SELECT e.id as id, e.name as name FROM #{#entityName} e")
    List<LocalityProjection> findAllCustom();

    @Query("SELECT e.id as id, e.name as name FROM #{#entityName} e WHERE e.id = :id")
    Optional<LocalityProjection> findByIdCustom(@Param("id") UUID localityId);

    @Query("SELECT e.id as id, e.name as name FROM #{#entityName} e WHERE e.id IN :localityIds")
    List<LocalityProjection> findAllFullNamesByIdIn(Iterable<UUID> localityIds);
}
