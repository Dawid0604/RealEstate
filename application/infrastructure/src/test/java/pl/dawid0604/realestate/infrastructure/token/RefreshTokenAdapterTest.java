/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.token;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.RefreshToken;

class RefreshTokenAdapterTest {

    @Nested
    @ExtendWith(MockitoExtension.class)
    final class SaveTests {
        @Mock private RefreshTokenJpaRepository jpaRepository;
        @Mock private TokenMapper tokenMapper;
        private RefreshTokenAdapter adapter;

        @BeforeEach
        void setUp() {
            this.adapter = new RefreshTokenAdapter(jpaRepository, tokenMapper);
        }

        @Test
        @DisplayName("Should save successfully")
        void shouldSaveSuccessfully() {
            // Given
            final RefreshToken domain = mock();
            final RefreshTokenEntity entity = mock();

            given(tokenMapper.toEntity(domain)).willReturn(entity);

            // When
            adapter.save(domain);

            // Then
            verify(jpaRepository).save(entity);
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    final class FindByIdTests {
        @Mock private RefreshTokenJpaRepository jpaRepository;
        @Mock private TokenMapper tokenMapper;
        private RefreshTokenAdapter adapter;

        @BeforeEach
        void setUp() {
            this.adapter = new RefreshTokenAdapter(jpaRepository, tokenMapper);
        }

        @Test
        @DisplayName("Should find successfully")
        void shouldFindSuccessfully() {
            // Given
            final Identifier id = Identifier.generate();
            final RefreshToken domain = mock();
            final RefreshTokenEntity entity = mock();

            given(tokenMapper.toDomain(entity)).willReturn(domain);
            given(jpaRepository.findByUserId(id.getValue())).willReturn(Optional.of(entity));

            // When
            final var result = adapter.findByUserId(id);

            // Then
            Assertions.assertThat(result).isNotEmpty().hasValue(domain);
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    final class DeleteIfExistsByUserIdTests {
        @Mock private RefreshTokenJpaRepository jpaRepository;
        @Mock private TokenMapper tokenMapper;
        private RefreshTokenAdapter adapter;

        @BeforeEach
        void setUp() {
            this.adapter = new RefreshTokenAdapter(jpaRepository, tokenMapper);
        }

        @Test
        @DisplayName("Should delete")
        void shouldDeleteSuccessfully() {
            // Given
            final Identifier id = Identifier.generate();

            // When
            adapter.deleteIfExistsByUserId(id);

            // Then
            verify(jpaRepository).deleteByUserId(id.getValue());
        }
    }
}
