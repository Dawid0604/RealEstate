/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.mapper.locality;

import org.mapstruct.Mapper;

import pl.dawid0604.realestate.application.dto.locality.LocalityDto;
import pl.dawid0604.realestate.domain.shared.locality.projection.LocalityProjection;

@Mapper(componentModel = "spring")
public interface LocalityMapper {

    LocalityDto toDto(LocalityProjection projection);
}
