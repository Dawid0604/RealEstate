/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementDetailsProjection;

@Repository
interface PlotAdvertisementJpaRepository extends JpaRepository<PlotAdvertisementEntity, UUID> {

    Optional<PlotAdvertisementEntity> findBySlug(String slug);

    @Query(
            """
                    SELECT
                        e.id as id,
                        e.slug as slug,
                        e.title as title,
                        e.description as description,
                        e.price as price,
                        e.area as area,
                        e.pricePerSquareMeter as pricePerSquareMeter,
                        e.localityId as localityId,
                        e.status as status,
                        e.userId as userId,
                        e.createdAt as createdAt,
                        e.isFeatured as featured,
                        e.plotType as plotType
                    FROM #{#entityName} e
                    WHERE e.slug = :slug
                """)
    Optional<PlotAdvertisementDetailsProjection> findDetailsBySlug(@Param("slug") String slug);
}
