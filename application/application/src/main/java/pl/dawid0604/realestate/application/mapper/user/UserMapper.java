/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.mapper.user;

import org.mapstruct.Mapper;

import pl.dawid0604.realestate.application.dto.user.UserProfileDto;
import pl.dawid0604.realestate.domain.shared.projection.user.UserProfileProjection;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserProfileDto toUserProfileDto(UserProfileProjection projection);
}
