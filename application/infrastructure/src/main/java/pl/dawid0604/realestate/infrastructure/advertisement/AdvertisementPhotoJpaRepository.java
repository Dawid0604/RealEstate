/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;

@NoRepositoryBean
interface AdvertisementPhotoJpaRepository<T extends AdvertisementPhotoEntity<?>>
        extends JpaRepository<T, UUID> {

    @Query(
            """
                            SELECT p
                            FROM #{#entityName} p
                            JOIN FETCH p.advertisement a
                            WHERE a.id IN :advertisementIds
                    """)
    List<PhotoProjection> findPhotosByAdvertisementIdIn(
            @Param("advertisementIds") Iterable<UUID> advertisementIds);
}
