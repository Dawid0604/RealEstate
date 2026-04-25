/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementDetailsProjection;

import java.util.Optional;
import java.util.UUID;

@Repository
interface PlotAdvertisementJpaRepository extends JpaRepository<PlotAdvertisementEntity, UUID> {

    Optional<PlotAdvertisementEntity> findBySlug(String slug);

    @Query("SELECT e FROM #{#entityName} e WHERE e.slug = :slug")
    Optional<PlotAdvertisementDetailsProjection> findDetailsBySlug(String slug);
}
