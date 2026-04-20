package pl.dawid0604.realestate.infrastructure.advertisement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementDetailsProjection;

import java.util.Optional;
import java.util.UUID;

@Repository
interface PlotAdvertisementJpaRepository extends JpaRepository<PlotAdvertisementEntity, UUID> {

    Optional<PlotAdvertisementEntity> findBySlug(String slug);

    @Query(
            """
                        SELECT
                            e.id,
                            e.slug,
                            e.title,
                            e.description,
                            e.price,
                            e.area,
                            e.pricePerSquareMeter,
                            e.localityId,
                            e.status,
                            u.email,
                            e.createdAt,
                            e.featured,
                            e.plotType
                        FROM #{#entityName} e
                        JOIN e.user u
                        WHERE e.slug = :slug
                    """)
    Optional<AdvertisementDetailsProjection> findDetailsBySlug(String slug);
}
