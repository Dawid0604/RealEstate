/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import pl.dawid0604.realestate.application.dto.user.UserProfileDto;
import pl.dawid0604.realestate.domain.shared.projection.user.UserProfileProjection;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", source = "projection.userId")
    @Mapping(target = "email", source = "projection.email")
    @Mapping(target = "contactPhoneNumber", source = "projection.contactPhoneNumber")
    @Mapping(target = "contactEmail", source = "projection.contactEmail")
    @Mapping(target = "role", source = "projection.role")
    @Mapping(target = "type", source = "projection.type")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "fullName", source = "projection", qualifiedByName = "toFullName")
    @Mapping(target = "avatarUrl", source = "projection.avatarUrl")
    UserProfileDto toUserProfileDto(UserProfileProjection projection);

    @Named("toFullName")
    @SuppressWarnings("unused")
    default String toFullName(final UserProfileProjection projection) {
        if (projection == null) {
            return null;
        }

        return projection.getFirstName() + " " + projection.getLastName();
    }
}
