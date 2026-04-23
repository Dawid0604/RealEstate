/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import org.mapstruct.Mapper;

import pl.dawid0604.realestate.domain.Advertisement;

@Mapper(componentModel = "spring")
interface AdvertisementMapper {

    Advertisement toDomain(AdvertisementEntity entity);

    AdvertisementEntity toEntity(Advertisement advertisement);
}
