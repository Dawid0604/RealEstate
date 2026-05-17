/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import pl.dawid0604.realestate.domain.shared.event.UserRegisteredEvent;
import pl.dawid0604.realestate.domain.shared.exception.ForbiddenException;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.time.Instant;

class UserTest {

    @Nested
    final class BuilderTests {

        @Nested
        final class CreateTests {

            @Test
            @DisplayName("Should throw exception when email is null")
            void shouldThrowExceptionWhenEmailIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(() -> User.create().build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Email cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when password is null")
            void shouldThrowExceptionWhenPasswordIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(() -> User.create().email(getValidEmail()).build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Password cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when fullName is null")
            void shouldThrowExceptionWhenFullNameIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.create()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("FullName cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when role is null")
            void shouldThrowExceptionWhenRoleIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.create()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Role cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when contactDetails is null")
            void shouldThrowExceptionWhenContactDetailsIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.create()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("ContactDetails cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when type is null")
            void shouldThrowExceptionWhenTypeIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.create()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Type cannot be null");
            }

            @Test
            @DisplayName("Should create instance successfully with nullable avatar")
            void shouldCreateInstanceSuccessfullyWithNullableAvatar() {
                // Given
                // When
                Assertions.assertThatCode(
                                () ->
                                        User.create()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .type(UserType.AGENCY)
                                                .build())
                        .doesNotThrowAnyException();
            }

            @Test
            @DisplayName("Should create instance successfully with avatar")
            void shouldCreateInstanceSuccessfullyWithAvatar() {
                // Given
                // When
                Assertions.assertThatCode(
                                () ->
                                        User.create()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .avatar(getValidAvatar())
                                                .type(UserType.AGENCY)
                                                .build())
                        .doesNotThrowAnyException();
            }

            @Test
            @DisplayName("Should generate id")
            void shouldGenerateId() {
                // Given
                // When
                final User instance =
                        User.create()
                                .email(getValidEmail())
                                .password(getValidPassword())
                                .fullName(getValidFullName())
                                .role(UserRole.ROLE_USER)
                                .contactDetails(getValidContactDetails())
                                .type(UserType.AGENCY)
                                .build();

                // Then
                Assertions.assertThat(instance.getId()).isNotNull();
            }

            @Test
            @DisplayName("Should not substitute id")
            void shouldNotSubstituteId() {
                // Given
                final Identifier identifier = getValidIdentifier();

                // When
                final User instance =
                        User.create()
                                .id(identifier)
                                .email(getValidEmail())
                                .password(getValidPassword())
                                .fullName(getValidFullName())
                                .role(UserRole.ROLE_USER)
                                .contactDetails(getValidContactDetails())
                                .type(UserType.AGENCY)
                                .build();

                // Then
                Assertions.assertThat(instance.getId()).isNotEqualTo(identifier);
            }

            @Test
            @DisplayName("Should set createdAt")
            void shouldSetCreatedAt() {
                // Given
                // When
                final User instance =
                        User.create()
                                .email(getValidEmail())
                                .password(getValidPassword())
                                .fullName(getValidFullName())
                                .role(UserRole.ROLE_USER)
                                .contactDetails(getValidContactDetails())
                                .type(UserType.AGENCY)
                                .build();

                // Then
                Assertions.assertThat(instance.getCreatedAt())
                        .isNotNull()
                        .matches(
                                d ->
                                        d.truncatedTo(SECONDS)
                                                .equals(Instant.now().truncatedTo(SECONDS)));
            }

            @Test
            @DisplayName("Should set status")
            void shouldSetStatus() {
                // Given
                // When
                final User instance =
                        User.create()
                                .email(getValidEmail())
                                .password(getValidPassword())
                                .fullName(getValidFullName())
                                .role(UserRole.ROLE_USER)
                                .contactDetails(getValidContactDetails())
                                .type(UserType.AGENCY)
                                .build();

                // Then
                Assertions.assertThat(instance.isInactive()).isTrue();
            }

            @Test
            @DisplayName("Should set type")
            void shouldSetType() {
                // Given
                final UserType type = UserType.AGENCY;

                // When
                final User instance =
                        User.create()
                                .email(getValidEmail())
                                .password(getValidPassword())
                                .fullName(getValidFullName())
                                .role(UserRole.ROLE_USER)
                                .contactDetails(getValidContactDetails())
                                .type(type)
                                .build();

                // Then
                Assertions.assertThat(instance.getType()).isEqualTo(type);
            }

            @Test
            @DisplayName("Should not substitute createdAt")
            void shouldNotSubstituteCreatedAt() {
                // Given
                final Instant createdAt = Instant.now().minusMillis(1_500_000);

                // When
                final User instance =
                        User.create()
                                .email(getValidEmail())
                                .password(getValidPassword())
                                .fullName(getValidFullName())
                                .role(UserRole.ROLE_USER)
                                .contactDetails(getValidContactDetails())
                                .createdAt(createdAt)
                                .type(UserType.AGENCY)
                                .build();

                // Then
                Assertions.assertThat(instance.getCreatedAt())
                        .isNotNull()
                        .doesNotMatch(
                                d -> d.truncatedTo(SECONDS).equals(createdAt.truncatedTo(SECONDS)));
            }

            @Test
            @DisplayName("Should not throw exception when createdAt is from the future")
            void shouldNotThrowExceptionWhenCreatedAtIsFromTheFuture() {
                // Given
                final Instant createdAt = Instant.now().plusMillis(1_500_000);

                // When
                // Then
                Assertions.assertThatCode(
                                () ->
                                        User.create()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .createdAt(createdAt)
                                                .type(UserType.AGENCY)
                                                .build())
                        .doesNotThrowAnyException();
            }

            @Test
            @DisplayName("Should set lastLoginAt")
            void shouldSetLastLoginAt() {
                // Given
                // When
                final User instance =
                        User.create()
                                .email(getValidEmail())
                                .password(getValidPassword())
                                .fullName(getValidFullName())
                                .role(UserRole.ROLE_USER)
                                .contactDetails(getValidContactDetails())
                                .type(UserType.AGENCY)
                                .build();

                // Then
                Assertions.assertThat(instance.getLastLoginAt()).isEmpty();
            }

            @Test
            @DisplayName("Should not substitute lastLoginAt")
            void shouldNotSubstituteLastLoginAt() {
                // Given
                final Instant lastLoginAt = Instant.now().minusMillis(1_500_000);

                // When
                final User instance =
                        User.create()
                                .email(getValidEmail())
                                .password(getValidPassword())
                                .fullName(getValidFullName())
                                .role(UserRole.ROLE_USER)
                                .contactDetails(getValidContactDetails())
                                .lastLoginAt(lastLoginAt)
                                .type(UserType.AGENCY)
                                .build();

                // Then
                Assertions.assertThat(instance.getLastLoginAt()).isEmpty();
            }

            @Test
            @DisplayName("Should not substitute status")
            void shouldNotSubstituteStatus() {
                // Given
                final UserStatus status = UserStatus.BANNED;

                // When
                final User instance =
                        User.create()
                                .email(getValidEmail())
                                .password(getValidPassword())
                                .fullName(getValidFullName())
                                .role(UserRole.ROLE_USER)
                                .contactDetails(getValidContactDetails())
                                .status(status)
                                .type(UserType.AGENCY)
                                .build();

                // Then
                Assertions.assertThat(instance.isInactive()).isTrue();
                Assertions.assertThat(instance.isBanned()).isFalse();
            }

            @Test
            @DisplayName("Should not throw exception when lastLoginAt is from the future")
            void shouldNotThrowExceptionWhenLastLoginAtIsFromTheFuture() {
                // Given
                final Instant lastLoginAt = Instant.now().plusMillis(1_500_000);

                // When
                // Then
                Assertions.assertThatCode(
                                () ->
                                        User.create()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .type(UserType.AGENCY)
                                                .lastLoginAt(lastLoginAt)
                                                .build())
                        .doesNotThrowAnyException();
            }
        }

