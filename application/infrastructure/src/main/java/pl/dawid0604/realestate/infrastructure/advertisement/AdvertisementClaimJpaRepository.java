package pl.dawid0604.realestate.infrastructure.advertisement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;

import java.util.Set;
import java.util.UUID;

@NoRepositoryBean
interface AdvertisementClaimJpaRepository<T extends AdvertisementClaimEntity>
        extends JpaRepository<T, UUID> {

    @Query(
            """
                    SELECT e.claimKey, e.claimValue
                    FROM #{#entityName} e
                    JOIN e.advertisement a
                    WHERE a.id = :id
                """)
    Set<AdvertisementClaimProjection> findClaimsById(@Param("id") UUID advertisementId);
}
