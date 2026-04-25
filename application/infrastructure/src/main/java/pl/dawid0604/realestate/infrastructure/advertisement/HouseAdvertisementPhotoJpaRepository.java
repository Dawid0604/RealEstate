/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import org.springframework.stereotype.Repository;

@Repository
interface HouseAdvertisementPhotoJpaRepository
        extends AdvertisementPhotoJpaRepository<HouseAdvertisementPhotoEntity> {}
