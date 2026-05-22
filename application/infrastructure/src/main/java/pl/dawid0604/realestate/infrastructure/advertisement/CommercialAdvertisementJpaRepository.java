/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementDetailsProjection;

import java.util.Optional;
import java.util.UUID;

@Repository
interface CommercialAdvertisementJpaRepository
        extends JpaRepository<CommercialAdvertisementEntity, UUID> {

    Optional<CommercialAdvertisementEntity> findBySlug(String slug);

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
                        e.buildingType as buildingType,
                        e.numberOfRooms as numberOfRooms,
                        e.floor as floor,
                        e.floors as floors,
                        e.builtYear as builtYear,
                        e.typeOfMarket as typeOfMarket
                    FROM #{#entityName} e
                    WHERE e.slug = :slug
                """)
    Optional<CommercialAdvertisementDetailsProjection> findDetailsBySlug(
            @Param("slug") String slug);
}
