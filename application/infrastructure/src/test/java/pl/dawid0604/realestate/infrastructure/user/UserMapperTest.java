/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.user;

import static org.mockito.Mockito.spy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.domain.ContactDetails;
import pl.dawid0604.realestate.domain.Email;
import pl.dawid0604.realestate.domain.FullName;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.Password;
import pl.dawid0604.realestate.domain.PhoneNumber;
import pl.dawid0604.realestate.domain.Url;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.UserType;

import java.time.Instant;

class UserMapperTest {
    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapper();
    }

    @Nested
    final class ToDomainTests {

        @Test
        @DisplayName("Should return null when entity is null")
        void shouldReturnNullWhenEntityIsNull() {
            // Given
            // When
            final User domain = mapper.toDomain(null);

            // Then
            Assertions.assertThat(domain).isNull();
        }

        @Test
        @ExtendWith(MockitoExtension.class)
        @DisplayName("Should map to domain successfully")
        void shouldMapToDomainSuccessfully() {
            // Given
            final Instant createdAt = Instant.now().minusMillis(125_000);
            final UserEntity entity =
                    spy(
                            new UserEntity(
                                    Identifier.generate().getValue(),
                                    "anyEmail@mail.com",
                                    "$anyHashedPassword",
                                    "firstName",
                                    "lastName",
                                    "notificationEmail@mail.com",
                                    "123456789",
                                    "https://anyUrl",
                                    UserRole.ROLE_USER,
                                    UserStatus.ACTIVE,
                                    UserType.AGENCY,
                                    Instant.now().minusMillis(35_000)));

            BDDMockito.given(entity.getCreatedAt()).willReturn(createdAt);

            // When
            final User domain = mapper.toDomain(entity);

            // Then
            Assertions.assertThat(domain)
                    .returns(entity.getId(), u -> u.getId().getValue())
                    .returns(entity.getEmail().toLowerCase(), u -> u.getEmail().value())
                    .returns(entity.getPassword(), u -> u.getPassword().getValue())
                    .returns(
                            entity.getAvatarUrl(),
                            u -> u.getAvatar() != null ? u.getAvatar().value() : null)
                    .returns(entity.getRole(), User::getRole)
                    .returns(entity.getStatus(), User::getStatus)
                    .returns(entity.getType(), User::getType)
                    .returns(entity.getCreatedAt(), User::getCreatedAt)
                    .returns(entity.getLastLoginAt(), u -> u.getLastLoginAt().orElse(null))
                    .returns(
                            entity.getNotificationEmail().toLowerCase(),
                            u ->
                                    u.getContactDetails().email() != null
                                            ? u.getContactDetails().email().value()
                                            : null)
                    .returns(
                            entity.getNotificationPhoneNumber(),
                            u ->
                                    u.getContactDetails().phoneNumber() != null
                                            ? u.getContactDetails().phoneNumber().value()
                                            : null)
                    .returns(entity.getFirstName(), u -> u.getFullName().firstName())
                    .returns(entity.getLastName(), u -> u.getFullName().lastName());
        }
    }

    @Nested
    final class ToEntityTests {

        @Test
        @DisplayName("Should return null when domain is null")
        void shouldReturnNullWhenDomainIsNull() {
            // Given
            // When
            final UserEntity entity = mapper.toEntity(null);

            // Then
            Assertions.assertThat(entity).isNull();
        }

        @Test
        @ExtendWith(MockitoExtension.class)
        @DisplayName("Should map to entity successfully")
        void shouldMapToEntitySuccessfully() {
            // Given
            final User domain =
                    User.reconstitute()
                            .id(Identifier.generate())
                            .email(new Email("anyMail.@mail.com"))
                            .password(Password.ofHashed("$abcde"))
                            .fullName(new FullName("abc", "cde"))
                            .contactDetails(new ContactDetails(null, new PhoneNumber("123456789")))
                            .avatar(new Url("https://abc"))
                            .role(UserRole.ROLE_USER)
                            .status(UserStatus.ACTIVE)
                            .createdAt(Instant.now().minusNanos(135_000))
                            .lastLoginAt(Instant.now().minusNanos(15_000))
                            .type(UserType.AGENCY)
                            .build();

            // When
            final UserEntity entity = mapper.toEntity(domain);

            // Then
            Assertions.assertThat(entity)
                    .returns(domain.getId().getValue(), UserEntity::getId)
                    .returns(domain.getEmail().value(), UserEntity::getEmail)
                    .returns(domain.getPassword().getValue(), UserEntity::getPassword)
                    .returns(domain.getFullName().firstName(), UserEntity::getFirstName)
                    .returns(domain.getFullName().lastName(), UserEntity::getLastName)
                    .returns(
                            domain.getContactDetails().email() != null
                                    ? domain.getContactDetails().email().value()
                                    : null,
                            UserEntity::getNotificationEmail)
                    .returns(
                            domain.getContactDetails().phoneNumber() != null
                                    ? domain.getContactDetails().phoneNumber().value()
                                    : null,
                            UserEntity::getNotificationPhoneNumber)
                    .returns(domain.getLastLoginAt().orElse(null), UserEntity::getLastLoginAt)
                    .returns(
                            domain.getAvatar() != null ? domain.getAvatar().value() : null,
                            UserEntity::getAvatarUrl)
                    .returns(domain.getRole(), UserEntity::getRole)
                    .returns(domain.getStatus(), UserEntity::getStatus)
                    .returns(domain.getType(), UserEntity::getType);
        }
    }
}
