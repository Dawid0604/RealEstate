/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import static org.awaitility.Awaitility.await;

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
import pl.dawid0604.realestate.infrastructure.ClearDatabase;
import pl.dawid0604.realestate.infrastructure.IntegrationTest;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

class LocalityAdapterTest {

    @Nested
    @ClearDatabase
    final class LocalityEntityTests extends IntegrationTest {
        @Autowired private LocalityJpaRepository repository;

        @Test
        @DisplayName("Should save and assign values to audit fields")
        void shouldSaveAndAssignValuesToAuditFields() {
            // Given
            final LocalityEntity user =
                    new LocalityEntity(Identifier.generate().getValue(), "name");

            // When
            final LocalityEntity savedEntity = repository.save(user);

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
            final LocalityEntity user =
                    new LocalityEntity(Identifier.generate().getValue(), "name");

            // When
            final LocalityEntity savedEntity = repository.saveAndFlush(user);
            final Instant savedEntityCreatedAt = savedEntity.getCreatedAt();
            final Instant savedEntityUpdatedAt = savedEntity.getUpdatedAt();

            // Then
            await().atMost(Duration.ofSeconds(2))
                    .untilAsserted(
                            () -> {
                                final LocalityEntity updatedEntity = repository.save(savedEntity);

                                Assertions.assertThat(updatedEntity.getCreatedAt())
                                        .isEqualTo(savedEntityCreatedAt);

                                Assertions.assertThat(updatedEntity.getUpdatedAt())
                                        .isAfter(savedEntityUpdatedAt);
                            });
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    final class ExistsByIdTests {
        @Mock private LocalityJpaRepository repository;
        @Mock private LocalityMapper localityMapper;
        private LocalityAdapter localityAdapter;

        @BeforeEach
        void setUp() {
            localityAdapter = new LocalityAdapter(repository, localityMapper);
        }

        @ParameterizedTest
        @DisplayName("Should verify")
        @ValueSource(booleans = {true, false})
        void shouldVerify(final boolean exists) {
            // Given
            final UUID id = Identifier.generate().getValue();
            BDDMockito.given(repository.existsById(id)).willReturn(exists);

            // When
            final boolean result = localityAdapter.existsById(id);

            // Then
            Assertions.assertThat(result).isEqualTo(exists);
        }
    }

    @Nested
    @ClearDatabase
    final class GetFullNamesInBatchTests extends IntegrationTest {
        @Autowired private LocalityJpaRepository repository;
        @Autowired private LocalityAdapter localityAdapter;

        @Test
        @DisplayName("Should return proper map")
        void shouldReturnProperMap() {
            // Given
            final List<LocalityEntity> entities = getEntities();
            entities.forEach(repository::save);

            final List<UUID> ids = entities.stream().map(LocalityEntity::getId).toList();

            // When
            final var result = localityAdapter.getFullNamesInBatch(ids);

            // Then
            Assertions.assertThat(result)
                    .isNotEmpty()
                    .hasSize(entities.size())
                    .containsOnlyKeys(ids)
                    .containsEntry(entities.getFirst().getId(), entities.getFirst().getName())
                    .containsEntry(entities.get(1).getId(), entities.get(1).getName())
                    .containsEntry(entities.get(2).getId(), entities.get(2).getName());
        }

        private static List<LocalityEntity> getEntities() {
            return List.of(
                    new LocalityEntity(Identifier.generate().getValue(), "abc"),
                    new LocalityEntity(Identifier.generate().getValue(), "cde"),
                    new LocalityEntity(Identifier.generate().getValue(), "fge"));
        }
    }
}
