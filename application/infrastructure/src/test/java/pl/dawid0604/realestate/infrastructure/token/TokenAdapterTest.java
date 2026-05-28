/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.token;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.ExpiredTokenException;
import pl.dawid0604.realestate.domain.shared.exception.InvalidTokenException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.time.Instant;
import java.util.Optional;

import javax.crypto.SecretKey;

@ExtendWith(MockitoExtension.class)
class TokenAdapterTest {
    @Mock private UserRepository userRepository;
    private TokenAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TokenAdapter(getValidJwtProperties(), userRepository);
    }

    @Nested
    final class GetUserEmailTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when token is blank")
        void shouldThrowExceptionWhenTokenIsBlank(final String token) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> adapter.getUserEmail(token))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Token cannot be blank");
        }

        @Test
        @DisplayName("Should throw exception when token expired")
        void shouldThrowExceptionWhenTokenExpired() {
            // Given
            given(userRepository.findUserRoleByEmail(getEmail()))
                    .willReturn(Optional.of(UserRole.ROLE_USER));

            adapter = new TokenAdapter(getExpiredJwtProperties(), userRepository);
            final String token = adapter.generateAccessToken(getEmail());

            // When
            // Then
            Assertions.assertThatThrownBy(() -> adapter.getUserEmail(token))
                    .isExactlyInstanceOf(ExpiredTokenException.class);
        }

        @Test
        @DisplayName("Should throw exception when token is invalid")
        void shouldThrowExceptionWhenTokenIsInvalid() {
            // Given
            given(userRepository.findUserRoleByEmail(getEmail()))
                    .willReturn(Optional.of(UserRole.ROLE_USER));

            final String token = adapter.generateAccessToken(getEmail());
            adapter = new TokenAdapter(getInvalidJwtProperties(), userRepository);

            // When
            // Then
            Assertions.assertThatThrownBy(() -> adapter.getUserEmail(token))
                    .isExactlyInstanceOf(InvalidTokenException.class);
        }

        @Test
        @DisplayName("Should get user username properly")
        void shouldGetUserEmailProperly() {
            // Given
            given(userRepository.findUserRoleByEmail(getEmail()))
                    .willReturn(Optional.of(UserRole.ROLE_USER));

            final String token = adapter.generateAccessToken(getEmail());

            // When
            final String userEmail = adapter.getUserEmail(token);

            // Then
            Assertions.assertThat(userEmail).isEqualTo(getEmail());
        }

        private static JwtProperties getExpiredJwtProperties() {
            return new JwtProperties(getSecret(), -1, 604800L);
        }

        private static JwtProperties getInvalidJwtProperties() {
            return new JwtProperties("etoe8nYmDjpaWcI0g4lu5bYt6zWEhMpxWyW2AsC6UkU", 900L, 604800L);
        }
    }

    @Nested
    final class GenerateAccessTokenTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when userEmail is blank")
        void shouldThrowExceptionWhenUserEmailIsBlank(final String userEmail) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> adapter.generateAccessToken(userEmail))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Email cannot be blank");
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> adapter.generateAccessToken(getEmail()))
                    .isExactlyInstanceOf(UserNotFoundException.class);

            verify(userRepository).findUserRoleByEmail(getEmail());
        }

        @Test
        @DisplayName("Should generate")
        void shouldGenerate() {
            // Given
            final UserRole role = UserRole.ROLE_USER;
            given(userRepository.findUserRoleByEmail(getEmail())).willReturn(Optional.of(role));

            // When
            final String token = adapter.generateAccessToken(getEmail());

            // Then
            Assertions.assertThat(getClaims(token))
                    .asInstanceOf(InstanceOfAssertFactories.type(Claims.class))
                    .satisfies(
                            claims -> {
                                Assertions.assertThat(claims).isNotEmpty();
                                Assertions.assertThat(claims.getSubject())
                                        .isNotNull()
                                        .isEqualTo(getEmail());

                                Assertions.assertThat(claims.get("role"))
                                        .isNotNull()
                                        .isEqualTo(role.name());

                                Assertions.assertThat(claims.getIssuedAt())
                                        .isNotNull()
                                        .isBefore(claims.getExpiration());

                                Assertions.assertThat(claims.getExpiration())
                                        .isNotNull()
                                        .isCloseTo(
                                                Instant.now()
                                                        .plusSeconds(
                                                                getValidJwtProperties()
                                                                        .accessTokenExpiration()),
                                                1_600_000);
                            });
        }
    }

    @Nested
    final class GenerateRefreshTokenTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when userEmail is blank")
        void shouldThrowExceptionWhenUserEmailIsBlank(final String userEmail) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> adapter.generateRefreshToken(userEmail))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Email cannot be blank");
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> adapter.generateRefreshToken(getEmail()))
                    .isExactlyInstanceOf(UserNotFoundException.class);

            verify(userRepository).existsByEmail(getEmail());
        }

        @Test
        @DisplayName("Should generate")
        void shouldGenerate() {
            // Given
            given(userRepository.existsByEmail(getEmail())).willReturn(true);

            // When
            final String token = adapter.generateRefreshToken(getEmail());

            // Then
            Assertions.assertThat(getClaims(token))
                    .asInstanceOf(InstanceOfAssertFactories.type(Claims.class))
                    .satisfies(
                            claims -> {
                                Assertions.assertThat(claims).isNotEmpty();
                                Assertions.assertThat(claims.getSubject())
                                        .isNotNull()
                                        .isEqualTo(getEmail());

                                Assertions.assertThat(claims.get("role")).isNull();

                                Assertions.assertThat(claims.getIssuedAt())
                                        .isNotNull()
                                        .isBefore(claims.getExpiration())
                                        .isBetween(
                                                Instant.now().minusSeconds(1),
                                                Instant.now().plusSeconds(1));

                                Assertions.assertThat(claims.getExpiration())
                                        .isNotNull()
                                        .isBetween(
                                                Instant.now()
                                                        .plusSeconds(
                                                                getValidJwtProperties()
                                                                        .refreshTokenExpiration())
                                                        .minusSeconds(1),
                                                Instant.now()
                                                        .plusSeconds(
                                                                getValidJwtProperties()
                                                                        .refreshTokenExpiration())
                                                        .plusSeconds(1));
                            });
        }
    }

    private static JwtProperties getValidJwtProperties() {
        return new JwtProperties(getSecret(), 900L, 604800L);
    }

    private static String getSecret() {
        return "dGVzdHNlY3JldHRlc3RzZWNyZXR0ZXN0c2VjcmV0dGVzdA==";
    }

    private static String getEmail() {
        return "anyMail@mail.com";
    }

    private static Claims getClaims(final String token) {
        return Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token).getPayload();
    }

    private static SecretKey signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(getSecret()));
    }
}
