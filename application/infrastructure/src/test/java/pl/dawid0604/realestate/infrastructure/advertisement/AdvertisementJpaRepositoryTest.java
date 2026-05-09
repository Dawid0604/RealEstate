/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import static java.util.Collections.emptySet;

import jakarta.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.infrastructure.IntegrationTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

class AdvertisementJpaRepositoryTest {

    @Nested
    final class SaveTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private FlatAdvertisementJpaRepository flatJpaRepository;
            @Mock private HouseAdvertisementJpaRepository houseJpaRepository;
            @Mock private CommercialAdvertisementJpaRepository commercialJpaRepository;
            @Mock private PlotAdvertisementJpaRepository plotJpaRepository;
            @Mock private FlatAdvertisementClaimJpaRepository flatAdvertisementClaimJpaRepository;
            @Mock private HouseAdvertisementClaimJpaRepository houseAdvertisementClaimJpaRepository;
            @Mock private PlotAdvertisementClaimJpaRepository plotAdvertisementClaimJpaRepository;
            @Mock private EntityManager entityManager;

            @Mock
            private CommercialAdvertisementClaimJpaRepository
                    commercialAdvertisementClaimJpaRepository;

            private AdvertisementJpaRepository advertisementJpaRepository;

            @BeforeEach
            void setUp() {
                advertisementJpaRepository =
                        new AdvertisementJpaRepository(
                                flatJpaRepository,
                                houseJpaRepository,
                                commercialJpaRepository,
                                plotJpaRepository,
                                flatAdvertisementClaimJpaRepository,
                                houseAdvertisementClaimJpaRepository,
                                commercialAdvertisementClaimJpaRepository,
                                plotAdvertisementClaimJpaRepository,
                                entityManager);
            }

