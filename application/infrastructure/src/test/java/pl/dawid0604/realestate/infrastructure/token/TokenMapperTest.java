/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.token;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import java.time.Instant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.RefreshToken;

@ExtendWith(MockitoExtension.class)
class TokenMapperTest {
    private TokenMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new TokenMapper();
    }

    @Nested
    final class ToDomainTests {

        @Test
        @DisplayName("Should return null when value is null")
        void shouldReturnNullWhenValueIsNull() {
            // Given
            // When
            final RefreshToken result = mapper.toDomain(null);

            // Then
            Assertions.assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should map properly")
        void shouldMapProperly() {
            // Given
            final Identifier id = Identifier.generate();
            final Identifier userId = Identifier.generate();
            final String hashedToken = "anyHashedToken";
            final Instant createdAt = Instant.now().minusSeconds(125_000);
            final Instant expiresAt = Instant.now().plusSeconds(250_000);

            final RefreshTokenEntity entity = mock();
            given(entity.getId()).willReturn(id.getValue());
            given(entity.getUserId()).willReturn(userId.getValue());
            given(entity.getHashedToken()).willReturn(hashedToken);
            given(entity.getCreatedAt()).willReturn(createdAt);
            given(entity.getExpiresAt()).willReturn(expiresAt);

            // When
            final RefreshToken result = mapper.toDomain(entity);

            // Then
            Assertions.assertThat(result)
                    .returns(id, RefreshToken::getId)
                    .returns(userId, RefreshToken::getUserId)
                    .returns(hashedToken, RefreshToken::getToken)
                    .returns(createdAt, RefreshToken::getCreatedAt)
                    .returns(expiresAt, RefreshToken::getExpiresAt);
        }
    }

    @Nested
    final class ToEntityTests {

        @Test
        @DisplayName("Should return null when value is null")
        void shouldReturnNullWhenValueIsNull() {
            // Given
            // When
            final RefreshTokenEntity result = mapper.toEntity(null);

            // Then
            Assertions.assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should map properly")
        void shouldMapProperly() {
            // Given
            final Identifier id = Identifier.generate();
            final Identifier userId = Identifier.generate();
            final String hashedToken = "anyHashedToken";
            final Instant createdAt = Instant.now().minusSeconds(125_000);
            final Instant expiresAt = Instant.now().plusSeconds(250_000);
            final RefreshToken domain =
                    RefreshToken.reconstitute(id, userId, hashedToken, createdAt, expiresAt);

            // When
            final RefreshTokenEntity result = mapper.toEntity(domain);

            // Then
            Assertions.assertThat(result)
                    .returns(id.getValue(), RefreshTokenEntity::getId)
                    .returns(userId.getValue(), RefreshTokenEntity::getUserId)
                    .returns(hashedToken, RefreshTokenEntity::getHashedToken)
                    .returns(expiresAt, RefreshTokenEntity::getExpiresAt);
        }
    }
}
