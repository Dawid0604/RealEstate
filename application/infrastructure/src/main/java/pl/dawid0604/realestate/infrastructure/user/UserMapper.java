/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.user;

import static lombok.AccessLevel.PACKAGE;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
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
                .avatar(new Url(entity.getAvatarUrl()))
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
                domain.getContactDetails().getEmail().map(Email::value).orElse(null),
                domain.getContactDetails().getPhoneNumber().map(PhoneNumber::value).orElse(null),
                domain.getAvatar().map(Url::value).orElse(null),
                domain.getRole(),
                domain.getStatus(),
                domain.getType(),
                domain.getLastLoginAt().orElse(null));
    }
}
