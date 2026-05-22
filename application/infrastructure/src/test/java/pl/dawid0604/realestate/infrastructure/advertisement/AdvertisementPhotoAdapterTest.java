/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import static java.util.Collections.emptySet;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;
import pl.dawid0604.realestate.infrastructure.ClearDatabase;
import pl.dawid0604.realestate.infrastructure.IntegrationTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

class AdvertisementPhotoAdapterTest {

    @Nested
    @ExtendWith(MockitoExtension.class)
    final class UnitTests {
        @Mock private FlatAdvertisementPhotoJpaRepository flatJpaRepository;
        @Mock private HouseAdvertisementPhotoJpaRepository houseJpaRepository;
        @Mock private CommercialAdvertisementPhotoJpaRepository commercialJpaRepository;
        @Mock private PlotAdvertisementPhotoJpaRepository plotJpaRepository;
        private AdvertisementPhotoAdapter adapter;

        @BeforeEach
        void setUp() {
            this.adapter =
                    new AdvertisementPhotoAdapter(
                            flatJpaRepository,
                            houseJpaRepository,
                            commercialJpaRepository,
                            plotJpaRepository);
        }

        @Test
        @DisplayName("Should throw exception when ids are null")
        void shouldThrowExceptionWhenIdsAreNull() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(
                            () -> adapter.findPhotosInBatch(null, AdvertisementType.FLAT))
                    .isExactlyInstanceOf(NullPointerException.class)
                    .hasMessage("Ids cannot be null");
        }
    }

    @Nested
    @ClearDatabase
    @DisableConstraints
    final class IntegrationTests extends IntegrationTest {
        @Autowired private AdvertisementPhotoAdapter repository;
        @Autowired private FlatAdvertisementJpaRepository flatAdvertisementJpaRepository;
        @Autowired private HouseAdvertisementJpaRepository houseAdvertisementJpaRepository;

        @Autowired
        private CommercialAdvertisementJpaRepository commercialAdvertisementJpaRepository;

        @Autowired private PlotAdvertisementJpaRepository plotAdvertisementJpaRepository;

        @Test
        @DisplayName("Should find flat photos")
        void shouldFindFlatPhotos() {
            // Given
            final Set<FlatAdvertisementPhotoEntity> firstEntityPhotos =
                    Set.of(
                            new FlatAdvertisementPhotoEntity(getId(), 0, "https://anyurl.com/1"),
                            new FlatAdvertisementPhotoEntity(getId(), 1, "https://anyurl.com/2"));

            final Set<FlatAdvertisementPhotoEntity> secondEntityPhotos =
                    Set.of(new FlatAdvertisementPhotoEntity(getId(), 0, "https://anyurl.com/3"));

            final FlatAdvertisementEntity firstEntity =
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
                            emptySet(),
                            firstEntityPhotos,
                            FlatBuildingType.APARTMENT,
                            null,
                            null,
                            null,
                            null,
                            TypeOfMarket.PRIMARY);

            final FlatAdvertisementEntity secondEntity =
                    new FlatAdvertisementEntity(
                            getId(),
                            getSlug() + "F2",
                            getTitle(),
                            null,
                            getPrice(),
                            getArea(),
                            getPricePerSquareMeter(),
                            getId(),
                            getId(),
                            true,
                            AdvertisementStatus.ACTIVE,
                            emptySet(),
                            secondEntityPhotos,
                            FlatBuildingType.APARTMENT,
                            null,
                            null,
                            null,
                            null,
                            TypeOfMarket.PRIMARY);

            final FlatAdvertisementEntity thirdEntity =
                    new FlatAdvertisementEntity(
                            getId(),
                            getSlug() + "F3",
                            getTitle(),
                            null,
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
                            null,
                            null,
                            null,
                            null,
                            TypeOfMarket.PRIMARY);

            firstEntityPhotos.forEach(c -> c.setAdvertisement(firstEntity));
            secondEntityPhotos.forEach(c -> c.setAdvertisement(secondEntity));

            flatAdvertisementJpaRepository.save(firstEntity);
            flatAdvertisementJpaRepository.save(secondEntity);
            flatAdvertisementJpaRepository.save(thirdEntity);

            // When
            final var result =
                    repository.findPhotosInBatch(
                            List.of(firstEntity.getId(), secondEntity.getId(), thirdEntity.getId()),
                            AdvertisementType.FLAT);

            // Then
            assertPhotos(firstEntity, result);
            assertPhotos(secondEntity, result);
            Assertions.assertThat(result.get(thirdEntity.getId())).isNull();
        }

        @Test
        @DisplayName("Should find commercial photos")
        void shouldFindCommercialPhotos() {
            // Given
            final Set<CommercialAdvertisementPhotoEntity> firstEntityPhotos =
                    Set.of(
                            new CommercialAdvertisementPhotoEntity(
                                    getId(), 0, "https://anyurl.com/1"),
                            new CommercialAdvertisementPhotoEntity(
                                    getId(), 1, "https://anyurl.com/2"));

            final Set<CommercialAdvertisementPhotoEntity> secondEntityPhotos =
                    Set.of(
                            new CommercialAdvertisementPhotoEntity(
                                    getId(), 0, "https://anyurl.com/3"));

            final CommercialAdvertisementEntity firstEntity =
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
                            emptySet(),
                            firstEntityPhotos,
                            CommercialBuildingType.WAREHOUSE,
                            null,
                            null,
                            null,
                            null,
                            TypeOfMarket.PRIMARY);

            final CommercialAdvertisementEntity secondEntity =
                    new CommercialAdvertisementEntity(
                            getId(),
                            getSlug() + "F2",
                            getTitle(),
                            null,
                            getPrice(),
                            getArea(),
                            getPricePerSquareMeter(),
                            getId(),
                            getId(),
                            true,
                            AdvertisementStatus.ACTIVE,
                            emptySet(),
                            secondEntityPhotos,
                            CommercialBuildingType.HALL,
                            null,
                            null,
                            null,
                            null,
                            TypeOfMarket.PRIMARY);

            final CommercialAdvertisementEntity thirdEntity =
                    new CommercialAdvertisementEntity(
                            getId(),
                            getSlug() + "F3",
                            getTitle(),
                            null,
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
                            null,
                            null,
                            null,
                            null,
                            TypeOfMarket.PRIMARY);

            firstEntityPhotos.forEach(c -> c.setAdvertisement(firstEntity));
            secondEntityPhotos.forEach(c -> c.setAdvertisement(secondEntity));

            commercialAdvertisementJpaRepository.save(firstEntity);
            commercialAdvertisementJpaRepository.save(secondEntity);
            commercialAdvertisementJpaRepository.save(thirdEntity);

            // When
            final var result =
                    repository.findPhotosInBatch(
                            List.of(firstEntity.getId(), secondEntity.getId(), thirdEntity.getId()),
                            AdvertisementType.COMMERCIAL);

            // Then
            assertPhotos(firstEntity, result);
            assertPhotos(secondEntity, result);
            Assertions.assertThat(result.get(thirdEntity.getId())).isNull();
        }

        @Test
        @DisplayName("Should find house photos")
        void shouldFindHousePhotos() {
            // Given
            final Set<HouseAdvertisementPhotoEntity> firstEntityPhotos =
                    Set.of(
                            new HouseAdvertisementPhotoEntity(getId(), 0, "https://anyurl.com/1"),
                            new HouseAdvertisementPhotoEntity(getId(), 1, "https://anyurl.com/2"));

            final Set<HouseAdvertisementPhotoEntity> secondEntityPhotos =
                    Set.of(new HouseAdvertisementPhotoEntity(getId(), 0, "https://anyurl.com/3"));

            final HouseAdvertisementEntity firstEntity =
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
                            emptySet(),
                            firstEntityPhotos,
                            HouseBuildingType.DETACHED,
                            null,
                            null,
                            null,
                            TypeOfMarket.PRIMARY);

            final HouseAdvertisementEntity secondEntity =
                    new HouseAdvertisementEntity(
                            getId(),
                            getSlug() + "F2",
                            getTitle(),
                            null,
                            getPrice(),
                            getArea(),
                            getPricePerSquareMeter(),
                            getId(),
                            getId(),
                            true,
                            AdvertisementStatus.ACTIVE,
                            emptySet(),
                            secondEntityPhotos,
                            HouseBuildingType.DETACHED,
                            null,
                            null,
                            null,
                            TypeOfMarket.PRIMARY);

            final HouseAdvertisementEntity thirdEntity =
                    new HouseAdvertisementEntity(
                            getId(),
                            getSlug() + "F3",
                            getTitle(),
                            null,
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
                            null,
                            null,
                            null,
                            TypeOfMarket.PRIMARY);

            firstEntityPhotos.forEach(c -> c.setAdvertisement(firstEntity));
            secondEntityPhotos.forEach(c -> c.setAdvertisement(secondEntity));

            houseAdvertisementJpaRepository.save(firstEntity);
            houseAdvertisementJpaRepository.save(secondEntity);
            houseAdvertisementJpaRepository.save(thirdEntity);

            // When
            final var result =
                    repository.findPhotosInBatch(
                            List.of(firstEntity.getId(), secondEntity.getId(), thirdEntity.getId()),
                            AdvertisementType.HOUSE);

            // Then
            assertPhotos(firstEntity, result);
            assertPhotos(secondEntity, result);
            Assertions.assertThat(result.get(thirdEntity.getId())).isNull();
        }

        @Test
        @DisplayName("Should find plot photos")
        void shouldFindPlotPhotos() {
            // Given
            final Set<PlotAdvertisementPhotoEntity> firstEntityPhotos =
                    Set.of(
                            new PlotAdvertisementPhotoEntity(getId(), 0, "https://anyurl.com/1"),
                            new PlotAdvertisementPhotoEntity(getId(), 1, "https://anyurl.com/2"));

            final Set<PlotAdvertisementPhotoEntity> secondEntityPhotos =
                    Set.of(new PlotAdvertisementPhotoEntity(getId(), 0, "https://anyurl.com/3"));

            final PlotAdvertisementEntity firstEntity =
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
                            emptySet(),
                            firstEntityPhotos,
                            PlotBuildingType.CONSTRUCTION);

            final PlotAdvertisementEntity secondEntity =
                    new PlotAdvertisementEntity(
                            getId(),
                            getSlug() + "F2",
                            getTitle(),
                            null,
                            getPrice(),
                            getArea(),
                            getPricePerSquareMeter(),
                            getId(),
                            getId(),
                            true,
                            AdvertisementStatus.ACTIVE,
                            emptySet(),
                            secondEntityPhotos,
                            PlotBuildingType.AGRICULTURAL);

            final PlotAdvertisementEntity thirdEntity =
                    new PlotAdvertisementEntity(
                            getId(),
                            getSlug() + "F3",
                            getTitle(),
                            null,
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

            firstEntityPhotos.forEach(c -> c.setAdvertisement(firstEntity));
            secondEntityPhotos.forEach(c -> c.setAdvertisement(secondEntity));

            plotAdvertisementJpaRepository.save(firstEntity);
            plotAdvertisementJpaRepository.save(secondEntity);
            plotAdvertisementJpaRepository.save(thirdEntity);

            // When
            final var result =
                    repository.findPhotosInBatch(
                            List.of(firstEntity.getId(), secondEntity.getId(), thirdEntity.getId()),
                            AdvertisementType.PLOT);

            // Then
            assertPhotos(firstEntity, result);
            assertPhotos(secondEntity, result);
            Assertions.assertThat(result.get(thirdEntity.getId())).isNull();
        }

        private static void assertPhotos(
                final AdvertisementEntity<?, ?> entity,
                final Map<UUID, Set<PhotoProjection>> photos) {

            final Tuple[] entityPhotosTuples =
                    entity.getPhotos().stream()
                            .map(
                                    p ->
                                            tuple(
                                                    p.getId(),
                                                    p.getUrl(),
                                                    p.getPosition(),
                                                    p.getAdvertisement().getId()))
                            .toArray(Tuple[]::new);

            Assertions.assertThat(photos.get(entity.getId()))
                    .isNotNull()
                    .extracting(
                            PhotoProjection::getId,
                            PhotoProjection::getUrl,
                            PhotoProjection::getPosition,
                            PhotoProjection::getAdvertisementId)
                    .containsExactlyInAnyOrder(entityPhotosTuples);
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
