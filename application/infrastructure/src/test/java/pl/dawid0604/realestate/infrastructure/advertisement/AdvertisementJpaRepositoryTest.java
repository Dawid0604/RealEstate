/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.SqlMergeMode;

import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchCommercialAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchFlatAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchHouseAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchPlotAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementCardProjection;
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
import pl.dawid0604.realestate.infrastructure.ClearDatabase;
import pl.dawid0604.realestate.infrastructure.IntegrationTest;

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
            @Autowired private AdvertisementJpaRepository repository;

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

    @Nested
    final class FindByCriteriaTests {

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
                Assertions.assertThatThrownBy(() -> advertisementJpaRepository.findByCriteria(null))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("Criteria cannot be null");
            }
        }

        @Nested
        final class IntegrationTests extends IntegrationTest {
            @Autowired private AdvertisementJpaRepository repository;

            @Nested
            @ClearDatabase
            @DisableConstraints
            final class AreaTests {

                @Test
                @DisplayName("Should find when criteria area is null")
                void shouldFindWhenCriteriaAreaIsNull() {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(null, null, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @DisplayName("Should find by areaFrom")
                @MethodSource("shouldFindByAreaFromDataProvider")
                void shouldFindByAreaFrom(final BigDecimal areaFrom, final BigDecimal area) {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(areaFrom, null, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    area,
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @DisplayName("Should find by areaTo")
                @MethodSource("shouldFindByAreaToDataProvider")
                void shouldFindByAreaTo(final BigDecimal areaTo, final BigDecimal area) {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(null, areaTo, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    area,
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @MethodSource("shouldFindByAreaFromTo")
                @DisplayName("Should find by areaFromTo")
                void shouldFindByAreaFromTo(
                        final BigDecimal areaFrom, final BigDecimal areaTo, final BigDecimal area) {

                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(areaFrom, areaTo, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    area,
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                private static Stream<Arguments> shouldFindByAreaFromTo() {
                    return Stream.of(
                            Arguments.of(
                                    BigDecimal.valueOf(25),
                                    BigDecimal.valueOf(25),
                                    BigDecimal.valueOf(25)),
                            Arguments.of(
                                    BigDecimal.valueOf(25),
                                    BigDecimal.valueOf(25.55),
                                    BigDecimal.valueOf(25.35)),
                            Arguments.of(
                                    BigDecimal.valueOf(1),
                                    BigDecimal.valueOf(25),
                                    BigDecimal.valueOf(1)),
                            Arguments.of(
                                    BigDecimal.valueOf(1),
                                    BigDecimal.valueOf(25),
                                    BigDecimal.valueOf(25)),
                            Arguments.of(
                                    BigDecimal.valueOf(1),
                                    BigDecimal.valueOf(25),
                                    BigDecimal.valueOf(16)));
                }

                private static Stream<Arguments> shouldFindByAreaFromDataProvider() {
                    return Stream.of(
                            Arguments.of(BigDecimal.valueOf(25), BigDecimal.valueOf(25)),
                            Arguments.of(BigDecimal.valueOf(25), BigDecimal.valueOf(25.25)),
                            Arguments.of(BigDecimal.valueOf(25.1), BigDecimal.valueOf(25.25)),
                            Arguments.of(BigDecimal.valueOf(25), BigDecimal.valueOf(35.25)));
                }

                private static Stream<Arguments> shouldFindByAreaToDataProvider() {
                    return Stream.of(
                            Arguments.of(BigDecimal.valueOf(25), BigDecimal.valueOf(25)),
                            Arguments.of(BigDecimal.valueOf(25), BigDecimal.valueOf(23.25)),
                            Arguments.of(BigDecimal.valueOf(25.1), BigDecimal.valueOf(25)),
                            Arguments.of(BigDecimal.valueOf(25), BigDecimal.valueOf(15.25)));
                }

                private static SearchFlatAdvertisementsCriteria getCriteria(
                        final BigDecimal areaFrom, final BigDecimal areaTo, final UUID localityId) {

                    return new SearchFlatAdvertisementsCriteria(
                            areaFrom,
                            areaTo,
                            null,
                            null,
                            null,
                            null,
                            0,
                            1,
                            null,
                            localityId,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
                }
            }

            @Nested
            @ClearDatabase
            @DisableConstraints
            final class PriceTests {

                @Test
                @DisplayName("Should find when criteria price is null")
                void shouldFindWhenCriteriaPriceIsNull() {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(null, null, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @DisplayName("Should find by priceFrom")
                @MethodSource("shouldFindByPriceFromDataProvider")
                void shouldFindByPriceFrom(final BigDecimal priceFrom, final BigDecimal price) {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(priceFrom, null, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    price,
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @DisplayName("Should find by priceTo")
                @MethodSource("shouldFindByPriceToDataProvider")
                void shouldFindByPriceTo(final BigDecimal priceTo, final BigDecimal price) {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(null, priceTo, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    price,
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @MethodSource("shouldFindByPriceFromTo")
                @DisplayName("Should find by priceFromTo")
                void shouldFindByPriceFromTo(
                        final BigDecimal priceFrom,
                        final BigDecimal priceTo,
                        final BigDecimal price) {

                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(priceFrom, priceTo, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    price,
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                private static Stream<Arguments> shouldFindByPriceFromTo() {
                    return Stream.of(
                            Arguments.of(
                                    BigDecimal.valueOf(250000),
                                    BigDecimal.valueOf(250000),
                                    BigDecimal.valueOf(250000)),
                            Arguments.of(
                                    BigDecimal.valueOf(250000),
                                    BigDecimal.valueOf(250000.55),
                                    BigDecimal.valueOf(250000.35)),
                            Arguments.of(
                                    BigDecimal.valueOf(1),
                                    BigDecimal.valueOf(250000),
                                    BigDecimal.valueOf(1)),
                            Arguments.of(
                                    BigDecimal.valueOf(1),
                                    BigDecimal.valueOf(250000),
                                    BigDecimal.valueOf(250000)),
                            Arguments.of(
                                    BigDecimal.valueOf(1),
                                    BigDecimal.valueOf(250000),
                                    BigDecimal.valueOf(160000)));
                }

                private static Stream<Arguments> shouldFindByPriceFromDataProvider() {
                    return Stream.of(
                            Arguments.of(BigDecimal.valueOf(250000), BigDecimal.valueOf(250000)),
                            Arguments.of(BigDecimal.valueOf(250000), BigDecimal.valueOf(250000.25)),
                            Arguments.of(
                                    BigDecimal.valueOf(250000.1), BigDecimal.valueOf(250000.25)),
                            Arguments.of(
                                    BigDecimal.valueOf(250000), BigDecimal.valueOf(350000.25)));
                }

                private static Stream<Arguments> shouldFindByPriceToDataProvider() {
                    return Stream.of(
                            Arguments.of(BigDecimal.valueOf(250000), BigDecimal.valueOf(250000)),
                            Arguments.of(BigDecimal.valueOf(250000), BigDecimal.valueOf(230000.25)),
                            Arguments.of(BigDecimal.valueOf(250000.1), BigDecimal.valueOf(250000)),
                            Arguments.of(
                                    BigDecimal.valueOf(250000), BigDecimal.valueOf(150000.25)));
                }

                private static SearchFlatAdvertisementsCriteria getCriteria(
                        final BigDecimal priceFrom,
                        final BigDecimal priceTo,
                        final UUID localityId) {

                    return new SearchFlatAdvertisementsCriteria(
                            null,
                            null,
                            priceFrom,
                            priceTo,
                            null,
                            null,
                            0,
                            1,
                            null,
                            localityId,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
                }
            }

            @Nested
            @ClearDatabase
            @DisableConstraints
            final class PricePerSquareMeterTests {

                @Test
                @DisplayName("Should find when criteria pricePerSquareMeter is null")
                void shouldFindWhenCriteriaPerSquareMeterIsNull() {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(null, null, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @DisplayName("Should find by pricePerSquareMeterFrom")
                @MethodSource("shouldFindByPricePerSquareMeterFromDataProvider")
                void shouldFindByPricePerSquareMeterFrom(
                        final BigDecimal pricePerSquareMeterFrom,
                        final BigDecimal pricePerSquareMeter) {

                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(pricePerSquareMeterFrom, null, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    pricePerSquareMeter,
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @DisplayName("Should find by pricePerSquareMeterTo")
                @MethodSource("shouldFindByPricePerSquareMeterToDataProvider")
                void shouldFindByPricePerSquareMeterTo(
                        final BigDecimal pricePerSquareMeterTo,
                        final BigDecimal pricePerSquareMeter) {

                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(null, pricePerSquareMeterTo, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    pricePerSquareMeter,
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @MethodSource("shouldFindByPricePerSquareMeterFromTo")
                @DisplayName("Should find by pricePerSquareMeterFromTo")
                void shouldFindByPricePerSquareMeterFromTo(
                        final BigDecimal pricePerSquareMeterFrom,
                        final BigDecimal pricePerSquareMeterTo,
                        final BigDecimal pricePerSquareMeter) {

                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(pricePerSquareMeterFrom, pricePerSquareMeterTo, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    pricePerSquareMeter,
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                private static Stream<Arguments> shouldFindByPricePerSquareMeterFromTo() {
                    return Stream.of(
                            Arguments.of(
                                    BigDecimal.valueOf(25000),
                                    BigDecimal.valueOf(25000),
                                    BigDecimal.valueOf(25000)),
                            Arguments.of(
                                    BigDecimal.valueOf(25000),
                                    BigDecimal.valueOf(25000.55),
                                    BigDecimal.valueOf(25000.35)),
                            Arguments.of(
                                    BigDecimal.valueOf(1),
                                    BigDecimal.valueOf(25000),
                                    BigDecimal.valueOf(1)),
                            Arguments.of(
                                    BigDecimal.valueOf(1),
                                    BigDecimal.valueOf(25000),
                                    BigDecimal.valueOf(25000)),
                            Arguments.of(
                                    BigDecimal.valueOf(1),
                                    BigDecimal.valueOf(25000),
                                    BigDecimal.valueOf(16000)));
                }

                private static Stream<Arguments> shouldFindByPricePerSquareMeterFromDataProvider() {
                    return Stream.of(
                            Arguments.of(BigDecimal.valueOf(25000), BigDecimal.valueOf(25000)),
                            Arguments.of(BigDecimal.valueOf(25000), BigDecimal.valueOf(25000.25)),
                            Arguments.of(BigDecimal.valueOf(25000.1), BigDecimal.valueOf(25000.25)),
                            Arguments.of(BigDecimal.valueOf(25000), BigDecimal.valueOf(35000.25)));
                }

                private static Stream<Arguments> shouldFindByPricePerSquareMeterToDataProvider() {
                    return Stream.of(
                            Arguments.of(BigDecimal.valueOf(25000), BigDecimal.valueOf(25000)),
                            Arguments.of(BigDecimal.valueOf(25000), BigDecimal.valueOf(23000.25)),
                            Arguments.of(BigDecimal.valueOf(25000.1), BigDecimal.valueOf(25000)),
                            Arguments.of(BigDecimal.valueOf(25000), BigDecimal.valueOf(15000.25)));
                }

                private static SearchFlatAdvertisementsCriteria getCriteria(
                        final BigDecimal pricePerSquareMeterFrom,
                        final BigDecimal pricePerSquareMeterTo,
                        final UUID localityId) {

                    return new SearchFlatAdvertisementsCriteria(
                            null,
                            null,
                            null,
                            null,
                            pricePerSquareMeterFrom,
                            pricePerSquareMeterTo,
                            0,
                            1,
                            null,
                            localityId,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
                }
            }

            @Nested
            @ClearDatabase
            @DisableConstraints
            final class DateTests {

                @Test
                @DisplayName("Should find when criteria date is null")
                void shouldFindWhenCriteriaDateIsNull() {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(null, null, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @DisplayName("Should find by dateFrom")
                @MethodSource("shouldFindByDateFromDataProvider")
                void shouldFindByDateFrom(final LocalDate dateFrom) {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(dateFrom, null, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @DisplayName("Should find by dateTo")
                @MethodSource("shouldFindByDateToDataProvider")
                void shouldFindByDateTo(final LocalDate dateTo) {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(null, dateTo, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @ParameterizedTest
                @MethodSource("shouldFindByDateFromTo")
                @DisplayName("Should find by dateFromTo")
                void shouldFindByDateFromTo(final LocalDate dateFrom, final LocalDate dateTo) {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(dateFrom, dateTo, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                private static Stream<Arguments> shouldFindByDateFromTo() {
                    return Stream.of(
                            Arguments.of(LocalDate.now().minusDays(1), LocalDate.now()),
                            Arguments.of(LocalDate.now().minusDays(15), LocalDate.now()));
                }

                private static Stream<Arguments> shouldFindByDateFromDataProvider() {
                    return Stream.of(
                            Arguments.of(LocalDate.now().minusDays(1)),
                            Arguments.of(LocalDate.now().minusWeeks(1)));
                }

                private static Stream<Arguments> shouldFindByDateToDataProvider() {
                    return Stream.of(Arguments.of(LocalDate.now()));
                }

                private static SearchFlatAdvertisementsCriteria getCriteria(
                        final LocalDate dateFrom, final LocalDate dateTo, final UUID localityId) {

                    return new SearchFlatAdvertisementsCriteria(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            0,
                            1,
                            null,
                            localityId,
                            dateFrom,
                            dateTo,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
                }
            }

            @Nested
            @ClearDatabase
            @DisableConstraints
            final class TypesTests {

                @Test
                @DisplayName("Should find when criteria types are empty")
                void shouldFindWhenCriteriaTypesAreEmpty() {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(emptySet(), localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @Test
                @DisplayName("Should find by type")
                void shouldFindByType() {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(Set.of(FlatBuildingType.APARTMENT.name()), localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @Test
                @DisplayName("Should find by multiple types")
                void shouldFindByMultipleTypes() {
                    // Given
                    final UUID localityId = getId();
                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(
                                    Set.of(
                                            FlatBuildingType.APARTMENT.name(),
                                            FlatBuildingType.LOFT.name()),
                                    localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    final FlatAdvertisementEntity secondEntity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F2",
                                    getTitle() + "F2",
                                    getDescription() + "F2",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
                                    getId(),
                                    true,
                                    AdvertisementStatus.ACTIVE,
                                    emptySet(),
                                    emptySet(),
                                    FlatBuildingType.LOFT,
                                    getNumberOfRooms(),
                                    getFloor(),
                                    getFloors(),
                                    getBuiltYear(),
                                    TypeOfMarket.PRIMARY);

                    repository.save(entity);
                    repository.save(secondEntity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(2)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactlyInAnyOrder(entity.getId(), secondEntity.getId());
                }

                private static SearchFlatAdvertisementsCriteria getCriteria(
                        final Set<String> types, final UUID localityId) {

                    return new SearchFlatAdvertisementsCriteria(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            0,
                            25,
                            types,
                            localityId,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
                }
            }

            @Nested
            @ClearDatabase
            @DisableConstraints
            final class PaginationTests {

                @Test
                @DisplayName("Should find all")
                void shouldFindAll() {
                    // Given
                    final UUID localityId = getId();
                    final int page = 0;
                    final int pageSize = 25;

                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(page, pageSize, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    final FlatAdvertisementEntity secondEntity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F2",
                                    getTitle() + "F2",
                                    getDescription() + "F2",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);
                    repository.save(secondEntity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
                    Assertions.assertThat(result.getNumber()).isEqualTo(page);
                    Assertions.assertThat(result.getSize()).isEqualTo(pageSize);
                    Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
                    Assertions.assertThat(result.hasNext()).isFalse();
                    Assertions.assertThat(result.isFirst()).isTrue();
                }

                @Test
                @DisplayName("Should find with more pages")
                void shouldFindWithMorePages() {
                    // Given
                    final UUID localityId = getId();
                    final int page = 0;
                    final int pageSize = 1;

                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(page, pageSize, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    final FlatAdvertisementEntity secondEntity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F2",
                                    getTitle() + "F2",
                                    getDescription() + "F2",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);
                    repository.save(secondEntity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
                    Assertions.assertThat(result.getNumber()).isEqualTo(page);
                    Assertions.assertThat(result.getSize()).isEqualTo(pageSize);
                    Assertions.assertThat(result.getTotalPages()).isEqualTo(2);
                    Assertions.assertThat(result.hasNext()).isTrue();
                    Assertions.assertThat(result.isFirst()).isTrue();
                }

                @Test
                @DisplayName("Should find with last page")
                void shouldFindWithLastPage() {
                    // Given
                    final UUID localityId = getId();
                    final int page = 1;
                    final int pageSize = 1;

                    final SearchFlatAdvertisementsCriteria criteria =
                            getCriteria(page, pageSize, localityId);

                    final FlatAdvertisementEntity entity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    final FlatAdvertisementEntity secondEntity =
                            new FlatAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F2",
                                    getTitle() + "F2",
                                    getDescription() + "F2",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
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

                    repository.save(entity);
                    repository.save(secondEntity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
                    Assertions.assertThat(result.getNumber()).isEqualTo(page);
                    Assertions.assertThat(result.getSize()).isEqualTo(pageSize);
                    Assertions.assertThat(result.getTotalPages()).isEqualTo(2);
                    Assertions.assertThat(result.hasNext()).isFalse();
                    Assertions.assertThat(result.isFirst()).isFalse();
                }

                private static SearchFlatAdvertisementsCriteria getCriteria(
                        final int page, final int pageSize, final UUID localityId) {

                    return new SearchFlatAdvertisementsCriteria(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            page,
                            pageSize,
                            emptySet(),
                            localityId,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
                }
            }

            @Nested
            @ClearDatabase
            @DisableConstraints
            final class CommercialTests {

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class TypeOfMarketsTests {

                    @Test
                    @DisplayName("Should find when criteria types are empty")
                    void shouldFindWhenCriteriaTypesAreEmpty() {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(emptySet(), localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @Test
                    @DisplayName("Should find by type")
                    void shouldFindByType() {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(Set.of(TypeOfMarket.PRIMARY.name()), localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @Test
                    @DisplayName("Should find by multiple types")
                    void shouldFindByMultipleTypes() {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(
                                        Set.of(
                                                TypeOfMarket.PRIMARY.name(),
                                                TypeOfMarket.SECONDARY.name()),
                                        localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        final CommercialAdvertisementEntity secondEntity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F2",
                                        getTitle() + "F2",
                                        getDescription() + "F2",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
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
                                        TypeOfMarket.SECONDARY);

                        repository.save(entity);
                        repository.save(secondEntity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(2)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactlyInAnyOrder(entity.getId(), secondEntity.getId());
                    }

                    private static SearchCommercialAdvertisementsCriteria getCriteria(
                            final Set<String> typeOfMarkets, final UUID localityId) {

                        return new SearchCommercialAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                25,
                                emptySet(),
                                localityId,
                                null,
                                null,
                                typeOfMarkets,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class FloorTests {

                    @Test
                    @DisplayName("Should find when criteria floor is null")
                    void shouldFindWhenCriteriaFloorIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by floorFrom")
                    @MethodSource("shouldFindByFloorFromDataProvider")
                    void shouldFindByFloorFrom(final Integer floorFrom, final Integer floor) {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(floorFrom, null, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        floor,
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by floorTo")
                    @MethodSource("shouldFindByFloorToDataProvider")
                    void shouldFindByFloorTo(final Integer floorTo, final Integer floor) {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(null, floorTo, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        floor,
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByFloorFromTo")
                    @DisplayName("Should find by floorFromTo")
                    void shouldFindByFloorFromTo(
                            final Integer floorFrom, final Integer floorTo, final Integer floor) {

                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(floorFrom, floorTo, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.HALL,
                                        getNumberOfRooms(),
                                        floor,
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByFloorFromTo() {
                        return Stream.of(
                                Arguments.of(2, 2, 2),
                                Arguments.of(1, 2, 1),
                                Arguments.of(1, 5, 5),
                                Arguments.of(1, 5, 3));
                    }

                    private static Stream<Arguments> shouldFindByFloorFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2, 2), Arguments.of(2, 2), Arguments.of(5, 6));
                    }

                    private static Stream<Arguments> shouldFindByFloorToDataProvider() {
                        return Stream.of(
                                Arguments.of(5, 5), Arguments.of(5, 3), Arguments.of(2, 1));
                    }

                    private static SearchCommercialAdvertisementsCriteria getCriteria(
                            final Integer floorFrom, final Integer floorTo, final UUID localityId) {

                        return new SearchCommercialAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                floorFrom,
                                floorTo,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class FloorsTests {

                    @Test
                    @DisplayName("Should find when criteria floors is null")
                    void shouldFindWhenCriteriaFloorsIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by floorsFrom")
                    @MethodSource("shouldFindByFloorsFromDataProvider")
                    void shouldFindByFloorsFrom(final Integer floorsFrom, final Integer floors) {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(floorsFrom, null, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        null,
                                        floors,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by floorsTo")
                    @MethodSource("shouldFindByFloorsToDataProvider")
                    void shouldFindByFloorsTo(final Integer floorsTo, final Integer floors) {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(null, floorsTo, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        null,
                                        floors,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByFloorsFromTo")
                    @DisplayName("Should find by floorsFromTo")
                    void shouldFindByFloorsFromTo(
                            final Integer floorsFrom,
                            final Integer floorsTo,
                            final Integer floors) {

                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(floorsFrom, floorsTo, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.HALL,
                                        getNumberOfRooms(),
                                        null,
                                        floors,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByFloorsFromTo() {
                        return Stream.of(
                                Arguments.of(2, 2, 2),
                                Arguments.of(1, 2, 1),
                                Arguments.of(1, 5, 5),
                                Arguments.of(1, 5, 3));
                    }

                    private static Stream<Arguments> shouldFindByFloorsFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2, 2), Arguments.of(2, 2), Arguments.of(5, 6));
                    }

                    private static Stream<Arguments> shouldFindByFloorsToDataProvider() {
                        return Stream.of(
                                Arguments.of(5, 5), Arguments.of(5, 3), Arguments.of(2, 1));
                    }

                    private static SearchCommercialAdvertisementsCriteria getCriteria(
                            final Integer floorsFrom,
                            final Integer floorsTo,
                            final UUID localityId) {

                        return new SearchCommercialAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                floorsFrom,
                                floorsTo,
                                null,
                                null,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class NumberOfRoomsTests {

                    @Test
                    @DisplayName("Should find when criteria numberOfRooms is null")
                    void shouldFindWhenCriteriaNumberOfRoomsIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by numberOfRoomsFrom")
                    @MethodSource("shouldFindByNumberOfRoomsFromDataProvider")
                    void shouldFindByNumberOfRoomsFrom(
                            final Integer numberOfRoomsFrom, final Integer numberOfRooms) {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(numberOfRoomsFrom, null, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        numberOfRooms,
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by numberOfRoomsTo")
                    @MethodSource("shouldFindByNumberOfRoomsToDataProvider")
                    void shouldFindByNumberOfRoomsTo(
                            final Integer numberOfRoomsTo, final Integer numberOfRooms) {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(null, numberOfRoomsTo, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        numberOfRooms,
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByNumberOfRoomsFromTo")
                    @DisplayName("Should find by numberOfRoomsFromTo")
                    void shouldFindByNumberOfRoomsFromTo(
                            final Integer numberOfRoomsFrom,
                            final Integer numberOfRoomsTo,
                            final Integer numberOfRooms) {

                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(numberOfRoomsFrom, numberOfRoomsTo, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        numberOfRooms,
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByNumberOfRoomsFromTo() {
                        return Stream.of(
                                Arguments.of(2, 2, 2),
                                Arguments.of(1, 2, 1),
                                Arguments.of(1, 5, 5),
                                Arguments.of(1, 5, 3));
                    }

                    private static Stream<Arguments> shouldFindByNumberOfRoomsFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2, 2), Arguments.of(2, 3), Arguments.of(5, 6));
                    }

                    private static Stream<Arguments> shouldFindByNumberOfRoomsToDataProvider() {
                        return Stream.of(
                                Arguments.of(5, 5), Arguments.of(5, 3), Arguments.of(2, 1));
                    }

                    private static SearchCommercialAdvertisementsCriteria getCriteria(
                            final Integer numberOfRoomsFrom,
                            final Integer numberOfRoomsTo,
                            final UUID localityId) {

                        return new SearchCommercialAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                numberOfRoomsFrom,
                                numberOfRoomsTo,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class BuiltYearTests {

                    @Test
                    @DisplayName("Should find when criteria builtYear is null")
                    void shouldFindWhenCriteriaBuiltYearIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by builtYearFrom")
                    @MethodSource("shouldFindByBuiltYearFromDataProvider")
                    void shouldFindByBuiltYearFrom(
                            final Integer builtYearFrom, final Integer builtYear) {

                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(builtYearFrom, null, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        builtYear,
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by builtYearTo")
                    @MethodSource("shouldFindByBuiltYearToDataProvider")
                    void shouldFindByBuiltYearTo(
                            final Integer builtYearTo, final Integer builtYear) {
                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(null, builtYearTo, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        builtYear,
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByBuiltYearFromTo")
                    @DisplayName("Should find by builtYearFromTo")
                    void shouldFindByBuiltYearFromTo(
                            final Integer builtYearFrom,
                            final Integer builtYearTo,
                            final Integer builtYear) {

                        // Given
                        final UUID localityId = getId();
                        final SearchCommercialAdvertisementsCriteria criteria =
                                getCriteria(builtYearFrom, builtYearTo, localityId);

                        final CommercialAdvertisementEntity entity =
                                new CommercialAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        CommercialBuildingType.WAREHOUSE,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        builtYear,
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByBuiltYearFromTo() {
                        return Stream.of(
                                Arguments.of(2000, 2000, 2000),
                                Arguments.of(2001, 2002, 2001),
                                Arguments.of(2001, 2005, 2005),
                                Arguments.of(2001, 2005, 2003));
                    }

                    private static Stream<Arguments> shouldFindByBuiltYearFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2002, 2002),
                                Arguments.of(2001, 2002),
                                Arguments.of(2005, 2006));
                    }

                    private static Stream<Arguments> shouldFindByBuiltYearToDataProvider() {
                        return Stream.of(
                                Arguments.of(2005, 2005),
                                Arguments.of(2005, 2003),
                                Arguments.of(2002, 2001));
                    }

                    private static SearchCommercialAdvertisementsCriteria getCriteria(
                            final Integer builtYearFrom,
                            final Integer builtYearTo,
                            final UUID localityId) {

                        return new SearchCommercialAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                builtYearFrom,
                                builtYearTo);
                    }
                }
            }

            @Nested
            @ClearDatabase
            @DisableConstraints
            final class FlatTests {

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class TypeOfMarketsTests {

                    @Test
                    @DisplayName("Should find when criteria types are empty")
                    void shouldFindWhenCriteriaTypesAreEmpty() {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(emptySet(), localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @Test
                    @DisplayName("Should find by type")
                    void shouldFindByType() {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(Set.of(TypeOfMarket.PRIMARY.name()), localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @Test
                    @DisplayName("Should find by multiple types")
                    void shouldFindByMultipleTypes() {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(
                                        Set.of(
                                                TypeOfMarket.PRIMARY.name(),
                                                TypeOfMarket.SECONDARY.name()),
                                        localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        final FlatAdvertisementEntity secondEntity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F2",
                                        getTitle() + "F2",
                                        getDescription() + "F2",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
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
                                        TypeOfMarket.SECONDARY);

                        repository.save(entity);
                        repository.save(secondEntity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(2)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactlyInAnyOrder(entity.getId(), secondEntity.getId());
                    }

                    private static SearchFlatAdvertisementsCriteria getCriteria(
                            final Set<String> typeOfMarkets, final UUID localityId) {

                        return new SearchFlatAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                25,
                                emptySet(),
                                localityId,
                                null,
                                null,
                                typeOfMarkets,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class FloorTests {

                    @Test
                    @DisplayName("Should find when criteria floor is null")
                    void shouldFindWhenCriteriaFloorIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by floorFrom")
                    @MethodSource("shouldFindByFloorFromDataProvider")
                    void shouldFindByFloorFrom(final Integer floorFrom, final Integer floor) {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(floorFrom, null, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        floor,
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by floorTo")
                    @MethodSource("shouldFindByFloorToDataProvider")
                    void shouldFindByFloorTo(final Integer floorTo, final Integer floor) {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(null, floorTo, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        floor,
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByFloorFromTo")
                    @DisplayName("Should find by floorFromTo")
                    void shouldFindByFloorFromTo(
                            final Integer floorFrom, final Integer floorTo, final Integer floor) {

                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(floorFrom, floorTo, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.APARTMENT,
                                        getNumberOfRooms(),
                                        floor,
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByFloorFromTo() {
                        return Stream.of(
                                Arguments.of(2, 2, 2),
                                Arguments.of(1, 2, 1),
                                Arguments.of(1, 5, 5),
                                Arguments.of(1, 5, 3));
                    }

                    private static Stream<Arguments> shouldFindByFloorFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2, 2), Arguments.of(2, 2), Arguments.of(5, 6));
                    }

                    private static Stream<Arguments> shouldFindByFloorToDataProvider() {
                        return Stream.of(
                                Arguments.of(5, 5), Arguments.of(5, 3), Arguments.of(2, 1));
                    }

                    private static SearchFlatAdvertisementsCriteria getCriteria(
                            final Integer floorFrom, final Integer floorTo, final UUID localityId) {

                        return new SearchFlatAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                floorFrom,
                                floorTo,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class FloorsTests {

                    @Test
                    @DisplayName("Should find when criteria floors is null")
                    void shouldFindWhenCriteriaFloorsIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by floorsFrom")
                    @MethodSource("shouldFindByFloorsFromDataProvider")
                    void shouldFindByFloorsFrom(final Integer floorsFrom, final Integer floors) {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(floorsFrom, null, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        null,
                                        floors,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by floorsTo")
                    @MethodSource("shouldFindByFloorsToDataProvider")
                    void shouldFindByFloorsTo(final Integer floorsTo, final Integer floors) {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(null, floorsTo, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        null,
                                        floors,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByFloorsFromTo")
                    @DisplayName("Should find by floorsFromTo")
                    void shouldFindByFloorsFromTo(
                            final Integer floorsFrom,
                            final Integer floorsTo,
                            final Integer floors) {

                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(floorsFrom, floorsTo, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.APARTMENT,
                                        getNumberOfRooms(),
                                        null,
                                        floors,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByFloorsFromTo() {
                        return Stream.of(
                                Arguments.of(2, 2, 2),
                                Arguments.of(1, 2, 1),
                                Arguments.of(1, 5, 5),
                                Arguments.of(1, 5, 3));
                    }

                    private static Stream<Arguments> shouldFindByFloorsFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2, 2), Arguments.of(2, 2), Arguments.of(5, 6));
                    }

                    private static Stream<Arguments> shouldFindByFloorsToDataProvider() {
                        return Stream.of(
                                Arguments.of(5, 5), Arguments.of(5, 3), Arguments.of(2, 1));
                    }

                    private static SearchFlatAdvertisementsCriteria getCriteria(
                            final Integer floorsFrom,
                            final Integer floorsTo,
                            final UUID localityId) {

                        return new SearchFlatAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                floorsFrom,
                                floorsTo,
                                null,
                                null,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class NumberOfRoomsTests {

                    @Test
                    @DisplayName("Should find when criteria numberOfRooms is null")
                    void shouldFindWhenCriteriaNumberOfRoomsIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by numberOfRoomsFrom")
                    @MethodSource("shouldFindByNumberOfRoomsFromDataProvider")
                    void shouldFindByNumberOfRoomsFrom(
                            final Integer numberOfRoomsFrom, final Integer numberOfRooms) {

                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(numberOfRoomsFrom, null, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        numberOfRooms,
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by numberOfRoomsTo")
                    @MethodSource("shouldFindByNumberOfRoomsToDataProvider")
                    void shouldFindByNumberOfRoomsTo(
                            final Integer numberOfRoomsTo, final Integer numberOfRooms) {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(null, numberOfRoomsTo, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        numberOfRooms,
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByNumberOfRoomsFromTo")
                    @DisplayName("Should find by numberOfRoomsFromTo")
                    void shouldFindByNumberOfRoomsFromTo(
                            final Integer numberOfRoomsFrom,
                            final Integer numberOfRoomsTo,
                            final Integer numberOfRooms) {

                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(numberOfRoomsFrom, numberOfRoomsTo, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        numberOfRooms,
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByNumberOfRoomsFromTo() {
                        return Stream.of(
                                Arguments.of(2, 2, 2),
                                Arguments.of(1, 2, 1),
                                Arguments.of(1, 5, 5),
                                Arguments.of(1, 5, 3));
                    }

                    private static Stream<Arguments> shouldFindByNumberOfRoomsFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2, 2), Arguments.of(2, 3), Arguments.of(5, 6));
                    }

                    private static Stream<Arguments> shouldFindByNumberOfRoomsToDataProvider() {
                        return Stream.of(
                                Arguments.of(5, 5), Arguments.of(5, 3), Arguments.of(2, 1));
                    }

                    private static SearchFlatAdvertisementsCriteria getCriteria(
                            final Integer numberOfRoomsFrom,
                            final Integer numberOfRoomsTo,
                            final UUID localityId) {

                        return new SearchFlatAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                numberOfRoomsFrom,
                                numberOfRoomsTo,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class BuiltYearTests {

                    @Test
                    @DisplayName("Should find when criteria builtYear is null")
                    void shouldFindWhenCriteriaBuiltYearIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by builtYearFrom")
                    @MethodSource("shouldFindByBuiltYearFromDataProvider")
                    void shouldFindByBuiltYearFrom(
                            final Integer builtYearFrom, final Integer builtYear) {

                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(builtYearFrom, null, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        builtYear,
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by builtYearTo")
                    @MethodSource("shouldFindByBuiltYearToDataProvider")
                    void shouldFindByBuiltYearTo(
                            final Integer builtYearTo, final Integer builtYear) {
                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(null, builtYearTo, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        builtYear,
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByBuiltYearFromTo")
                    @DisplayName("Should find by builtYearFromTo")
                    void shouldFindByBuiltYearFromTo(
                            final Integer builtYearFrom,
                            final Integer builtYearTo,
                            final Integer builtYear) {

                        // Given
                        final UUID localityId = getId();
                        final SearchFlatAdvertisementsCriteria criteria =
                                getCriteria(builtYearFrom, builtYearTo, localityId);

                        final FlatAdvertisementEntity entity =
                                new FlatAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        FlatBuildingType.LOFT,
                                        getNumberOfRooms(),
                                        getFloor(),
                                        getFloors(),
                                        builtYear,
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByBuiltYearFromTo() {
                        return Stream.of(
                                Arguments.of(2000, 2000, 2000),
                                Arguments.of(2001, 2002, 2001),
                                Arguments.of(2001, 2005, 2005),
                                Arguments.of(2001, 2005, 2003));
                    }

                    private static Stream<Arguments> shouldFindByBuiltYearFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2002, 2002),
                                Arguments.of(2001, 2002),
                                Arguments.of(2005, 2006));
                    }

                    private static Stream<Arguments> shouldFindByBuiltYearToDataProvider() {
                        return Stream.of(
                                Arguments.of(2005, 2005),
                                Arguments.of(2005, 2003),
                                Arguments.of(2002, 2001));
                    }

                    private static SearchFlatAdvertisementsCriteria getCriteria(
                            final Integer builtYearFrom,
                            final Integer builtYearTo,
                            final UUID localityId) {

                        return new SearchFlatAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                builtYearFrom,
                                builtYearTo);
                    }
                }
            }

            @Nested
            @ClearDatabase
            @DisableConstraints
            final class HouseTests {

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class TypeOfMarketsTests {

                    @Test
                    @DisplayName("Should find when criteria types are empty")
                    void shouldFindWhenCriteriaTypesAreEmpty() {
                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(emptySet(), localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.SEMI_DETACHED,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @Test
                    @DisplayName("Should find by type")
                    void shouldFindByType() {
                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(Set.of(TypeOfMarket.PRIMARY.name()), localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.MANSION,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @Test
                    @DisplayName("Should find by multiple types")
                    void shouldFindByMultipleTypes() {
                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(
                                        Set.of(
                                                TypeOfMarket.PRIMARY.name(),
                                                TypeOfMarket.SECONDARY.name()),
                                        localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.TERRACED,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        final HouseAdvertisementEntity secondEntity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F2",
                                        getTitle() + "F2",
                                        getDescription() + "F2",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.MANSION,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.SECONDARY);

                        repository.save(entity);
                        repository.save(secondEntity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(2)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactlyInAnyOrder(entity.getId(), secondEntity.getId());
                    }

                    private static SearchHouseAdvertisementsCriteria getCriteria(
                            final Set<String> typeOfMarkets, final UUID localityId) {

                        return new SearchHouseAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                25,
                                emptySet(),
                                localityId,
                                null,
                                null,
                                typeOfMarkets,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class FloorsTests {

                    @Test
                    @DisplayName("Should find when criteria floors is null")
                    void shouldFindWhenCriteriaFloorsIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.DETACHED,
                                        getNumberOfRooms(),
                                        null,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by floorsFrom")
                    @MethodSource("shouldFindByFloorsFromDataProvider")
                    void shouldFindByFloorsFrom(final Integer floorsFrom, final Integer floors) {
                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(floorsFrom, null, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.TERRACED,
                                        getNumberOfRooms(),
                                        floors,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by floorsTo")
                    @MethodSource("shouldFindByFloorsToDataProvider")
                    void shouldFindByFloorsTo(final Integer floorsTo, final Integer floors) {
                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(null, floorsTo, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.SEMI_DETACHED,
                                        getNumberOfRooms(),
                                        floors,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByFloorsFromTo")
                    @DisplayName("Should find by floorsFromTo")
                    void shouldFindByFloorsFromTo(
                            final Integer floorsFrom,
                            final Integer floorsTo,
                            final Integer floors) {

                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(floorsFrom, floorsTo, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.MANSION,
                                        getNumberOfRooms(),
                                        floors,
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByFloorsFromTo() {
                        return Stream.of(
                                Arguments.of(2, 2, 2),
                                Arguments.of(1, 2, 1),
                                Arguments.of(1, 5, 5),
                                Arguments.of(1, 5, 3));
                    }

                    private static Stream<Arguments> shouldFindByFloorsFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2, 2), Arguments.of(2, 2), Arguments.of(5, 6));
                    }

                    private static Stream<Arguments> shouldFindByFloorsToDataProvider() {
                        return Stream.of(
                                Arguments.of(5, 5), Arguments.of(5, 3), Arguments.of(2, 1));
                    }

                    private static SearchHouseAdvertisementsCriteria getCriteria(
                            final Integer floorsFrom,
                            final Integer floorsTo,
                            final UUID localityId) {

                        return new SearchHouseAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                floorsFrom,
                                floorsTo,
                                null,
                                null,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class NumberOfRoomsTests {

                    @Test
                    @DisplayName("Should find when criteria numberOfRooms is null")
                    void shouldFindWhenCriteriaNumberOfRoomsIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.MANSION,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by numberOfRoomsFrom")
                    @MethodSource("shouldFindByNumberOfRoomsFromDataProvider")
                    void shouldFindByNumberOfRoomsFrom(
                            final Integer numberOfRoomsFrom, final Integer numberOfRooms) {

                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(numberOfRoomsFrom, null, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.SEMI_DETACHED,
                                        numberOfRooms,
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by numberOfRoomsTo")
                    @MethodSource("shouldFindByNumberOfRoomsToDataProvider")
                    void shouldFindByNumberOfRoomsTo(
                            final Integer numberOfRoomsTo, final Integer numberOfRooms) {
                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(null, numberOfRoomsTo, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.MANSION,
                                        numberOfRooms,
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByNumberOfRoomsFromTo")
                    @DisplayName("Should find by numberOfRoomsFromTo")
                    void shouldFindByNumberOfRoomsFromTo(
                            final Integer numberOfRoomsFrom,
                            final Integer numberOfRoomsTo,
                            final Integer numberOfRooms) {

                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(numberOfRoomsFrom, numberOfRoomsTo, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.SEMI_DETACHED,
                                        numberOfRooms,
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByNumberOfRoomsFromTo() {
                        return Stream.of(
                                Arguments.of(2, 2, 2),
                                Arguments.of(1, 2, 1),
                                Arguments.of(1, 5, 5),
                                Arguments.of(1, 5, 3));
                    }

                    private static Stream<Arguments> shouldFindByNumberOfRoomsFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2, 2), Arguments.of(2, 3), Arguments.of(5, 6));
                    }

                    private static Stream<Arguments> shouldFindByNumberOfRoomsToDataProvider() {
                        return Stream.of(
                                Arguments.of(5, 5), Arguments.of(5, 3), Arguments.of(2, 1));
                    }

                    private static SearchHouseAdvertisementsCriteria getCriteria(
                            final Integer numberOfRoomsFrom,
                            final Integer numberOfRoomsTo,
                            final UUID localityId) {

                        return new SearchHouseAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                numberOfRoomsFrom,
                                numberOfRoomsTo,
                                null,
                                null);
                    }
                }

                @Nested
                @ClearDatabase
                @DisableConstraints
                final class BuiltYearTests {

                    @Test
                    @DisplayName("Should find when criteria builtYear is null")
                    void shouldFindWhenCriteriaBuiltYearIsNull() {
                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(null, null, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.SEMI_DETACHED,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        getBuiltYear(),
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by builtYearFrom")
                    @MethodSource("shouldFindByBuiltYearFromDataProvider")
                    void shouldFindByBuiltYearFrom(
                            final Integer builtYearFrom, final Integer builtYear) {

                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(builtYearFrom, null, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.MANSION,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        builtYear,
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @DisplayName("Should find by builtYearTo")
                    @MethodSource("shouldFindByBuiltYearToDataProvider")
                    void shouldFindByBuiltYearTo(
                            final Integer builtYearTo, final Integer builtYear) {
                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(null, builtYearTo, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.DETACHED,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        builtYear,
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    @ParameterizedTest
                    @MethodSource("shouldFindByBuiltYearFromTo")
                    @DisplayName("Should find by builtYearFromTo")
                    void shouldFindByBuiltYearFromTo(
                            final Integer builtYearFrom,
                            final Integer builtYearTo,
                            final Integer builtYear) {

                        // Given
                        final UUID localityId = getId();
                        final SearchHouseAdvertisementsCriteria criteria =
                                getCriteria(builtYearFrom, builtYearTo, localityId);

                        final HouseAdvertisementEntity entity =
                                new HouseAdvertisementEntity(
                                        getId(),
                                        getSlug() + "F",
                                        getTitle() + "F",
                                        getDescription() + "F",
                                        getPrice(),
                                        getArea(),
                                        getPricePerSquareMeter(),
                                        localityId,
                                        getId(),
                                        true,
                                        AdvertisementStatus.ACTIVE,
                                        emptySet(),
                                        emptySet(),
                                        HouseBuildingType.MANSION,
                                        getNumberOfRooms(),
                                        getFloors(),
                                        builtYear,
                                        TypeOfMarket.PRIMARY);

                        repository.save(entity);

                        // When
                        final var result = repository.findByCriteria(criteria);

                        // Then
                        Assertions.assertThat(result.getContent())
                                .hasSize(1)
                                .extracting(AdvertisementCardProjection::getId)
                                .containsExactly(entity.getId());
                    }

                    private static Stream<Arguments> shouldFindByBuiltYearFromTo() {
                        return Stream.of(
                                Arguments.of(2000, 2000, 2000),
                                Arguments.of(2001, 2002, 2001),
                                Arguments.of(2001, 2005, 2005),
                                Arguments.of(2001, 2005, 2003));
                    }

                    private static Stream<Arguments> shouldFindByBuiltYearFromDataProvider() {
                        return Stream.of(
                                Arguments.of(2002, 2002),
                                Arguments.of(2001, 2002),
                                Arguments.of(2005, 2006));
                    }

                    private static Stream<Arguments> shouldFindByBuiltYearToDataProvider() {
                        return Stream.of(
                                Arguments.of(2005, 2005),
                                Arguments.of(2005, 2003),
                                Arguments.of(2002, 2001));
                    }

                    private static SearchHouseAdvertisementsCriteria getCriteria(
                            final Integer builtYearFrom,
                            final Integer builtYearTo,
                            final UUID localityId) {

                        return new SearchHouseAdvertisementsCriteria(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                1,
                                null,
                                localityId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                builtYearFrom,
                                builtYearTo);
                    }
                }
            }

            @Nested
            @ClearDatabase
            @DisableConstraints
            final class PlotTests {

                @Test
                @DisplayName("Should find when criteria types are empty")
                void shouldFindWhenCriteriaTypesAreEmpty() {
                    // Given
                    final UUID localityId = getId();
                    final SearchPlotAdvertisementsCriteria criteria =
                            getCriteria(emptySet(), localityId);

                    final PlotAdvertisementEntity entity =
                            new PlotAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
                                    getId(),
                                    true,
                                    AdvertisementStatus.ACTIVE,
                                    emptySet(),
                                    emptySet(),
                                    PlotBuildingType.FOREST);

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @Test
                @DisplayName("Should find by type")
                void shouldFindByType() {
                    // Given
                    final UUID localityId = getId();
                    final SearchPlotAdvertisementsCriteria criteria =
                            getCriteria(Set.of(PlotBuildingType.CONSTRUCTION.name()), localityId);

                    final PlotAdvertisementEntity entity =
                            new PlotAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
                                    getId(),
                                    true,
                                    AdvertisementStatus.ACTIVE,
                                    emptySet(),
                                    emptySet(),
                                    PlotBuildingType.CONSTRUCTION);

                    repository.save(entity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(1)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactly(entity.getId());
                }

                @Test
                @DisplayName("Should find by multiple types")
                void shouldFindByMultipleTypes() {
                    // Given
                    final UUID localityId = getId();
                    final SearchPlotAdvertisementsCriteria criteria =
                            getCriteria(
                                    Set.of(
                                            PlotBuildingType.AGRICULTURAL.name(),
                                            PlotBuildingType.FOREST.name()),
                                    localityId);

                    final PlotAdvertisementEntity entity =
                            new PlotAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F",
                                    getTitle() + "F",
                                    getDescription() + "F",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
                                    getId(),
                                    true,
                                    AdvertisementStatus.ACTIVE,
                                    emptySet(),
                                    emptySet(),
                                    PlotBuildingType.FOREST);

                    final PlotAdvertisementEntity secondEntity =
                            new PlotAdvertisementEntity(
                                    getId(),
                                    getSlug() + "F2",
                                    getTitle() + "F2",
                                    getDescription() + "F2",
                                    getPrice(),
                                    getArea(),
                                    getPricePerSquareMeter(),
                                    localityId,
                                    getId(),
                                    true,
                                    AdvertisementStatus.ACTIVE,
                                    emptySet(),
                                    emptySet(),
                                    PlotBuildingType.AGRICULTURAL);

                    repository.save(entity);
                    repository.save(secondEntity);

                    // When
                    final var result = repository.findByCriteria(criteria);

                    // Then
                    Assertions.assertThat(result.getContent())
                            .hasSize(2)
                            .extracting(AdvertisementCardProjection::getId)
                            .containsExactlyInAnyOrder(entity.getId(), secondEntity.getId());
                }

                private static SearchPlotAdvertisementsCriteria getCriteria(
                        final Set<String> types, final UUID localityId) {

                    return new SearchPlotAdvertisementsCriteria(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            0,
                            25,
                            localityId,
                            null,
                            null,
                            types);
                }
            }
        }
    }

    @Nested
    final class ClearClaimsTests {

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
            @DisplayName("Should clear flat claims")
            void shouldClearFlatClaims() {
                // Given
                final Identifier id = Identifier.generate();

                // When
                advertisementJpaRepository.clearFlatClaims(id);

                // Then
                verify(flatAdvertisementClaimJpaRepository).deleteByAdvertisementId(id.getValue());
            }

            @Test
            @DisplayName("Should clear house claims")
            void shouldClearHouseClaims() {
                // Given
                final Identifier id = Identifier.generate();

                // When
                advertisementJpaRepository.clearHouseClaims(id);

                // Then
                verify(houseAdvertisementClaimJpaRepository).deleteByAdvertisementId(id.getValue());
            }

            @Test
            @DisplayName("Should clear commercial claims")
            void shouldClearCommercialClaims() {
                // Given
                final Identifier id = Identifier.generate();

                // When
                advertisementJpaRepository.clearCommercialClaims(id);

                // Then
                verify(commercialAdvertisementClaimJpaRepository)
                        .deleteByAdvertisementId(id.getValue());
            }

            @Test
            @DisplayName("Should clear plot claims")
            void shouldClearPlotClaims() {
                // Given
                final Identifier id = Identifier.generate();

                // When
                advertisementJpaRepository.clearPlotClaims(id);

                // Then
                verify(plotAdvertisementClaimJpaRepository).deleteByAdvertisementId(id.getValue());
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
