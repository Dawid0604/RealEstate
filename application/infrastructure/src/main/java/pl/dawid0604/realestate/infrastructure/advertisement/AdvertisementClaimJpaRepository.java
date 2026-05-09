/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;

@NoRepositoryBean
sealed interface AdvertisementClaimJpaRepository<T extends AdvertisementClaimEntity<?>>
        extends JpaRepository<T, UUID>
        permits CommercialAdvertisementClaimJpaRepository,
                FlatAdvertisementClaimJpaRepository,
                HouseAdvertisementClaimJpaRepository,
                PlotAdvertisementClaimJpaRepository {

    @Query(
            """
                    SELECT e
                    FROM #{#entityName} e
                    JOIN e.advertisement a
                    WHERE a.id = :id
                """)
    Set<AdvertisementClaimProjection> findClaimsById(@Param("id") UUID advertisementId);
}
