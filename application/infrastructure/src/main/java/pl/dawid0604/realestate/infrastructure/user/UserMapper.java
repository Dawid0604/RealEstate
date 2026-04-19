/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.user;

import org.mapstruct.Mapper;

import pl.dawid0604.realestate.domain.User;

@Mapper(componentModel = "spring")
interface UserMapper {

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
