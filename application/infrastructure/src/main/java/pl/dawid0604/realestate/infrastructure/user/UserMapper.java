/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.user;

import static lombok.AccessLevel.PACKAGE;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.domain.ContactDetails;
import pl.dawid0604.realestate.domain.Email;
import pl.dawid0604.realestate.domain.FullName;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.Password;
import pl.dawid0604.realestate.domain.PhoneNumber;
import pl.dawid0604.realestate.domain.Url;
import pl.dawid0604.realestate.domain.User;

@Component
@NoArgsConstructor(access = PACKAGE)
class UserMapper {

    User toDomain(final UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.reconstitute()
                .id(Identifier.of(entity.getId()))
                .email(new Email(entity.getEmail()))
                .password(Password.ofHashed(entity.getPassword()))
                .fullName(new FullName(entity.getFirstName(), entity.getLastName()))
                .contactDetails(
                        new ContactDetails(
                                new Email(entity.getNotificationEmail()),
                                new PhoneNumber(entity.getNotificationPhoneNumber())))
                .avatar(isNotBlank(entity.getAvatarUrl()) ? new Url(entity.getAvatarUrl()) : null)
                .role(entity.getRole())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .type(entity.getType())
                .lastLoginAt(entity.getLastLoginAt())
                .build();
    }

    UserEntity toEntity(final User domain) {
        if (domain == null) {
            return null;
        }

        return new UserEntity(
                domain.getId().getValue(),
                domain.getEmail().value(),
                domain.getPassword().getValue(),
                domain.getFullName().firstName(),
                domain.getFullName().lastName(),
                domain.getContactDetails().email() != null
                        ? domain.getContactDetails().email().value()
                        : null,
                domain.getContactDetails().phoneNumber() != null
                        ? domain.getContactDetails().phoneNumber().value()
                        : null,
                domain.getAvatar() != null ? domain.getAvatar().value() : null,
                domain.getRole(),
                domain.getStatus(),
                domain.getType(),
                domain.getLastLoginAt().orElse(null));
    }
}
