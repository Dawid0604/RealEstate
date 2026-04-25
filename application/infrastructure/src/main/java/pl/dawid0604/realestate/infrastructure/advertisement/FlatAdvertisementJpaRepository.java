/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementDetailsProjection;

import java.util.Optional;
import java.util.UUID;

@Repository
interface FlatAdvertisementJpaRepository extends JpaRepository<FlatAdvertisementEntity, UUID> {

    Optional<FlatAdvertisementEntity> findBySlug(String slug);

    @Query("SELECT e FROM #{#entityName} e WHERE e.slug = :slug")
    Optional<FlatAdvertisementDetailsProjection> findDetailsBySlug(@Param("slug") String slug);
}
