package pl.dawid0604.realestate.infrastructure.advertisement;

import org.mapstruct.Mapper;

import pl.dawid0604.realestate.domain.Advertisement;

@Mapper(componentModel = "spring")
interface AdvertisementMapper {

    Advertisement toDomain(FlatAdvertisementEntity entity);

    Advertisement toDomain(HouseAdvertisementEntity entity);

    Advertisement toDomain(CommercialAdvertisementEntity entity);

    Advertisement toDomain(PlotAdvertisementEntity entity);
}
