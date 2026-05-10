/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import org.springframework.stereotype.Repository;

@Repository
non-sealed interface CommercialAdvertisementClaimJpaRepository
        extends AdvertisementClaimJpaRepository<CommercialAdvertisementClaimEntity> {}