        @Nested
        final class ReconstituteTests {

            @Test
            @DisplayName("Should throw exception when email is null")
            void shouldThrowExceptionWhenEmailIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(() -> User.reconstitute().build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Email cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when password is null")
            void shouldThrowExceptionWhenPasswordIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.reconstitute()
                                                .id(getValidIdentifier())
                                                .email(getValidEmail())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Password cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when fullName is null")
            void shouldThrowExceptionWhenFullNameIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.reconstitute()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("FullName cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when role is null")
            void shouldThrowExceptionWhenRoleIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.reconstitute()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Role cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when contactDetails is null")
            void shouldThrowExceptionWhenContactDetailsIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.reconstitute()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("ContactDetails cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when status is null")
            void shouldThrowExceptionWhenStatusIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.reconstitute()
                                                .id(Identifier.generate())
                                                .createdAt(Instant.now())
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .type(UserType.AGENCY)
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Status cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when type is null")
            void shouldThrowExceptionWhenTypeIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.reconstitute()
                                                .id(Identifier.generate())
                                                .createdAt(Instant.now())
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .status(UserStatus.ACTIVE)
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Type cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when id is null")
            void shouldThrowExceptionWhenIdIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.reconstitute()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .type(UserType.AGENCY)
                                                .status(UserStatus.ACTIVE)
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Id cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when createdAt is null")
            void shouldThrowExceptionWhenCreatedAtIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.reconstitute()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .type(UserType.AGENCY)
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .status(UserStatus.ACTIVE)
                                                .id(getValidIdentifier())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("CreatedAt cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when createdAt is from the future")
            void shouldThrowExceptionWhenCreatedAtIsFromTheFuture() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.reconstitute()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .status(UserStatus.ACTIVE)
                                                .id(getValidIdentifier())
                                                .type(UserType.AGENCY)
                                                .createdAt(Instant.now().plusMillis(2_500_000))
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("CreatedAt cannot be from the future");
            }

