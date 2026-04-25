/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementDetailsProjection;

import java.util.Optional;
import java.util.UUID;

@Repository
interface HouseAdvertisementJpaRepository extends JpaRepository<HouseAdvertisementEntity, UUID> {

    Optional<HouseAdvertisementEntity> findBySlug(String slug);

    @Query("SELECT e FROM #{#entityName} e WHERE e.slug = :slug")
    Optional<HouseAdvertisementDetailsProjection> findDetailsBySlug(String slug);
}
