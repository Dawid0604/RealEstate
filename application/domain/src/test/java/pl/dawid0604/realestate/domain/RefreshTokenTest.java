package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.time.Instant;

class RefreshTokenTest {

    @Nested
    final class CreateTests {

        @Test
        @DisplayName("Should throw exception when userId is null")
        void shouldThrowExceptionWhenUserIdIsNull() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> RefreshToken.create(null, null, null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("UserId cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when expiresAt is null")
        void shouldThrowExceptionWhenExpiresAtIsNull() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(
                            () -> RefreshToken.create(Identifier.generate(), getToken(), null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("ExpiresAt cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when expiresAt is from the past")
        void shouldThrowExceptionWhenExpiresAtIsFromThePast() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(
                            () ->
                                    RefreshToken.create(
                                            Identifier.generate(),
                                            getToken(),
                                            Instant.now().minusMillis(250_000)))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("ExpiresAt cannot be from the past");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when token is blank")
        void shouldThrowExceptionWhenTokenIsBlank(final String token) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(
                            () -> RefreshToken.create(Identifier.generate(), token, Instant.now()))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Token cannot be blank");
        }

        @Test
        @DisplayName("Should create successfully")
        void shouldCreateSuccessfully() {
            // Given
            final Identifier userId = Identifier.generate();
            final String token = getToken();
            final Instant expiresAt = Instant.now().plusMillis(250_000);

            // When
            final RefreshToken instance = RefreshToken.create(userId, token, expiresAt);

            // Then
            Assertions.assertThat(instance)
                    .isNotNull()
                    .satisfies(
                            v -> {
                                Assertions.assertThat(v.getId()).isNotNull();
                                Assertions.assertThat(v.getUserId()).isEqualTo(userId);
                                Assertions.assertThat(v.getExpiresAt()).isEqualTo(expiresAt);
                                Assertions.assertThat(v.getCreatedAt()).isNotNull();
                                Assertions.assertThat(v.getToken())
                                        .isNotBlank()
                                        .isNotEqualTo(token);
                            });
        }
    }

    @Nested
    final class ReconstituteTests {

        @Test
        @DisplayName("Should throw exception when id is null")
        void shouldThrowExceptionWhenIdIsNull() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(
                            () -> RefreshToken.reconstitute(null, null, null, null, null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Id cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when userId is null")
        void shouldThrowExceptionWhenUserIdIsNull() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(
                            () ->
                                    RefreshToken.reconstitute(
                                            Identifier.generate(), null, null, null, null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("UserId cannot be null");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when token is blank")
        void shouldThrowExceptionWhenTokenIsBlank(final String token) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(
                            () ->
                                    RefreshToken.reconstitute(
                                            Identifier.generate(),
                                            Identifier.generate(),
                                            token,
                                            Instant.now(),
                                            Instant.now()))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Token cannot be blank");
        }

        @Test
        @DisplayName("Should throw exception when createdAt is null")
        void shouldThrowExceptionWhenCreatedAtIsNull() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(
                            () ->
                                    RefreshToken.reconstitute(
                                            Identifier.generate(),
                                            Identifier.generate(),
                                            getToken(),
                                            null,
                                            Instant.now().minusMillis(250_000)))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("CreatedAt cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when createdAt is from the past")
        void shouldThrowExceptionWhenCreatedAtIsFromThePast() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(
                            () ->
                                    RefreshToken.reconstitute(
                                            Identifier.generate(),
                                            Identifier.generate(),
                                            getToken(),
                                            Instant.now().plusMillis(250_000),
                                            Instant.now().plusMillis(125_000)))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("CreatedAt cannot be from the future");
        }
    }

    @Nested
    final class TokenMatchesTests {

        @ParameterizedTest
        @CsvSource(
                value = {
                    "abc-xde,abc-XDE",
                    "abc-xde,other-token",
                    "abc-xde,",
                    "abc-xde, ",
                },
                ignoreLeadingAndTrailingWhitespace = false)
        @DisplayName("Should not match")
        void shouldNotMatch(final String token, final String incomingToken) {
            // Given
            final RefreshToken instance =
                    RefreshToken.reconstitute(
                            Identifier.generate(),
                            Identifier.generate(),
                            token,
                            Instant.now(),
                            Instant.now().plusMillis(125_000));

            // When
            final boolean result = instance.tokenMatches(incomingToken);

            // Then
            Assertions.assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should not match")
        void shouldMatch() {
            // Given

            // Both values are same
            final String token = "e59b5bbdf74688ae7ac4ad15cc621c75a8be1392962eeefdeefc24d15891387a";
            final String incomingToken = "realestate";

            final RefreshToken instance =
                    RefreshToken.reconstitute(
                            Identifier.generate(),
                            Identifier.generate(),
                            token,
                            Instant.now(),
                            Instant.now().plusMillis(125_000));

            // When
            final boolean result = instance.tokenMatches(incomingToken);

            // Then
            Assertions.assertThat(result).isTrue();
        }
    }

    private static String getToken() {
        return "any-token-abc";
    }
}