            @Test
            @DisplayName("Should create instance successfully with nullable avatar and lastLoginAt")
            void shouldCreateInstanceSuccessfullyWithNullableAvatarAndLastLoginAt() {
                // Given
                // When
                // Then
                Assertions.assertThatCode(
                                () ->
                                        User.reconstitute()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .status(UserStatus.ACTIVE)
                                                .type(UserType.AGENCY)
                                                .id(getValidIdentifier())
                                                .createdAt(Instant.now())
                                                .build())
                        .doesNotThrowAnyException();
            }

            @Test
            @DisplayName("Should create instance successfully with lastLoginAt")
            void shouldCreateInstanceSuccessfullyWithLastLoginAt() {
                // Given
                final Instant lastLoginAt = Instant.now().minusMillis(1_500_000);

                // When
                final User instance =
                        User.reconstitute()
                                .email(getValidEmail())
                                .password(getValidPassword())
                                .fullName(getValidFullName())
                                .role(UserRole.ROLE_USER)
                                .contactDetails(getValidContactDetails())
                                .status(UserStatus.ACTIVE)
                                .id(getValidIdentifier())
                                .createdAt(Instant.now())
                                .lastLoginAt(lastLoginAt)
                                .type(UserType.AGENCY)
                                .build();

                // Then
                Assertions.assertThat(instance.getLastLoginAt()).isPresent().hasValue(lastLoginAt);
            }

            @Test
            @DisplayName("Should throw exception when lastLoginAt is from the future")
            void shouldThrowExceptionWhenLastLoginAtIsFromTheFuture() {
                // Given
                final Instant lastLoginAt = Instant.now().plusMillis(1_500_000);

                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        User.reconstitute()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .status(UserStatus.ACTIVE)
                                                .type(UserType.AGENCY)
                                                .id(getValidIdentifier())
                                                .createdAt(Instant.now())
                                                .lastLoginAt(lastLoginAt)
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("LastLoginAt cannot be from the future");
            }