            @Test
            @DisplayName("Should throw exception when entity is null")
            void shouldThrowExceptionWhenEntityIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(() -> advertisementJpaRepository.save(null))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("Entity cannot be null");
            }
        }

        @Nested
        final class IntegrationTests extends IntegrationTest {
            @Autowired private AdvertisementJpaRepository repository;
            @Autowired private FlatAdvertisementJpaRepository flatAdvertisementJpaRepository;
            @Autowired private HouseAdvertisementJpaRepository houseAdvertisementJpaRepository;

            @Autowired
            private CommercialAdvertisementJpaRepository commercialAdvertisementJpaRepository;

            @Autowired private PlotAdvertisementJpaRepository plotAdvertisementJpaRepository;

            @Test
            @DisplayName("Should save flat entity")
            @Sql(
                    statements = "ALTER TABLE flat_advertisements DISABLE TRIGGER ALL",
                    scripts = "/scripts/clear_database.sql",
                    executionPhase = BEFORE_TEST_METHOD)
            @Sql(
                    statements = "ALTER TABLE flat_advertisements ENABLE TRIGGER ALL",
                    executionPhase = AFTER_TEST_METHOD)
            void shouldSaveFlatEntity() {
                // Given
                final Set<FlatAdvertisementPhotoEntity> photos =
                        Set.of(
                                new FlatAdvertisementPhotoEntity(
                                        getId(), 0, "https://anyurl.com/1"),
                                new FlatAdvertisementPhotoEntity(
                                        getId(), 1, "https://anyurl.com/2"));

                final Set<FlatAdvertisementClaimEntity> claims =
                        Set.of(
                                new FlatAdvertisementClaimEntity(getId(), "abc", "xyz"),
                                new FlatAdvertisementClaimEntity(getId(), "cde", "qeq"));

                final FlatAdvertisementEntity entity =
                        new FlatAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                null,
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                claims,
                                photos,
                                FlatBuildingType.APARTMENT,
                                1,
                                2,
                                3,
                                2011,
                                TypeOfMarket.PRIMARY);

                claims.forEach(c -> c.setAdvertisement(entity));
                photos.forEach(p -> p.setAdvertisement(entity));
                flatAdvertisementJpaRepository.save(entity);

                // When
                // Then
                Assertions.assertThatCode(() -> repository.save(entity)).doesNotThrowAnyException();
            }

            @Test
            @DisplayName("Should save house entity")
            @Sql(
                    statements = "ALTER TABLE house_advertisements DISABLE TRIGGER ALL",
                    scripts = "/scripts/clear_database.sql",
                    executionPhase = BEFORE_TEST_METHOD)
            @Sql(
                    statements = "ALTER TABLE house_advertisements ENABLE TRIGGER ALL",
                    executionPhase = AFTER_TEST_METHOD)
            void shouldSaveHouseEntity() {
                // Given
                final Set<HouseAdvertisementPhotoEntity> photos =
                        Set.of(
                                new HouseAdvertisementPhotoEntity(
                                        getId(), 0, "https://anyurl.com/1"),
                                new HouseAdvertisementPhotoEntity(
                                        getId(), 1, "https://anyurl.com/2"));

                final Set<HouseAdvertisementClaimEntity> claims =
                        Set.of(
                                new HouseAdvertisementClaimEntity(getId(), "abc", "xyz"),
                                new HouseAdvertisementClaimEntity(getId(), "cde", "qeq"));

                final HouseAdvertisementEntity entity =
                        new HouseAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                null,
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                claims,
                                photos,
                                HouseBuildingType.DETACHED,
                                1,
                                2,
                                2011,
                                TypeOfMarket.PRIMARY);

                claims.forEach(c -> c.setAdvertisement(entity));
                photos.forEach(p -> p.setAdvertisement(entity));
                houseAdvertisementJpaRepository.save(entity);

                // When
                // Then
                Assertions.assertThatCode(() -> repository.save(entity)).doesNotThrowAnyException();
            }

            @Test
            @DisplayName("Should save commercial entity")
            @Sql(
                    statements = "ALTER TABLE commercial_advertisements DISABLE TRIGGER ALL",
                    scripts = "/scripts/clear_database.sql",
                    executionPhase = BEFORE_TEST_METHOD)
            @Sql(
                    statements = "ALTER TABLE commercial_advertisements ENABLE TRIGGER ALL",
                    executionPhase = AFTER_TEST_METHOD)
            void shouldSaveCommercialEntity() {
                // Given
                final Set<CommercialAdvertisementPhotoEntity> photos =
                        Set.of(
                                new CommercialAdvertisementPhotoEntity(
                                        getId(), 0, "https://anyurl.com/1"),
                                new CommercialAdvertisementPhotoEntity(
                                        getId(), 1, "https://anyurl.com/2"));

                final Set<CommercialAdvertisementClaimEntity> claims =
                        Set.of(
                                new CommercialAdvertisementClaimEntity(getId(), "abc", "xyz"),
                                new CommercialAdvertisementClaimEntity(getId(), "cde", "qeq"));

                final CommercialAdvertisementEntity entity =
                        new CommercialAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                null,
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                claims,
                                photos,
                                CommercialBuildingType.HALL,
                                1,
                                2,
                                3,
                                2011,
                                TypeOfMarket.PRIMARY);

                claims.forEach(c -> c.setAdvertisement(entity));
                photos.forEach(p -> p.setAdvertisement(entity));
                commercialAdvertisementJpaRepository.save(entity);

                // When
                // Then
                Assertions.assertThatCode(() -> repository.save(entity)).doesNotThrowAnyException();
            }

            @Test
            @DisplayName("Should save plot entity")
            @Sql(
                    statements = "ALTER TABLE plot_advertisements DISABLE TRIGGER ALL",
                    scripts = "/scripts/clear_database.sql",
                    executionPhase = BEFORE_TEST_METHOD)
            @Sql(
                    statements = "ALTER TABLE plot_advertisements ENABLE TRIGGER ALL",
                    executionPhase = AFTER_TEST_METHOD)
            void shouldSavePlotEntity() {
                // Given
                final Set<PlotAdvertisementPhotoEntity> photos =
                        Set.of(
                                new PlotAdvertisementPhotoEntity(
                                        getId(), 0, "https://anyurl.com/1"),
                                new PlotAdvertisementPhotoEntity(
                                        getId(), 1, "https://anyurl.com/2"));

                final Set<PlotAdvertisementClaimEntity> claims =
                        Set.of(
                                new PlotAdvertisementClaimEntity(getId(), "abc", "xyz"),
                                new PlotAdvertisementClaimEntity(getId(), "cde", "qeq"));

                final PlotAdvertisementEntity entity =
                        new PlotAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                null,
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                claims,
                                photos,
                                PlotBuildingType.AGRICULTURAL);

                claims.forEach(c -> c.setAdvertisement(entity));
                photos.forEach(p -> p.setAdvertisement(entity));
                plotAdvertisementJpaRepository.save(entity);

                // When
                // Then
                Assertions.assertThatCode(() -> repository.save(entity)).doesNotThrowAnyException();
            }
        }
    }

    @Nested
    final class FindBySlugTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private FlatAdvertisementJpaRepository flatJpaRepository;
            @Mock private HouseAdvertisementJpaRepository houseJpaRepository;
            @Mock private CommercialAdvertisementJpaRepository commercialJpaRepository;
            @Mock private PlotAdvertisementJpaRepository plotJpaRepository;
            @Mock private FlatAdvertisementClaimJpaRepository flatAdvertisementClaimJpaRepository;
            @Mock private HouseAdvertisementClaimJpaRepository houseAdvertisementClaimJpaRepository;
            @Mock private PlotAdvertisementClaimJpaRepository plotAdvertisementClaimJpaRepository;
            @Mock private EntityManager entityManager;

            @Mock
            private CommercialAdvertisementClaimJpaRepository
                    commercialAdvertisementClaimJpaRepository;

            private AdvertisementJpaRepository advertisementJpaRepository;

            @BeforeEach
            void setUp() {
                advertisementJpaRepository =
                        new AdvertisementJpaRepository(
                                flatJpaRepository,
                                houseJpaRepository,
                                commercialJpaRepository,
                                plotJpaRepository,
                                flatAdvertisementClaimJpaRepository,
                                houseAdvertisementClaimJpaRepository,
                                commercialAdvertisementClaimJpaRepository,
                                plotAdvertisementClaimJpaRepository,
                                entityManager);
            }

            @ParameterizedTest
            @NullAndEmptySource
            @DisplayName("Should throw exception when entity is null")
            void shouldThrowExceptionWhenSlugIsBlank(final String slug) {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        advertisementJpaRepository.findBySlug(
                                                slug, AdvertisementType.FLAT))
                        .isExactlyInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Slug cannot be blank");
            }
        }

        @Nested
        final class IntegrationTests extends IntegrationTest {
            @Autowired private AdvertisementJpaRepository repository;
            @Autowired private FlatAdvertisementJpaRepository flatAdvertisementJpaRepository;
            @Autowired private HouseAdvertisementJpaRepository houseAdvertisementJpaRepository;

            @Autowired
            private CommercialAdvertisementJpaRepository commercialAdvertisementJpaRepository;

            @Autowired private PlotAdvertisementJpaRepository plotAdvertisementJpaRepository;

            @Test
            @DisplayName("Should find flat claims")
            @Sql(
                    statements = "ALTER TABLE flat_advertisements DISABLE TRIGGER ALL",
                    scripts = "/scripts/clear_database.sql",
                    executionPhase = BEFORE_TEST_METHOD)
            @Sql(
                    statements = "ALTER TABLE flat_advertisements ENABLE TRIGGER ALL",
                    executionPhase = AFTER_TEST_METHOD)
            void shouldFindFlatClaims() {
                // Given
                final String key = "abc";
                final String value = "cde";
                final String key2 = "xyz";
                final String value2 = "qwe";
                final Set<FlatAdvertisementClaimEntity> claims =
                        Set.of(
                                new FlatAdvertisementClaimEntity(getId(), key, value),
                                new FlatAdvertisementClaimEntity(getId(), key2, value2));

                final FlatAdvertisementEntity entity =
                        new FlatAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                null,
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                claims,
                                emptySet(),
                                FlatBuildingType.APARTMENT,
                                null,
                                null,
                                null,
                                null,
                                TypeOfMarket.PRIMARY);

                claims.forEach(c -> c.setAdvertisement(entity));
                flatAdvertisementJpaRepository.save(entity);

                // When
                final var result = repository.findClaims(entity.getId(), AdvertisementType.FLAT);

                // Then
                assertClaims(claims, result);
            }

            @Test
            @DisplayName("Should find house claims")
            @Sql(
                    statements = "ALTER TABLE house_advertisements DISABLE TRIGGER ALL",
                    scripts = "/scripts/clear_database.sql",
                    executionPhase = BEFORE_TEST_METHOD)
            @Sql(
                    statements = "ALTER TABLE house_advertisements ENABLE TRIGGER ALL",
                    executionPhase = AFTER_TEST_METHOD)
            void shouldFindHouseClaims() {
                // Given
                final String key = "abc";
                final String value = "cde";
                final String key2 = "xyz";
                final String value2 = "qwe";
                final Set<HouseAdvertisementClaimEntity> claims =
                        Set.of(
                                new HouseAdvertisementClaimEntity(getId(), key, value),
                                new HouseAdvertisementClaimEntity(getId(), key2, value2));

                final HouseAdvertisementEntity entity =
                        new HouseAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                null,
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                claims,
                                emptySet(),
                                HouseBuildingType.DETACHED,
                                null,
                                null,
                                null,
                                TypeOfMarket.PRIMARY);

                claims.forEach(c -> c.setAdvertisement(entity));
                houseAdvertisementJpaRepository.save(entity);

                // When
                final var result = repository.findClaims(entity.getId(), AdvertisementType.HOUSE);

                // Then
                assertClaims(claims, result);
            }

            @Test
            @DisplayName("Should find commercial claims")
            @Sql(
                    statements = "ALTER TABLE commercial_advertisements DISABLE TRIGGER ALL",
                    scripts = "/scripts/clear_database.sql",
                    executionPhase = BEFORE_TEST_METHOD)
            @Sql(
                    statements = "ALTER TABLE commercial_advertisements ENABLE TRIGGER ALL",
                    executionPhase = AFTER_TEST_METHOD)
            void shouldFindCommercialClaims() {
                // Given
                final String key = "abc";
                final String value = "cde";
                final String key2 = "xyz";
                final String value2 = "qwe";
                final Set<CommercialAdvertisementClaimEntity> claims =
                        Set.of(
                                new CommercialAdvertisementClaimEntity(getId(), key, value),
                                new CommercialAdvertisementClaimEntity(getId(), key2, value2));

                final CommercialAdvertisementEntity entity =
                        new CommercialAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                null,
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                claims,
                                emptySet(),
                                CommercialBuildingType.HALL,
                                null,
                                null,
                                null,
                                null,
                                TypeOfMarket.PRIMARY);

                claims.forEach(c -> c.setAdvertisement(entity));
                commercialAdvertisementJpaRepository.save(entity);

                // When
                final var result =
                        repository.findClaims(entity.getId(), AdvertisementType.COMMERCIAL);

                // Then
                assertClaims(claims, result);
            }

            @Test
            @DisplayName("Should find plot claims")
            @Sql(
                    statements = "ALTER TABLE plot_advertisements DISABLE TRIGGER ALL",
                    scripts = "/scripts/clear_database.sql",
                    executionPhase = BEFORE_TEST_METHOD)
            @Sql(
                    statements = "ALTER TABLE plot_advertisements ENABLE TRIGGER ALL",
                    executionPhase = AFTER_TEST_METHOD)
            void shouldFindPlotClaims() {
                // Given
                final String key = "abc";
                final String value = "cde";
                final String key2 = "xyz";
                final String value2 = "qwe";
                final Set<PlotAdvertisementClaimEntity> claims =
                        Set.of(
                                new PlotAdvertisementClaimEntity(getId(), key, value),
                                new PlotAdvertisementClaimEntity(getId(), key2, value2));

                final PlotAdvertisementEntity entity =
                        new PlotAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                null,
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                claims,
                                emptySet(),
                                PlotBuildingType.AGRICULTURAL);

                claims.forEach(c -> c.setAdvertisement(entity));
                plotAdvertisementJpaRepository.save(entity);

                // When
                final var result = repository.findClaims(entity.getId(), AdvertisementType.PLOT);

                // Then
                assertClaims(claims, result);
            }

            private static void assertClaims(
                    final Set<? extends AdvertisementClaimEntity<?>> claims,
                    final Set<AdvertisementClaimProjection> result) {

                final List<AdvertisementClaimEntity<?>> listedClaims = new ArrayList<>(claims);

                Assertions.assertThat(result)
                        .hasSize(claims.size())
                        .extracting(
                                AdvertisementClaimProjection::getId,
                                AdvertisementClaimProjection::getClaimKey,
                                AdvertisementClaimProjection::getClaimValue)
                        .containsExactlyInAnyOrder(
                                Tuple.tuple(
                                        listedClaims.getFirst().getId(),
                                        listedClaims.getFirst().getClaimKey(),
                                        listedClaims.getFirst().getClaimValue()),
                                Tuple.tuple(
                                        listedClaims.getLast().getId(),
                                        listedClaims.getLast().getClaimKey(),
                                        listedClaims.getLast().getClaimValue()));
            }
        }
    }

    @Nested
    final class FindClaimsTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private FlatAdvertisementJpaRepository flatJpaRepository;
            @Mock private HouseAdvertisementJpaRepository houseJpaRepository;
            @Mock private CommercialAdvertisementJpaRepository commercialJpaRepository;
            @Mock private PlotAdvertisementJpaRepository plotJpaRepository;
            @Mock private FlatAdvertisementClaimJpaRepository flatAdvertisementClaimJpaRepository;
            @Mock private HouseAdvertisementClaimJpaRepository houseAdvertisementClaimJpaRepository;
            @Mock private PlotAdvertisementClaimJpaRepository plotAdvertisementClaimJpaRepository;
            @Mock private EntityManager entityManager;

            @Mock
            private CommercialAdvertisementClaimJpaRepository
                    commercialAdvertisementClaimJpaRepository;

            private AdvertisementJpaRepository advertisementJpaRepository;

            @BeforeEach
            void setUp() {
                advertisementJpaRepository =
                        new AdvertisementJpaRepository(
                                flatJpaRepository,
                                houseJpaRepository,
                                commercialJpaRepository,
                                plotJpaRepository,
                                flatAdvertisementClaimJpaRepository,
                                houseAdvertisementClaimJpaRepository,
                                commercialAdvertisementClaimJpaRepository,
                                plotAdvertisementClaimJpaRepository,
                                entityManager);
            }

            @Test
            @DisplayName("Should throw exception when id is null")
            void shouldThrowExceptionWhenIdIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        advertisementJpaRepository.findClaims(
                                                null, AdvertisementType.FLAT))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("Id cannot be null");
            }

            @Test
            @DisplayName("Should find flat claims")
            void shouldFindFlatClaims() {
                // Given
                final UUID id = getId();
                final Set<AdvertisementClaimProjection> projections =
                        Set.of(
                                mock(AdvertisementClaimProjection.class),
                                mock(AdvertisementClaimProjection.class));

                given(flatAdvertisementClaimJpaRepository.findClaimsById(id))
                        .willReturn(projections);

                // When
                final var result =
                        advertisementJpaRepository.findClaims(id, AdvertisementType.FLAT);

                // Then
                Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(projections);
            }

            @Test
            @DisplayName("Should find house claims")
            void shouldFindHouseClaims() {
                // Given
                final UUID id = getId();
                final Set<AdvertisementClaimProjection> projections =
                        Set.of(
                                mock(AdvertisementClaimProjection.class),
                                mock(AdvertisementClaimProjection.class));

                given(houseAdvertisementClaimJpaRepository.findClaimsById(id))
                        .willReturn(projections);

                // When
                final var result =
                        advertisementJpaRepository.findClaims(id, AdvertisementType.HOUSE);

                // Then
                Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(projections);
            }

            @Test
            @DisplayName("Should find commercial claims")
            void shouldFindCommercialClaims() {
                // Given
                final UUID id = getId();
                final Set<AdvertisementClaimProjection> projections =
                        Set.of(
                                mock(AdvertisementClaimProjection.class),
                                mock(AdvertisementClaimProjection.class));

                given(commercialAdvertisementClaimJpaRepository.findClaimsById(id))
                        .willReturn(projections);

                // When
                final var result =
                        advertisementJpaRepository.findClaims(id, AdvertisementType.COMMERCIAL);

                // Then
                Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(projections);
            }

            @Test
            @DisplayName("Should find plot claims")
            void shouldFindPlotClaims() {
                // Given
                final UUID id = getId();
                final Set<AdvertisementClaimProjection> projections =
                        Set.of(
                                mock(AdvertisementClaimProjection.class),
                                mock(AdvertisementClaimProjection.class));

                given(plotAdvertisementClaimJpaRepository.findClaimsById(id))
                        .willReturn(projections);

                // When
                final var result =
                        advertisementJpaRepository.findClaims(id, AdvertisementType.PLOT);

                // Then
                Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(projections);
            }

            private static UUID getId() {
                return UUID.randomUUID();
            }
        }
    }

    private static String getSlug() {
        return "any-slug-1234567890";
    }

    private static String getTitle() {
        return "any-title-abc-abc";
    }

    private static BigDecimal getPrice() {
        return BigDecimal.valueOf(25_000_000);
    }

    private static BigDecimal getPricePerSquareMeter() {
        return BigDecimal.valueOf(2500);
    }

    private static BigDecimal getArea() {
        return BigDecimal.valueOf(25);
    }

    private static UUID getId() {
        return UUID.randomUUID();
    }
}
