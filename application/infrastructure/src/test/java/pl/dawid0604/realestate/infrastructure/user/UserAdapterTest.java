/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.user;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;
import pl.dawid0604.realestate.domain.shared.user.projection.AdvertisementUserProjection;
import pl.dawid0604.realestate.domain.shared.user.projection.UserProfileProjection;
import pl.dawid0604.realestate.infrastructure.ClearDatabase;
import pl.dawid0604.realestate.infrastructure.IntegrationTest;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class UserAdapterTest {

    @Nested
    @ClearDatabase
    final class UserEntityTests extends IntegrationTest {
        @Autowired private UserJpaRepository repository;

        @Test
        @DisplayName("Should save and assign values to audit fields")
        void shouldSaveAndAssignValuesToAuditFields() {
            // Given
            final UserEntity user =
                    new UserEntity(
                            Identifier.generate().getValue(),
                            "anyEmail@mail.com",
                            "anyPassword",
                            "John",
                            "Doe",
                            "abc",
                            "cde",
                            "anyImage",
                            UserRole.USER_ROLE,
                            UserStatus.ACTIVE,
                            UserType.AGENCY,
                            null);

            // When
            final UserEntity savedEntity = repository.save(user);

            // Then
            Assertions.assertThat(user.getCreatedAt()).isNull();
            Assertions.assertThat(user.getUpdatedAt()).isNull();

            Assertions.assertThat(savedEntity.getCreatedAt()).isNotNull();
            Assertions.assertThat(savedEntity.getUpdatedAt()).isNotNull();
            Assertions.assertThat(savedEntity.getCreatedAt()).isEqualTo(savedEntity.getUpdatedAt());
        }

        @Test
        @DisplayName("Should update updatedAt while update")
        void shouldUpdateUpdatedAtWhileUpdate() {
            // Given
            final UserEntity user =
                    new UserEntity(
                            Identifier.generate().getValue(),
                            "anyEmail@mail.com",
                            "anyPassword",
                            "John",
                            "Doe",
                            "abc",
                            "cde",
                            "anyImage",
                            UserRole.USER_ROLE,
                            UserStatus.ACTIVE,
                            UserType.AGENCY,
                            null);

            // When
            final UserEntity savedEntity = repository.saveAndFlush(user);
            final Instant savedEntityCreatedAt = savedEntity.getCreatedAt();
            final Instant savedEntityUpdatedAt = savedEntity.getUpdatedAt();

            // Then
            await().atMost(Duration.ofSeconds(2))
                    .untilAsserted(
                            () -> {
                                final UserEntity updatedEntity = repository.save(savedEntity);

                                Assertions.assertThat(updatedEntity.getCreatedAt())
                                        .isEqualTo(savedEntityCreatedAt);

                                Assertions.assertThat(updatedEntity.getUpdatedAt())
                                        .isAfter(savedEntityUpdatedAt);
                            });
        }
    }

    @Nested
    final class FindUserProfileTests {

        @Nested
        @ClearDatabase
        final class IntegrationTests extends IntegrationTest {
            @Autowired private UserJpaRepository repository;
            @Autowired private UserAdapter userAdapter;

            @Test
            @DisplayName("Should return value via projection")
            void shouldReturnValueViaProjection() {
                // Given
                final String email = "anyEmail@mail.com";

                final UserEntity user =
                        new UserEntity(
                                Identifier.generate().getValue(),
                                email,
                                "anyPassword",
                                "John",
                                "Doe",
                                "abc",
                                "cde",
                                "anyImage",
                                UserRole.USER_ROLE,
                                UserStatus.ACTIVE,
                                UserType.AGENCY,
                                null);

                repository.save(user);

                // When
                final var result = userAdapter.findUserProfile(email);

                // Then
                Assertions.assertThat(result)
                        .isPresent()
                        .get()
                        .returns(user.getId(), UserProfileProjection::getId)
                        .returns(user.getEmail(), UserProfileProjection::getEmail)
                        .returns(user.getFirstName(), UserProfileProjection::getFirstName)
                        .returns(user.getLastName(), UserProfileProjection::getLastName)
                        .returns(
                                user.getNotificationEmail(),
                                UserProfileProjection::getNotificationEmail)
                        .returns(
                                user.getNotificationPhoneNumber(),
                                UserProfileProjection::getNotificationPhoneNumber)
                        .returns(user.getAvatarUrl(), UserProfileProjection::getAvatarUrl)
                        .returns(user.getRole(), UserProfileProjection::getRole)
                        .returns(user.getType(), UserProfileProjection::getType)
                        .returns(user.getStatus(), UserProfileProjection::getStatus);
            }

            @Test
            @DisplayName("Should not return value when other users exist")
            void shouldNotReturnValueWhenOtherUsersExist() {
                // Given
                final String email = "anyEmail@mail.com";

                final UserEntity user =
                        new UserEntity(
                                Identifier.generate().getValue(),
                                email,
                                "anyPassword",
                                "John",
                                "Doe",
                                "abc",
                                "cde",
                                "anyImage",
                                UserRole.USER_ROLE,
                                UserStatus.ACTIVE,
                                UserType.AGENCY,
                                null);

                repository.save(user);

                // When
                final var result = userAdapter.findUserProfile("anyOtherEmail");

                // Then
                Assertions.assertThat(result).isEmpty();
            }
        }
    }

    @Nested
    final class FindAdvertisementUserTests {

        @Nested
        @ClearDatabase
        final class IntegrationTests extends IntegrationTest {
            @Autowired private UserJpaRepository repository;
            @Autowired private UserAdapter userAdapter;

            @Test
            @DisplayName("Should return value via projection")
            void shouldReturnValueViaProjection() {
                // Given
                final UUID id = Identifier.generate().getValue();

                final UserEntity user =
                        new UserEntity(
                                id,
                                "anyEmail@mail.com",
                                "anyPassword",
                                "John",
                                "Doe",
                                "abc",
                                "cde",
                                "anyImage",
                                UserRole.USER_ROLE,
                                UserStatus.ACTIVE,
                                UserType.AGENCY,
                                null);

                repository.save(user);

                // When
                final var result = userAdapter.findAdvertisementUser(id);

                // Then
                Assertions.assertThat(result)
                        .isPresent()
                        .get()
                        .returns(user.getId(), AdvertisementUserProjection::getId)
                        .returns(user.getFirstName(), AdvertisementUserProjection::getFirstName)
                        .returns(user.getLastName(), AdvertisementUserProjection::getLastName)
                        .returns(
                                user.getNotificationEmail(),
                                AdvertisementUserProjection::getNotificationEmail)
                        .returns(
                                user.getNotificationPhoneNumber(),
                                AdvertisementUserProjection::getNotificationPhoneNumber)
                        .returns(user.getType(), AdvertisementUserProjection::getType);
            }

            @Test
            @DisplayName("Should not return value when other users exist")
            void shouldNotReturnValueWhenOtherUsersExist() {
                // Given
                final UUID id = Identifier.generate().getValue();

                final UserEntity user =
                        new UserEntity(
                                Identifier.generate().getValue(),
                                "anyEmail@mail.com",
                                "anyPassword",
                                "John",
                                "Doe",
                                "abc",
                                "cde",
                                "anyImage",
                                UserRole.USER_ROLE,
                                UserStatus.ACTIVE,
                                UserType.AGENCY,
                                null);

                repository.save(user);

                // When
                final var result = userAdapter.findAdvertisementUser(id);

                // Then
                Assertions.assertThat(result).isEmpty();
            }
        }
    }

    @Nested
    final class FindByEmailTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private UserJpaRepository repository;
            @Mock private UserMapper mapper;
            private UserAdapter userAdapter;

            @BeforeEach
            void setUp() {
                userAdapter = new UserAdapter(repository, mapper);
            }

            @Test
            @DisplayName("Should return value")
            void shouldReturnValue() {
                // Given
                final UserEntity userEntity = mock();
                final String email = "anyMail@mail.com";

                BDDMockito.given(repository.findByEmail(email)).willReturn(Optional.of(userEntity));
                BDDMockito.given(mapper.toDomain(userEntity)).willReturn(mock());

                // When
                final var result = userAdapter.findByEmail(email);

                // Then
                Assertions.assertThat(result).isPresent();
            }
        }
    }

    @Nested
    final class GetUserTypesInBatchTests {

        @Nested
        @ClearDatabase
        final class IntegrationTests extends IntegrationTest {
            @Autowired private UserJpaRepository repository;
            @Autowired private UserAdapter userAdapter;

            @Test
            @DisplayName("Should return proper map")
            void shouldReturnProperMap() {
                // Given
                final List<UserEntity> entities = getEntities();
                entities.forEach(repository::save);

                final List<UUID> ids = entities.stream().map(UserEntity::getId).toList();

                // When
                final var result = userAdapter.getUserTypesInBatch(ids);

                // Then
                Assertions.assertThat(result)
                        .isNotEmpty()
                        .hasSize(entities.size())
                        .containsOnlyKeys(ids)
                        .containsEntry(entities.getFirst().getId(), entities.getFirst().getType())
                        .containsEntry(entities.get(1).getId(), entities.get(1).getType())
                        .containsEntry(entities.get(2).getId(), entities.get(2).getType());
            }

            private static List<UserEntity> getEntities() {
                final UserEntity firstUserEntity =
                        new UserEntity(
                                Identifier.generate().getValue(),
                                "anyEmail1@mail.com",
                                "anyPassword",
                                "John",
                                "Doe",
                                "abc",
                                "cde",
                                "anyImage",
                                UserRole.USER_ROLE,
                                UserStatus.ACTIVE,
                                UserType.AGENCY,
                                null);

                final UserEntity secondUserEntity =
                        new UserEntity(
                                Identifier.generate().getValue(),
                                "anyEmail2@mail.com",
                                "anyPassword",
                                "John",
                                "Doe",
                                "abc",
                                "cde",
                                "anyImage",
                                UserRole.USER_ROLE,
                                UserStatus.ACTIVE,
                                UserType.AGENCY,
                                null);

                final UserEntity thirdUserEntity =
                        new UserEntity(
                                Identifier.generate().getValue(),
                                "anyEmail3@mail.com",
                                "anyPassword",
                                "John",
                                "Doe",
                                "abc",
                                "cde",
                                "anyImage",
                                UserRole.USER_ROLE,
                                UserStatus.ACTIVE,
                                UserType.DEVELOPER,
                                null);

                return List.of(firstUserEntity, secondUserEntity, thirdUserEntity);
            }
        }
    }

    @Nested
    final class SaveTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private UserJpaRepository repository;
            @Mock private UserMapper mapper;
            private UserAdapter userAdapter;

            @BeforeEach
            void setUp() {
                userAdapter = new UserAdapter(repository, mapper);
            }

            @Test
            @DisplayName("Should save")
            void shouldSave() {
                // Given
                final User user = mock();
                BDDMockito.given(mapper.toEntity(user)).willReturn(mock());

                // When
                // Then
                Assertions.assertThatCode(() -> userAdapter.save(user)).doesNotThrowAnyException();
            }
        }
    }

    @Nested
    final class ExistsByEmailTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private UserJpaRepository repository;
            @Mock private UserMapper mapper;
            private UserAdapter userAdapter;

            @BeforeEach
            void setUp() {
                userAdapter = new UserAdapter(repository, mapper);
            }

            @ParameterizedTest
            @ValueSource(booleans = {true, false})
            @DisplayName("Should verify")
            void shouldVerify(final boolean exists) {
                // Given
                final String email = "anyEmail@mail.com";
                BDDMockito.given(repository.existsByEmail(email)).willReturn(exists);

                // When
                final boolean result = userAdapter.existsByEmail(email);

                // Then
                Assertions.assertThat(result).isEqualTo(exists);
            }
        }
    }

    @Nested
    final class DeleteByEmailTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private UserJpaRepository repository;
            @Mock private UserMapper mapper;
            private UserAdapter userAdapter;

            @BeforeEach
            void setUp() {
                userAdapter = new UserAdapter(repository, mapper);
            }

            @Test
            @DisplayName("Should delete")
            void shouldDelete() {
                // Given
                final String email = "anyEmail@mail.com";
                BDDMockito.given(repository.deleteByEmail(email)).willReturn(1);

                // When
                // Then
                Assertions.assertThatCode(() -> userAdapter.deleteByEmail(email))
                        .doesNotThrowAnyException();
            }

            @Test
            @DisplayName("Should throw exception when number of deletions is equal 0")
            void shouldThrowExceptionWhenNumberOfDeletionsIsEqualZero() {
                // Given
                final String email = "anyEmail@mail.com";
                BDDMockito.given(repository.deleteByEmail(email)).willReturn(0);

                // When
                // Then
                Assertions.assertThatThrownBy(() -> userAdapter.deleteByEmail(email))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    final class HasStatusTests {

        @Nested
        @ClearDatabase
        final class IntegrationTests extends IntegrationTest {
            @Autowired private UserJpaRepository repository;
            @Autowired private UserAdapter userAdapter;

            @Test
            @DisplayName("Should return true when user has given status")
            void shouldReturnTrueWhenUserHasGivenStatus() {
                // Given
                final String email = "anyEmail@mail.com";
                final UserStatus status = UserStatus.ACTIVE;

                final UserEntity user =
                        new UserEntity(
                                Identifier.generate().getValue(),
                                email,
                                "anyPassword",
                                "John",
                                "Doe",
                                "abc",
                                "cde",
                                "anyImage",
                                UserRole.USER_ROLE,
                                status,
                                UserType.AGENCY,
                                null);

                repository.save(user);

                // When
                final var result = userAdapter.hasStatus(email, status);

                // Then
                Assertions.assertThat(result).isTrue();
            }

            @Test
            @DisplayName("Should return false when user does not has given status")
            void shouldReturnFalseWhenUserDoesNotHasGivenStatus() {
                // Given
                final String email = "anyEmail@mail.com";
                final UserStatus status = UserStatus.ACTIVE;

                final UserEntity user =
                        new UserEntity(
                                Identifier.generate().getValue(),
                                email,
                                "anyPassword",
                                "John",
                                "Doe",
                                "abc",
                                "cde",
                                "anyImage",
                                UserRole.USER_ROLE,
                                UserStatus.BANNED,
                                UserType.AGENCY,
                                null);

                repository.save(user);

                // When
                final var result = userAdapter.hasStatus(email, status);

                // Then
                Assertions.assertThat(result).isFalse();
            }
        }
    }

    @Nested
    final class FindIdByEmailTests {

        @Nested
        final class IntegrationTests extends IntegrationTest {
            @Autowired private UserJpaRepository repository;
            @Autowired private UserAdapter userAdapter;

            @Test
            @ClearDatabase
            @DisplayName("Should return id")
            void shouldReturnId() {
                // Given
                final String email = "anyEmail@mail.com";

                final UserEntity user =
                        new UserEntity(
                                Identifier.generate().getValue(),
                                email,
                                "anyPassword",
                                "John",
                                "Doe",
                                "abc",
                                "cde",
                                "anyImage",
                                UserRole.USER_ROLE,
                                UserStatus.ACTIVE,
                                UserType.AGENCY,
                                null);

                repository.save(user);

                // When
                final var result = userAdapter.findIdByEmail(email);

                // Then
                Assertions.assertThat(result).isPresent().hasValue(user.getId());
            }
        }
    }
}