            @Test
            @DisplayName("Should create instance successfully with avatar")
            void shouldCreateInstanceSuccessfullyWithAvatar() {
                // Given
                // When
                // Then
                Assertions.assertThatCode(
                                () ->
                                        User.reconstitute()
                                                .email(getValidEmail())
                                                .password(getValidPassword())
                                                .fullName(getValidFullName())
                                                .role(UserRole.ROLE_USER)
                                                .contactDetails(getValidContactDetails())
                                                .status(UserStatus.ACTIVE)
                                                .id(getValidIdentifier())
                                                .createdAt(Instant.now())
                                                .type(UserType.AGENCY)
                                                .avatar(getValidAvatar())
                                                .build())
                        .doesNotThrowAnyException();
            }
        }
    }

    @ParameterizedTest
    @EnumSource(UserStatus.class)
    @DisplayName("Should return proper value at canLogin")
    void shouldReturnProperValueAtCanLogin(final UserStatus status) {
        // Given
        final boolean expectedValue = status == UserStatus.ACTIVE;

        // When
        final User instance =
                User.reconstitute()
                        .email(getValidEmail())
                        .password(getValidPassword())
                        .fullName(getValidFullName())
                        .role(UserRole.ROLE_USER)
                        .contactDetails(getValidContactDetails())
                        .status(status)
                        .id(getValidIdentifier())
                        .createdAt(Instant.now())
                        .type(UserType.AGENCY)
                        .build();

        // Then
        Assertions.assertThat(instance.canLogin()).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @EnumSource(UserStatus.class)
    @DisplayName("Should return proper value at isBanned")
    void shouldReturnProperValueAtIsBanned(final UserStatus status) {
        // Given
        final boolean expectedValue = status == UserStatus.BANNED;

        // When
        final User instance =
                User.reconstitute()
                        .email(getValidEmail())
                        .password(getValidPassword())
                        .fullName(getValidFullName())
                        .role(UserRole.ROLE_USER)
                        .contactDetails(getValidContactDetails())
                        .status(status)
                        .id(getValidIdentifier())
                        .createdAt(Instant.now())
                        .type(UserType.AGENCY)
                        .build();

        // Then
        Assertions.assertThat(instance.isBanned()).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @EnumSource(UserStatus.class)
    @DisplayName("Should return proper value at isInactive")
    void shouldReturnProperValueAtIsInactive(final UserStatus status) {
        // Given
        final boolean expectedValue = status == UserStatus.INACTIVE;

        // When
        final User instance =
                User.reconstitute()
                        .email(getValidEmail())
                        .password(getValidPassword())
                        .fullName(getValidFullName())
                        .role(UserRole.ROLE_USER)
                        .contactDetails(getValidContactDetails())
                        .status(status)
                        .id(getValidIdentifier())
                        .createdAt(Instant.now())
                        .type(UserType.AGENCY)
                        .type(UserType.AGENCY)
                        .build();

        // Then
        Assertions.assertThat(instance.isInactive()).isEqualTo(expectedValue);
    }

    @Nested
    final class BanTests {

        @Test
        @DisplayName("Should throw exception when status is banned")
        void shouldThrowExceptionWhenStatusIsBanned() {
            // Given
            // When
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.BANNED)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // Then
            Assertions.assertThatThrownBy(instance::ban)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("User is already banned");
        }

        @ParameterizedTest
        @EnumSource(UserStatus.class)
        @DisplayName("Should ban successfully")
        void shouldBanSuccessfully(final UserStatus status) {
            // Given
            if (status == UserStatus.BANNED) {
                return;
            }

            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(status)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.ban();

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.isBanned()).isTrue();
        }
    }

    @Nested
    final class UnbanTests {

        @ParameterizedTest
        @EnumSource(UserStatus.class)
        @DisplayName("Should throw exception when status is different than banned")
        void shouldThrowExceptionWhenStatusIsDifferentThanBanned(final UserStatus status) {
            // Given
            if (status == UserStatus.BANNED) {
                return;
            }

            // When
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(status)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // Then
            Assertions.assertThatThrownBy(instance::unban)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("User is not banned");
        }

        @ParameterizedTest
        @EnumSource(UserStatus.class)
        @DisplayName("Should unban successfully")
        void shouldUnbanSuccessfully(final UserStatus status) {
            // Given
            if (status != UserStatus.BANNED) {
                return;
            }

            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(status)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.unban();

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.canLogin()).isTrue();
        }
    }

