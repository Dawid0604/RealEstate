/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Collections.emptySet;

import jakarta.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
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
import org.springframework.test.context.jdbc.SqlMergeMode;

import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserCommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserFlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserHouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserPlotAdvertisementCardProjection;
import pl.dawid0604.realestate.infrastructure.IntegrationTest;

import java.lang.annotation.Retention;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ClearDatabase
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
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
            @DisableFlatConstraints
            @DisplayName("Should save flat entity")
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
            @DisableHouseConstraints
            @DisplayName("Should save house entity")
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
            @DisableCommercialConstraints
            @DisplayName("Should save commercial entity")
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
            @DisablePlotConstraints
            @DisplayName("Should save plot entity")
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

            @Test
            @DisplayName("Should return flat by slug")
            void shouldReturnFlatBySlug() {
                // Given
                final String slug = getSlug();
                final FlatAdvertisementEntity entity = mock();
                given(flatJpaRepository.findBySlug(getSlug())).willReturn(Optional.of(entity));

                // When
                var result = advertisementJpaRepository.findBySlug(slug, AdvertisementType.FLAT);

                // Then
                Assertions.assertThat(result).isPresent().hasValue(entity);
            }

            @Test
            @DisplayName("Should return house by slug")
            void shouldReturnHouseBySlug() {
                // Given
                final String slug = getSlug();
                final HouseAdvertisementEntity entity = mock();
                given(houseJpaRepository.findBySlug(getSlug())).willReturn(Optional.of(entity));

                // When
                var result = advertisementJpaRepository.findBySlug(slug, AdvertisementType.HOUSE);

                // Then
                Assertions.assertThat(result).isPresent().hasValue(entity);
            }

            @Test
            @DisplayName("Should return commercial by slug")
            void shouldReturnCommercialBySlug() {
                // Given
                final String slug = getSlug();
                final CommercialAdvertisementEntity entity = mock();
                given(commercialJpaRepository.findBySlug(getSlug()))
                        .willReturn(Optional.of(entity));

                // When
                var result =
                        advertisementJpaRepository.findBySlug(slug, AdvertisementType.COMMERCIAL);

                // Then
                Assertions.assertThat(result).isPresent().hasValue(entity);
            }

            @Test
            @DisplayName("Should return plot by slug")
            void shouldReturnPlotBySlug() {
                // Given
                final String slug = getSlug();
                final PlotAdvertisementEntity entity = mock();
                given(plotJpaRepository.findBySlug(getSlug())).willReturn(Optional.of(entity));

                // When
                var result = advertisementJpaRepository.findBySlug(slug, AdvertisementType.PLOT);

                // Then
                Assertions.assertThat(result).isPresent().hasValue(entity);
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
            @DisableFlatConstraints
            @DisplayName("Should find flat claims")
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
            @DisableHouseConstraints
            @DisplayName("Should find house claims")
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
            @DisableCommercialConstraints
            @DisplayName("Should find commercial claims")
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
            @DisablePlotConstraints
            @DisplayName("Should find plot claims")
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
    final class FindDetailsTests {

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
            @DisplayName("Should throw exception when slug is blank")
            void shouldThrowExceptionWhenSlugIsBlank(final String slug) {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        advertisementJpaRepository.findDetails(
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
            @DisableFlatConstraints
            @DisplayName("Should find flat details")
            void shouldFindFlatDetails() {
                // Given
                final FlatAdvertisementEntity entity =
                        new FlatAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                getDescription(),
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                emptySet(),
                                emptySet(),
                                FlatBuildingType.APARTMENT,
                                getNumberOfRooms(),
                                getFloor(),
                                getFloors(),
                                getBuiltYear(),
                                TypeOfMarket.PRIMARY);

                flatAdvertisementJpaRepository.save(entity);

                // When
                final var result = repository.findDetails(entity.getSlug(), AdvertisementType.FLAT);

                // Then
                Assertions.assertThat(result)
                        .isPresent()
                        .get()
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(
                                        FlatAdvertisementDetailsProjection.class))
                        .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                        .returns(entity.getId(), FlatAdvertisementDetailsProjection::getId)
                        .returns(entity.getSlug(), FlatAdvertisementDetailsProjection::getSlug)
                        .returns(entity.getTitle(), FlatAdvertisementDetailsProjection::getTitle)
                        .returns(
                                entity.getDescription(),
                                FlatAdvertisementDetailsProjection::getDescription)
                        .returns(entity.getPrice(), FlatAdvertisementDetailsProjection::getPrice)
                        .returns(entity.getArea(), FlatAdvertisementDetailsProjection::getArea)
                        .returns(
                                entity.getPricePerSquareMeter(),
                                FlatAdvertisementDetailsProjection::getPricePerSquareMeter)
                        .returns(
                                entity.getLocalityId(),
                                FlatAdvertisementDetailsProjection::getLocalityId)
                        .returns(entity.getUserId(), FlatAdvertisementDetailsProjection::getUserId)
                        .returns(
                                entity.isFeatured(), FlatAdvertisementDetailsProjection::isFeatured)
                        .returns(
                                entity.getBuildingType(),
                                FlatAdvertisementDetailsProjection::getBuildingType)
                        .returns(
                                entity.getNumberOfRooms(),
                                FlatAdvertisementDetailsProjection::getNumberOfRooms)
                        .returns(entity.getFloor(), FlatAdvertisementDetailsProjection::getFloor)
                        .returns(entity.getFloors(), FlatAdvertisementDetailsProjection::getFloors)
                        .returns(
                                entity.getTypeOfMarket(),
                                FlatAdvertisementDetailsProjection::getTypeOfMarket)
                        .satisfies(r -> Assertions.assertThat(r.getCreatedAt()).isNotNull());
            }

            @Test
            @DisableHouseConstraints
            @DisplayName("Should find house details")
            void shouldFindHouseDetails() {
                // Given
                final HouseAdvertisementEntity entity =
                        new HouseAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                getDescription(),
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                emptySet(),
                                emptySet(),
                                HouseBuildingType.DETACHED,
                                getNumberOfRooms(),
                                getFloors(),
                                getBuiltYear(),
                                TypeOfMarket.PRIMARY);

                houseAdvertisementJpaRepository.save(entity);

                // When
                final var result =
                        repository.findDetails(entity.getSlug(), AdvertisementType.HOUSE);

                // Then
                Assertions.assertThat(result)
                        .isPresent()
                        .get()
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(
                                        HouseAdvertisementDetailsProjection.class))
                        .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                        .returns(entity.getId(), HouseAdvertisementDetailsProjection::getId)
                        .returns(entity.getSlug(), HouseAdvertisementDetailsProjection::getSlug)
                        .returns(entity.getTitle(), HouseAdvertisementDetailsProjection::getTitle)
                        .returns(
                                entity.getDescription(),
                                HouseAdvertisementDetailsProjection::getDescription)
                        .returns(entity.getPrice(), HouseAdvertisementDetailsProjection::getPrice)
                        .returns(entity.getArea(), HouseAdvertisementDetailsProjection::getArea)
                        .returns(
                                entity.getPricePerSquareMeter(),
                                HouseAdvertisementDetailsProjection::getPricePerSquareMeter)
                        .returns(
                                entity.getLocalityId(),
                                HouseAdvertisementDetailsProjection::getLocalityId)
                        .returns(entity.getUserId(), HouseAdvertisementDetailsProjection::getUserId)
                        .returns(
                                entity.isFeatured(),
                                HouseAdvertisementDetailsProjection::isFeatured)
                        .returns(
                                entity.getBuildingType(),
                                HouseAdvertisementDetailsProjection::getBuildingType)
                        .returns(
                                entity.getNumberOfRooms(),
                                HouseAdvertisementDetailsProjection::getNumberOfRooms)
                        .returns(entity.getFloors(), HouseAdvertisementDetailsProjection::getFloors)
                        .returns(
                                entity.getTypeOfMarket(),
                                HouseAdvertisementDetailsProjection::getTypeOfMarket)
                        .satisfies(r -> Assertions.assertThat(r.getCreatedAt()).isNotNull());
            }

            @Test
            @DisableCommercialConstraints
            @DisplayName("Should find commercial details")
            void shouldFindCommercialDetails() {
                // Given
                final CommercialAdvertisementEntity entity =
                        new CommercialAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                getDescription(),
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                emptySet(),
                                emptySet(),
                                CommercialBuildingType.HALL,
                                getNumberOfRooms(),
                                getFloor(),
                                getFloors(),
                                getBuiltYear(),
                                TypeOfMarket.PRIMARY);

                commercialAdvertisementJpaRepository.save(entity);

                // When
                final var result =
                        repository.findDetails(entity.getSlug(), AdvertisementType.COMMERCIAL);

                // Then
                Assertions.assertThat(result)
                        .isPresent()
                        .get()
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(
                                        CommercialAdvertisementDetailsProjection.class))
                        .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                        .returns(entity.getId(), CommercialAdvertisementDetailsProjection::getId)
                        .returns(
                                entity.getSlug(), CommercialAdvertisementDetailsProjection::getSlug)
                        .returns(
                                entity.getTitle(),
                                CommercialAdvertisementDetailsProjection::getTitle)
                        .returns(
                                entity.getDescription(),
                                CommercialAdvertisementDetailsProjection::getDescription)
                        .returns(
                                entity.getPrice(),
                                CommercialAdvertisementDetailsProjection::getPrice)
                        .returns(
                                entity.getArea(), CommercialAdvertisementDetailsProjection::getArea)
                        .returns(
                                entity.getPricePerSquareMeter(),
                                CommercialAdvertisementDetailsProjection::getPricePerSquareMeter)
                        .returns(
                                entity.getLocalityId(),
                                CommercialAdvertisementDetailsProjection::getLocalityId)
                        .returns(
                                entity.getUserId(),
                                CommercialAdvertisementDetailsProjection::getUserId)
                        .returns(
                                entity.isFeatured(),
                                CommercialAdvertisementDetailsProjection::isFeatured)
                        .returns(
                                entity.getBuildingType(),
                                CommercialAdvertisementDetailsProjection::getBuildingType)
                        .returns(
                                entity.getNumberOfRooms(),
                                CommercialAdvertisementDetailsProjection::getNumberOfRooms)
                        .returns(
                                entity.getFloor(),
                                CommercialAdvertisementDetailsProjection::getFloor)
                        .returns(
                                entity.getFloors(),
                                CommercialAdvertisementDetailsProjection::getFloors)
                        .returns(
                                entity.getTypeOfMarket(),
                                CommercialAdvertisementDetailsProjection::getTypeOfMarket)
                        .satisfies(r -> Assertions.assertThat(r.getCreatedAt()).isNotNull());
            }

            @Test
            @DisablePlotConstraints
            @DisplayName("Should find plot details")
            void shouldFindPlotDetails() {
                // Given
                final PlotAdvertisementEntity entity =
                        new PlotAdvertisementEntity(
                                getId(),
                                getSlug(),
                                getTitle(),
                                getDescription(),
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                getId(),
                                true,
                                AdvertisementStatus.ACTIVE,
                                emptySet(),
                                emptySet(),
                                PlotBuildingType.AGRICULTURAL);

                plotAdvertisementJpaRepository.save(entity);

                // When
                final var result = repository.findDetails(entity.getSlug(), AdvertisementType.PLOT);

                // Then
                Assertions.assertThat(result)
                        .isPresent()
                        .get()
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(
                                        PlotAdvertisementDetailsProjection.class))
                        .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                        .returns(entity.getId(), PlotAdvertisementDetailsProjection::getId)
                        .returns(entity.getSlug(), PlotAdvertisementDetailsProjection::getSlug)
                        .returns(entity.getTitle(), PlotAdvertisementDetailsProjection::getTitle)
                        .returns(
                                entity.getDescription(),
                                PlotAdvertisementDetailsProjection::getDescription)
                        .returns(entity.getPrice(), PlotAdvertisementDetailsProjection::getPrice)
                        .returns(entity.getArea(), PlotAdvertisementDetailsProjection::getArea)
                        .returns(
                                entity.getPricePerSquareMeter(),
                                PlotAdvertisementDetailsProjection::getPricePerSquareMeter)
                        .returns(
                                entity.getLocalityId(),
                                PlotAdvertisementDetailsProjection::getLocalityId)
                        .returns(entity.getUserId(), PlotAdvertisementDetailsProjection::getUserId)
                        .returns(
                                entity.isFeatured(), PlotAdvertisementDetailsProjection::isFeatured)
                        .returns(
                                entity.getPlotType(),
                                PlotAdvertisementDetailsProjection::getPlotType)
                        .satisfies(r -> Assertions.assertThat(r.getCreatedAt()).isNotNull());
            }
        }
    }

    @Nested
    final class FindAdvertisementsByUserTests {

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
            @DisplayName("Should throw exception when userId is null")
            void shouldThrowExceptionWhenUserIdIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        advertisementJpaRepository.findAdvertisementsByUser(
                                                Set.of(), null, 0, 1))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("UserId cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when statuses are null")
            void shouldThrowExceptionWhenStatusesAreNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        advertisementJpaRepository.findAdvertisementsByUser(
                                                null, UUID.randomUUID(), 0, 1))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("Statuses cannot be null");
            }
        }

        @Nested
        final class IntegrationTests extends IntegrationTest {
            @Autowired private EntityManager entityManager;
            @Autowired private AdvertisementJpaRepository repository;
            @Autowired private FlatAdvertisementJpaRepository flatAdvertisementJpaRepository;
            @Autowired private HouseAdvertisementJpaRepository houseAdvertisementJpaRepository;

            @Autowired
            private CommercialAdvertisementJpaRepository commercialAdvertisementJpaRepository;

            @Autowired private PlotAdvertisementJpaRepository plotAdvertisementJpaRepository;

            @Retention(RUNTIME)
            @DisableFlatConstraints
            @DisablePlotConstraints
            @DisableHouseConstraints
            @DisableCommercialConstraints
            @interface DisableConstraints {}

            @Test
            @DisableConstraints
            @DisplayName("Should find advertisements with all statuses")
            void shouldFindAdvertisementsWithAllStatuses() {
                // Given
                final UUID userId = getId();
                final Set<AdvertisementStatus> statuses =
                        Arrays.stream(AdvertisementStatus.values()).collect(Collectors.toSet());

                final var entities = getEntities(userId);
                entities.forEach(repository::save);

                // When
                final var result = repository.findAdvertisementsByUser(statuses, userId, 0, 25);

                // Then
                Assertions.assertThat(result.getTotalElements()).isEqualTo(entities.size());
                Assertions.assertThat(result.getContent()).hasSize(entities.size());
                assertEntities(entities, result.getContent());
            }

            @Test
            @DisableConstraints
            @DisplayName("Should find advertisements with some statuses")
            void shouldFindAdvertisementsWithSomeStatuses() {
                // Given
                final UUID userId = getId();
                final Set<AdvertisementStatus> statuses =
                        Arrays.stream(AdvertisementStatus.values()).collect(Collectors.toSet());

                final int page = 0;
                final int pageSize = 2;

                final var entities = getEntities(userId);
                entities.forEach(repository::save);

                // When
                final var result =
                        repository.findAdvertisementsByUser(statuses, userId, page, pageSize);

                // Then
                Assertions.assertThat(result.getNumber()).isEqualTo(page);
                Assertions.assertThat(result.getSize()).isEqualTo(pageSize);
                Assertions.assertThat(result.getTotalPages())
                        .isEqualTo((int) Math.ceil((double) result.getTotalElements() / pageSize));
            }

            @Test
            @DisableConstraints
            @DisplayName("Should paginate results")
            void shouldPaginateResults() {
                // Given
                final UUID userId = getId();
                final AdvertisementStatus status = AdvertisementStatus.ACTIVE;

                final var entities = getEntities(userId);
                entities.forEach(repository::save);

                // When
                final var result =
                        repository.findAdvertisementsByUser(Set.of(status), userId, 0, 25);

                // Then
                final var filteredEntities =
                        entities.stream().filter(e -> e.getStatus() == status).toList();

                Assertions.assertThat(result.getTotalElements()).isEqualTo(filteredEntities.size());
                Assertions.assertThat(result.getContent()).hasSize(filteredEntities.size());
                assertEntities(filteredEntities, result.getContent());
            }

            private static void assertEntities(
                    final List<AdvertisementEntity<?, ?>> entities,
                    final List<UserAdvertisementCardProjection> pageContent) {

                for (final var entity : entities) {
                    final UserAdvertisementCardProjection projection =
                            pageContent.stream()
                                    .filter(p -> p.getId().equals(entity.getId()))
                                    .findFirst()
                                    .orElseGet(
                                            () ->
                                                    fail(
                                                            "Entity with id="
                                                                    + entity.getId()
                                                                    + " not found in page content"));

                    var assertion =
                            Assertions.assertThat(projection)
                                    .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                                    .returns(entity.getId(), UserAdvertisementCardProjection::getId)
                                    .returns(
                                            entity.getSlug(),
                                            UserAdvertisementCardProjection::getSlug)
                                    .returns(
                                            entity.getTitle(),
                                            UserAdvertisementCardProjection::getTitle)
                                    .returns(
                                            entity.getPrice(),
                                            UserAdvertisementCardProjection::getPrice)
                                    .returns(
                                            entity.getArea(),
                                            UserAdvertisementCardProjection::getArea)
                                    .returns(
                                            entity.getPricePerSquareMeter(),
                                            UserAdvertisementCardProjection::getPricePerSquareMeter)
                                    .returns(
                                            entity.isFeatured(),
                                            UserAdvertisementCardProjection::isFeatured)
                                    .returns(
                                            entity.getStatus(),
                                            UserAdvertisementCardProjection::getStatus);

                    switch (entity) {
                        case CommercialAdvertisementEntity commercialAdvertisementEntity ->
                                assertion
                                        .asInstanceOf(
                                                InstanceOfAssertFactories.type(
                                                        UserCommercialAdvertisementCardProjection
                                                                .class))
                                        .returns(
                                                AdvertisementType.COMMERCIAL,
                                                UserCommercialAdvertisementCardProjection::getType)
                                        .returns(
                                                commercialAdvertisementEntity.getBuildingType(),
                                                UserCommercialAdvertisementCardProjection
                                                        ::getBuildingType)
                                        .returns(
                                                commercialAdvertisementEntity.getNumberOfRooms(),
                                                UserCommercialAdvertisementCardProjection
                                                        ::getNumberOfRooms)
                                        .returns(
                                                commercialAdvertisementEntity.getFloor(),
                                                UserCommercialAdvertisementCardProjection::getFloor)
                                        .returns(
                                                commercialAdvertisementEntity.getFloors(),
                                                UserCommercialAdvertisementCardProjection
                                                        ::getFloors)
                                        .returns(
                                                commercialAdvertisementEntity.getBuiltYear(),
                                                UserCommercialAdvertisementCardProjection
                                                        ::getBuiltYear)
                                        .returns(
                                                commercialAdvertisementEntity.getTypeOfMarket(),
                                                UserCommercialAdvertisementCardProjection
                                                        ::getTypeOfMarket)
                                        .satisfies(
                                                e ->
                                                        Assertions.assertThat(e.getCreatedAt())
                                                                .isNotNull());

                        case FlatAdvertisementEntity flatAdvertisementEntity ->
                                assertion
                                        .asInstanceOf(
                                                InstanceOfAssertFactories.type(
                                                        UserFlatAdvertisementCardProjection.class))
                                        .returns(
                                                AdvertisementType.FLAT,
                                                UserFlatAdvertisementCardProjection::getType)
                                        .returns(
                                                flatAdvertisementEntity.getBuildingType(),
                                                UserFlatAdvertisementCardProjection
                                                        ::getBuildingType)
                                        .returns(
                                                flatAdvertisementEntity.getNumberOfRooms(),
                                                UserFlatAdvertisementCardProjection
                                                        ::getNumberOfRooms)
                                        .returns(
                                                flatAdvertisementEntity.getFloor(),
                                                UserFlatAdvertisementCardProjection::getFloor)
                                        .returns(
                                                flatAdvertisementEntity.getFloors(),
                                                UserFlatAdvertisementCardProjection::getFloors)
                                        .returns(
                                                flatAdvertisementEntity.getBuiltYear(),
                                                UserFlatAdvertisementCardProjection::getBuiltYear)
                                        .returns(
                                                flatAdvertisementEntity.getTypeOfMarket(),
                                                UserFlatAdvertisementCardProjection
                                                        ::getTypeOfMarket)
                                        .satisfies(
                                                e ->
                                                        Assertions.assertThat(e.getCreatedAt())
                                                                .isNotNull());

                        case HouseAdvertisementEntity houseAdvertisementEntity ->
                                assertion
                                        .asInstanceOf(
                                                InstanceOfAssertFactories.type(
                                                        UserHouseAdvertisementCardProjection.class))
                                        .returns(
                                                AdvertisementType.HOUSE,
                                                UserHouseAdvertisementCardProjection::getType)
                                        .returns(
                                                houseAdvertisementEntity.getBuildingType(),
                                                UserHouseAdvertisementCardProjection
                                                        ::getBuildingType)
                                        .returns(
                                                houseAdvertisementEntity.getNumberOfRooms(),
                                                UserHouseAdvertisementCardProjection
                                                        ::getNumberOfRooms)
                                        .returns(
                                                houseAdvertisementEntity.getFloors(),
                                                UserHouseAdvertisementCardProjection::getFloors)
                                        .returns(
                                                houseAdvertisementEntity.getBuiltYear(),
                                                UserHouseAdvertisementCardProjection::getBuiltYear)
                                        .returns(
                                                houseAdvertisementEntity.getTypeOfMarket(),
                                                UserHouseAdvertisementCardProjection
                                                        ::getTypeOfMarket)
                                        .satisfies(
                                                e ->
                                                        Assertions.assertThat(e.getCreatedAt())
                                                                .isNotNull());

                        case PlotAdvertisementEntity plotAdvertisementEntity ->
                                assertion
                                        .asInstanceOf(
                                                InstanceOfAssertFactories.type(
                                                        UserPlotAdvertisementCardProjection.class))
                                        .returns(
                                                AdvertisementType.PLOT,
                                                UserPlotAdvertisementCardProjection::getType)
                                        .returns(
                                                plotAdvertisementEntity.getPlotType(),
                                                UserPlotAdvertisementCardProjection::getPlotType)
                                        .satisfies(
                                                e ->
                                                        Assertions.assertThat(e.getCreatedAt())
                                                                .isNotNull());
                    }
                }
            }

            private static List<AdvertisementEntity<?, ?>> getEntities(final UUID userId) {
                return List.of(
                        new FlatAdvertisementEntity(
                                getId(),
                                getSlug() + "F",
                                getTitle() + "F",
                                getDescription() + "F",
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                userId,
                                true,
                                AdvertisementStatus.ACTIVE,
                                emptySet(),
                                emptySet(),
                                FlatBuildingType.APARTMENT,
                                getNumberOfRooms(),
                                getFloor(),
                                getFloors(),
                                getBuiltYear(),
                                TypeOfMarket.PRIMARY),
                        new FlatAdvertisementEntity(
                                getId(),
                                getSlug() + "F2",
                                getTitle() + "F2",
                                getDescription() + "F2",
                                getPrice().add(BigDecimal.valueOf(5)),
                                getArea().add(BigDecimal.valueOf(5)),
                                getPricePerSquareMeter().add(BigDecimal.valueOf(5)),
                                getId(),
                                userId,
                                false,
                                AdvertisementStatus.DELETED,
                                emptySet(),
                                emptySet(),
                                FlatBuildingType.BLOCK,
                                getNumberOfRooms() + 1,
                                getFloor() + 1,
                                getFloors() + 1,
                                getBuiltYear() + 1,
                                TypeOfMarket.SECONDARY),
                        new HouseAdvertisementEntity(
                                getId(),
                                getSlug() + "H",
                                getTitle() + "H",
                                getDescription() + "H",
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                userId,
                                true,
                                AdvertisementStatus.ACTIVE,
                                emptySet(),
                                emptySet(),
                                HouseBuildingType.DETACHED,
                                getNumberOfRooms(),
                                getFloors(),
                                getBuiltYear(),
                                TypeOfMarket.PRIMARY),
                        new HouseAdvertisementEntity(
                                getId(),
                                getSlug() + "H2",
                                getTitle() + "H2",
                                getDescription() + "H2",
                                getPrice().add(BigDecimal.valueOf(5)),
                                getArea().add(BigDecimal.valueOf(5)),
                                getPricePerSquareMeter().add(BigDecimal.valueOf(5)),
                                getId(),
                                userId,
                                false,
                                AdvertisementStatus.DELETED,
                                emptySet(),
                                emptySet(),
                                HouseBuildingType.MANSION,
                                getNumberOfRooms() + 1,
                                getFloors() + 1,
                                getBuiltYear() + 1,
                                TypeOfMarket.SECONDARY),
                        new CommercialAdvertisementEntity(
                                getId(),
                                getSlug() + "C",
                                getTitle() + "C",
                                getDescription() + "C",
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                userId,
                                true,
                                AdvertisementStatus.ACTIVE,
                                emptySet(),
                                emptySet(),
                                CommercialBuildingType.PREMISE,
                                getNumberOfRooms(),
                                getFloor(),
                                getFloors(),
                                getBuiltYear(),
                                TypeOfMarket.PRIMARY),
                        new CommercialAdvertisementEntity(
                                getId(),
                                getSlug() + "C2",
                                getTitle() + "C2",
                                getDescription() + "C2",
                                getPrice().add(BigDecimal.valueOf(5)),
                                getArea().add(BigDecimal.valueOf(5)),
                                getPricePerSquareMeter().add(BigDecimal.valueOf(5)),
                                getId(),
                                userId,
                                false,
                                AdvertisementStatus.DELETED,
                                emptySet(),
                                emptySet(),
                                CommercialBuildingType.WAREHOUSE,
                                getNumberOfRooms() + 1,
                                getFloor() + 1,
                                getFloors() + 1,
                                getBuiltYear() + 1,
                                TypeOfMarket.SECONDARY),
                        new PlotAdvertisementEntity(
                                getId(),
                                getSlug() + "P",
                                getTitle() + "P",
                                getDescription() + "P",
                                getPrice(),
                                getArea(),
                                getPricePerSquareMeter(),
                                getId(),
                                userId,
                                true,
                                AdvertisementStatus.ACTIVE,
                                emptySet(),
                                emptySet(),
                                PlotBuildingType.CONSTRUCTION),
                        new PlotAdvertisementEntity(
                                getId(),
                                getSlug() + "P2",
                                getTitle() + "P2",
                                getDescription() + "P2",
                                getPrice().add(BigDecimal.valueOf(5)),
                                getArea().add(BigDecimal.valueOf(5)),
                                getPricePerSquareMeter().add(BigDecimal.valueOf(5)),
                                getId(),
                                userId,
                                false,
                                AdvertisementStatus.DELETED,
                                emptySet(),
                                emptySet(),
                                PlotBuildingType.FOREST));
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

    private static String getDescription() {
        return "adadasdas";
    }

    private static Integer getNumberOfRooms() {
        return 1;
    }

    private static Integer getFloor() {
        return 2;
    }

    private static Integer getFloors() {
        return 3;
    }

    private static Integer getBuiltYear() {
        return 2013;
    }

    private static UUID getId() {
        return UUID.randomUUID();
    }
}