    @Nested
    final class CanActivateTests {

        @Test
        @DisplayName("Should throw exception when status is active")
        void shouldThrowExceptionWhenStatusIsActive() {
            // Given
            // When
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // Then
            Assertions.assertThatThrownBy(instance::activate)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("User is already active");
        }

        @Test
        @DisplayName("Should throw exception when status is banned")
        void shouldThrowExceptionWhenStatusIsBanned() {
            // Given
            // When
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.BANNED)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // Then
            Assertions.assertThatThrownBy(instance::activate)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("User must be deactivated");
        }

        @Test
        @DisplayName("Should activate successfully")
        void shouldActivateSuccessfully() {
            // Given
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.INACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.activate();

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.canLogin()).isTrue();
        }
    }

    @Nested
    final class UpdatePasswordTests {

        @Test
        @DisplayName("Should throw exception when password is null")
        void shouldThrowExceptionWhenPasswordIsNull() {
            // Given
            // When
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // Then
            Assertions.assertThatThrownBy(() -> instance.updatePassword(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Password cannot be null");
        }

        @Test
        @DisplayName("Should update password successfully")
        void shouldUpdatePasswordSuccessfully() {
            // Given
            final Password newPassword = Password.ofHashed("$abcdyx");
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.updatePassword(newPassword);

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.getPassword()).isEqualTo(newPassword);
        }
    }

    @Nested
    final class UpdateEmailTests {

        @Test
        @DisplayName("Should throw exception when email is null")
        void shouldThrowExceptionWhenEmailIsNull() {
            // Given
            // When
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // Then
            Assertions.assertThatThrownBy(() -> instance.updateEmail(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Email cannot be null");
        }

        @Test
        @DisplayName("Should update email successfully")
        void shouldUpdateEmailSuccessfully() {
            // Given
            final Email newEmail = new Email("abc@mail.com");

            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.updateEmail(newEmail);

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.getEmail()).isEqualTo(newEmail);
        }
    }

    @Nested
    final class UpdateContactDetailsTests {

        @Test
        @DisplayName("Should throw exception when contactDetails is null")
        void shouldThrowExceptionWhenContactDetailsIsNull() {
            // Given
            // When
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // Then
            Assertions.assertThatThrownBy(() -> instance.updateContactDetails(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("ContactDetails cannot be null");
        }

        @Test
        @DisplayName("Should update contactDetails successfully")
        void shouldUpdateContactDetailsSuccessfully() {
            // Given
            final ContactDetails newContactDetails =
                    new ContactDetails(new Email("abc@mail.com"), null);

            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.updateContactDetails(newContactDetails);

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.getContactDetails()).isEqualTo(newContactDetails);
        }
    }

    @Nested
    final class UpdateFullNameTests {

        @Test
        @DisplayName("Should throw exception when fullName is null")
        void shouldThrowExceptionWhenFullNameIsNull() {
            // Given
            // When
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // Then
            Assertions.assertThatThrownBy(() -> instance.updateFullName(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("FullName cannot be null");
        }

        @Test
        @DisplayName("Should update fullName successfully")
        void shouldUpdateFullNameSuccessfully() {
            // Given
            final FullName newFullName = new FullName("abc", "cde");

            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.updateFullName(newFullName);

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.getFullName()).isEqualTo(newFullName);
        }
    }

    @Nested
    final class UpdateAvatarTests {

        @Test
        @DisplayName("Should update avatar successfully when value is present")
        void shouldUpdateFullNameSuccessfullyWhenValueIsPresent() {
            // Given
            final Url newAvatar = new Url("https://any");

            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.updateAvatar(newAvatar);

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.getAvatar()).isPresent().hasValue(newAvatar);
        }

        @Test
        @DisplayName("Should update avatar successfully when value is null")
        void shouldUpdateFullNameSuccessfullyWhenValueIsNull() {
            // Given
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.updateAvatar(null);

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.getAvatar()).isEmpty();
        }
    }

    @Nested
    final class HandleLoginTests {

        @Test
        @DisplayName("Should handle when lastLoginAt is null")
        void shouldHandleWhenLastLoginAtIsNull() {
            // Given
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.handleLogin();

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.getLastLoginAt())
                    .isPresent()
                    .map(v -> v.truncatedTo(MINUTES))
                    .hasValue(Instant.now().truncatedTo(MINUTES));
        }

        @Test
        @DisplayName("Should handle when lastLoginAt is present")
        void shouldHandleWhenLastLoginAtIsPresent() {
            // Given
            final Instant lastLoginAt = Instant.now().minusMillis(15_000);

            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .lastLoginAt(lastLoginAt)
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.handleLogin();

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.getLastLoginAt())
                    .isPresent()
                    .map(v -> v.truncatedTo(MINUTES))
                    .hasValue(Instant.now().truncatedTo(MINUTES));
        }
    }

    @Nested
    final class RegisterTests {

        @Test
        @DisplayName("Should register successfully")
        void shouldRegisterSuccessfully() {
            // Given
            final User instance =
                    User.create()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.register();

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.getEvents())
                    .anyMatch(UserRegisteredEvent.class::isInstance)
                    .hasSize(1);
        }

        @Test
        @DisplayName("Should throw exception when user is already registered")
        void shouldThrowExceptionWhenUserIsAlreadyRegistered() {
            // Given
            final User instance =
                    User.reconstitute()
                            .id(Identifier.generate())
                            .createdAt(Instant.now())
                            .status(UserStatus.ACTIVE)
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .type(UserType.AGENCY)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::register)
                    .isExactlyInstanceOf(ForbiddenException.class)
                    .hasMessage("User is already registered");
        }
    }

    @Nested
    final class UpdateTypeTests {

        @Test
        @DisplayName("Should update type successfully when value is present")
        void shouldUpdateFullNameSuccessfullyWhenValueIsPresent() {
            // Given
            final UserType newType = UserType.DEVELOPER;

            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            final User updatedInstance = instance.updateType(newType);

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.getType()).isEqualTo(newType);
        }

        @Test
        @DisplayName("Should throw exception when type is null")
        void shouldThrowExceptionWhenTypeIsNull() {
            // Given
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .type(UserType.AGENCY)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.updateType(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Type cannot be null");
        }
    }

    @Nested
    final class VerifyUserTests {

        @ParameterizedTest
        @EnumSource(UserStatus.class)
        @DisplayName("Should throw exception when status is invalid")
        void shouldThrowExceptionWhenStatusIsInvalid(final UserStatus status) {
            // Given
            if (status == UserStatus.ACTIVE) {
                return;
            }

            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .id(Identifier.generate())
                            .createdAt(Instant.now())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .type(UserType.AGENCY)
                            .status(status)
                            .build();

            // When
            Assertions.assertThatThrownBy(instance::verifyUser)
                    .isExactlyInstanceOf(ForbiddenException.class)
                    .hasMessage("User account has no permissions to perform this action");
        }

        @Test
        @DisplayName("Should not throw exception when status is valid")
        void shouldNotThrowExceptionWhenStatusIsValid() {
            // Given
            final User instance =
                    User.reconstitute()
                            .email(getValidEmail())
                            .id(Identifier.generate())
                            .createdAt(Instant.now())
                            .password(getValidPassword())
                            .fullName(getValidFullName())
                            .role(UserRole.ROLE_USER)
                            .contactDetails(getValidContactDetails())
                            .type(UserType.AGENCY)
                            .status(UserStatus.ACTIVE)
                            .build();

            // When
            Assertions.assertThatCode(instance::verifyUser).doesNotThrowAnyException();
        }
    }

    private static Identifier getValidIdentifier() {
        return Identifier.generate();
    }

    private static Email getValidEmail() {
        return new Email("abc@mail.com");
    }

    private static Password getValidPassword() {
        return Password.ofHashed("$xyz");
    }

    private static FullName getValidFullName() {
        return new FullName("abc", "bcd");
    }

    private static ContactDetails getValidContactDetails() {
        return new ContactDetails(getValidEmail(), null);
    }

    private static Url getValidAvatar() {
        return new Url("https://abc");
    }
}
