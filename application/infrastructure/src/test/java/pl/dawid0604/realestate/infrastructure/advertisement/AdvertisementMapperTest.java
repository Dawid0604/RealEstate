/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementClaim;
import pl.dawid0604.realestate.domain.AdvertisementPhoto;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.CommercialDetails;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.FlatDetails;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.HouseDetails;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.PlotDetails;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AdvertisementMapperTest {
    private AdvertisementMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new AdvertisementMapper();
    }

    @Nested
    final class ToDomainTests {

        @Test
        @DisplayName("Should return null when entity is null")
        void shouldReturnNullWhenEntityIsNull() {
            // Given
            // When
            final var result = mapper.toDomain(null);

            // Then
            Assertions.assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should map flat entity")
        void shouldMapFlatEntity() {
            // Given
            final FlatAdvertisementEntity entity = getFlatEntity();

            // When
            final var result = mapper.toDomain(entity);

            // Then
            Assertions.assertThat(result)
                    .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .returns(entity.getId(), a -> a.getId().getValue())
                    .returns(entity.getSlug(), a -> a.getSlug().getValue())
                    .returns(entity.getTitle(), a -> a.getTitle().value())
                    .returns(entity.getDescription(), a -> a.getDescription().value())
                    .returns(entity.getPrice(), a -> a.getPrice().value())
                    .returns(entity.getArea(), a -> a.getArea().value())
                    .returns(
                            entity.getPricePerSquareMeter(),
                            a -> a.getPricePerSquareMeter().getValue())
                    .returns(entity.getLocalityId(), a -> a.getLocality().id().getValue())
                    .returns(entity.getUserId(), a -> a.getOwner().getValue())
                    .returns(entity.getStatus(), Advertisement::getStatus)
                    .returns(entity.getCreatedAt(), Advertisement::getCreatedAt)
                    .returns(entity.isFeatured(), Advertisement::isFeatured)
                    .satisfies(
                            e ->
                                    Assertions.assertThat(e.getDetails())
                                            .isInstanceOf(FlatDetails.class)
                                            .asInstanceOf(
                                                    InstanceOfAssertFactories.type(
                                                            FlatDetails.class))
                                            .returns(
                                                    entity.getBuildingType(),
                                                    FlatDetails::getBuildingType)
                                            .returns(entity.getFloor(), d -> d.getFloor().value())
                                            .returns(entity.getFloors(), d -> d.getFloors().value())
                                            .returns(
                                                    entity.getBuiltYear(),
                                                    d -> d.getBuiltYear().value())
                                            .returns(
                                                    entity.getTypeOfMarket(),
                                                    FlatDetails::getTypeOfMarket));

            assertClaims(entity.getClaims(), result.getDetails().getClaims());
            assertPhotos(entity.getPhotos(), result.getPhotos());
        }

        private static FlatAdvertisementEntity getFlatEntity() {
            final FlatAdvertisementEntity entity = mock();
            final UUID id = getId();
            final String slug = getSlug();
            final String title = getTitle();
            final String description = getDescription();
            final BigDecimal price = getPrice();
            final BigDecimal area = getArea();
            final BigDecimal pricePerSquareMeter = getPricePerSquareMeter();
            final UUID localityId = getId();
            final AdvertisementStatus status = getStatus();
            final Instant createdAt = getCreatedAt();
            final boolean isFeatured = isFeatured();
            final UUID userId = getId();
            final FlatBuildingType buildingType = FlatBuildingType.APARTMENT;
            final Integer numberOfRooms = getNumberOfRooms();
            final Integer floor = getFloor();
            final Integer floors = getFloors();
            final Integer builtYear = getBuiltYear();
            final TypeOfMarket typeOfMarket = getTypeOfMarket();
            final Set<FlatAdvertisementClaimEntity> claims =
                    Set.of(
                            new FlatAdvertisementClaimEntity(getId(), "abc", "cde"),
                            new FlatAdvertisementClaimEntity(getId(), "abc2", "cde2"));

            final Set<FlatAdvertisementPhotoEntity> photos =
                    Set.of(
                            new FlatAdvertisementPhotoEntity(getId(), 0, "https://anyUrl.com/1"),
                            new FlatAdvertisementPhotoEntity(getId(), 1, "https://anyUrl.com/2"));

            given(entity.getId()).willReturn(id);
            given(entity.getSlug()).willReturn(slug);
            given(entity.getTitle()).willReturn(title);
            given(entity.getDescription()).willReturn(description);
            given(entity.getPrice()).willReturn(price);
            given(entity.getArea()).willReturn(area);
            given(entity.getPricePerSquareMeter()).willReturn(pricePerSquareMeter);
            given(entity.getLocalityId()).willReturn(localityId);
            given(entity.getStatus()).willReturn(status);
            given(entity.getCreatedAt()).willReturn(createdAt);
            given(entity.isFeatured()).willReturn(isFeatured);
            given(entity.getUserId()).willReturn(userId);
            given(entity.getPhotos()).willReturn(photos);
            given(entity.getClaims()).willReturn(claims);
            given(entity.getNumberOfRooms()).willReturn(numberOfRooms);
            given(entity.getFloor()).willReturn(floor);
            given(entity.getFloors()).willReturn(floors);
            given(entity.getBuiltYear()).willReturn(builtYear);
            given(entity.getBuildingType()).willReturn(buildingType);
            given(entity.getTypeOfMarket()).willReturn(typeOfMarket);
            return entity;
        }

        @Test
        @DisplayName("Should map house entity")
        void shouldMapHouseEntity() {
            // Given
            final HouseAdvertisementEntity entity = getHouseEntity();

            // When
            final var result = mapper.toDomain(entity);

            // Then
            Assertions.assertThat(result)
                    .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .returns(entity.getId(), a -> a.getId().getValue())
                    .returns(entity.getSlug(), a -> a.getSlug().getValue())
                    .returns(entity.getTitle(), a -> a.getTitle().value())
                    .returns(entity.getDescription(), a -> a.getDescription().value())
                    .returns(entity.getPrice(), a -> a.getPrice().value())
                    .returns(entity.getArea(), a -> a.getArea().value())
                    .returns(
                            entity.getPricePerSquareMeter(),
                            a -> a.getPricePerSquareMeter().getValue())
                    .returns(entity.getLocalityId(), a -> a.getLocality().id().getValue())
                    .returns(entity.getUserId(), a -> a.getOwner().getValue())
                    .returns(entity.getStatus(), Advertisement::getStatus)
                    .returns(entity.getCreatedAt(), Advertisement::getCreatedAt)
                    .returns(entity.isFeatured(), Advertisement::isFeatured)
                    .satisfies(
                            e ->
                                    Assertions.assertThat(e.getDetails())
                                            .isInstanceOf(HouseDetails.class)
                                            .asInstanceOf(
                                                    InstanceOfAssertFactories.type(
                                                            HouseDetails.class))
                                            .returns(
                                                    entity.getBuildingType(),
                                                    HouseDetails::getBuildingType)
                                            .returns(entity.getFloors(), d -> d.getFloors().value())
                                            .returns(
                                                    entity.getBuiltYear(),
                                                    d -> d.getBuiltYear().value())
                                            .returns(
                                                    entity.getTypeOfMarket(),
                                                    HouseDetails::getTypeOfMarket));

            assertClaims(entity.getClaims(), result.getDetails().getClaims());
            assertPhotos(entity.getPhotos(), result.getPhotos());
        }

        private static HouseAdvertisementEntity getHouseEntity() {
            final HouseAdvertisementEntity entity = mock();
            final UUID id = getId();
            final String slug = getSlug();
            final String title = getTitle();
            final String description = getDescription();
            final BigDecimal price = getPrice();
            final BigDecimal area = getArea();
            final BigDecimal pricePerSquareMeter = getPricePerSquareMeter();
            final UUID localityId = getId();
            final AdvertisementStatus status = getStatus();
            final Instant createdAt = getCreatedAt();
            final boolean isFeatured = isFeatured();
            final UUID userId = getId();
            final HouseBuildingType buildingType = HouseBuildingType.DETACHED;
            final Integer numberOfRooms = getNumberOfRooms();
            final Integer floors = getFloors();
            final Integer builtYear = getBuiltYear();
            final TypeOfMarket typeOfMarket = getTypeOfMarket();
            final Set<HouseAdvertisementClaimEntity> claims =
                    Set.of(
                            new HouseAdvertisementClaimEntity(getId(), "abc", "cde"),
                            new HouseAdvertisementClaimEntity(getId(), "abc2", "cde2"));

            final Set<HouseAdvertisementPhotoEntity> photos =
                    Set.of(
                            new HouseAdvertisementPhotoEntity(getId(), 0, "https://anyUrl.com/1"),
                            new HouseAdvertisementPhotoEntity(getId(), 1, "https://anyUrl.com/2"));

            given(entity.getId()).willReturn(id);
            given(entity.getSlug()).willReturn(slug);
            given(entity.getTitle()).willReturn(title);
            given(entity.getDescription()).willReturn(description);
            given(entity.getPrice()).willReturn(price);
            given(entity.getArea()).willReturn(area);
            given(entity.getPricePerSquareMeter()).willReturn(pricePerSquareMeter);
            given(entity.getLocalityId()).willReturn(localityId);
            given(entity.getStatus()).willReturn(status);
            given(entity.getCreatedAt()).willReturn(createdAt);
            given(entity.isFeatured()).willReturn(isFeatured);
            given(entity.getUserId()).willReturn(userId);
            given(entity.getPhotos()).willReturn(photos);
            given(entity.getClaims()).willReturn(claims);
            given(entity.getNumberOfRooms()).willReturn(numberOfRooms);
            given(entity.getFloors()).willReturn(floors);
            given(entity.getBuiltYear()).willReturn(builtYear);
            given(entity.getBuildingType()).willReturn(buildingType);
            given(entity.getTypeOfMarket()).willReturn(typeOfMarket);
            return entity;
        }

        @Test
        @DisplayName("Should map commercial entity")
        void shouldMapCommercialEntity() {
            // Given
            final CommercialAdvertisementEntity entity = getCommercialEntity();

            // When
            final var result = mapper.toDomain(entity);

            // Then
            Assertions.assertThat(result)
                    .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .returns(entity.getId(), a -> a.getId().getValue())
                    .returns(entity.getSlug(), a -> a.getSlug().getValue())
                    .returns(entity.getTitle(), a -> a.getTitle().value())
                    .returns(entity.getDescription(), a -> a.getDescription().value())
                    .returns(entity.getPrice(), a -> a.getPrice().value())
                    .returns(entity.getArea(), a -> a.getArea().value())
                    .returns(
                            entity.getPricePerSquareMeter(),
                            a -> a.getPricePerSquareMeter().getValue())
                    .returns(entity.getLocalityId(), a -> a.getLocality().id().getValue())
                    .returns(entity.getUserId(), a -> a.getOwner().getValue())
                    .returns(entity.getStatus(), Advertisement::getStatus)
                    .returns(entity.getCreatedAt(), Advertisement::getCreatedAt)
                    .returns(entity.isFeatured(), Advertisement::isFeatured)
                    .satisfies(
                            e ->
                                    Assertions.assertThat(e.getDetails())
                                            .isInstanceOf(CommercialDetails.class)
                                            .asInstanceOf(
                                                    InstanceOfAssertFactories.type(
                                                            CommercialDetails.class))
                                            .returns(
                                                    entity.getBuildingType(),
                                                    CommercialDetails::getBuildingType)
                                            .returns(entity.getFloor(), d -> d.getFloor().value())
                                            .returns(entity.getFloors(), d -> d.getFloors().value())
                                            .returns(
                                                    entity.getBuiltYear(),
                                                    d -> d.getBuiltYear().value())
                                            .returns(
                                                    entity.getTypeOfMarket(),
                                                    CommercialDetails::getTypeOfMarket));

            assertClaims(entity.getClaims(), result.getDetails().getClaims());
            assertPhotos(entity.getPhotos(), result.getPhotos());
        }

        private static CommercialAdvertisementEntity getCommercialEntity() {
            final CommercialAdvertisementEntity entity = mock();
            final UUID id = getId();
            final String slug = getSlug();
            final String title = getTitle();
            final String description = getDescription();
            final BigDecimal price = getPrice();
            final BigDecimal area = getArea();
            final BigDecimal pricePerSquareMeter = getPricePerSquareMeter();
            final UUID localityId = getId();
            final AdvertisementStatus status = getStatus();
            final Instant createdAt = getCreatedAt();
            final boolean isFeatured = isFeatured();
            final UUID userId = getId();
            final CommercialBuildingType buildingType = CommercialBuildingType.WAREHOUSE;
            final Integer numberOfRooms = getNumberOfRooms();
            final Integer floor = getFloor();
            final Integer floors = getFloors();
            final Integer builtYear = getBuiltYear();
            final TypeOfMarket typeOfMarket = getTypeOfMarket();
            final Set<CommercialAdvertisementClaimEntity> claims =
                    Set.of(
                            new CommercialAdvertisementClaimEntity(getId(), "abc", "cde"),
                            new CommercialAdvertisementClaimEntity(getId(), "abc2", "cde2"));

            final Set<CommercialAdvertisementPhotoEntity> photos =
                    Set.of(
                            new CommercialAdvertisementPhotoEntity(
                                    getId(), 0, "https://anyUrl.com/1"),
                            new CommercialAdvertisementPhotoEntity(
                                    getId(), 1, "https://anyUrl.com/2"));

            given(entity.getId()).willReturn(id);
            given(entity.getSlug()).willReturn(slug);
            given(entity.getTitle()).willReturn(title);
            given(entity.getDescription()).willReturn(description);
            given(entity.getPrice()).willReturn(price);
            given(entity.getArea()).willReturn(area);
            given(entity.getPricePerSquareMeter()).willReturn(pricePerSquareMeter);
            given(entity.getLocalityId()).willReturn(localityId);
            given(entity.getStatus()).willReturn(status);
            given(entity.getCreatedAt()).willReturn(createdAt);
            given(entity.isFeatured()).willReturn(isFeatured);
            given(entity.getUserId()).willReturn(userId);
            given(entity.getPhotos()).willReturn(photos);
            given(entity.getClaims()).willReturn(claims);
            given(entity.getNumberOfRooms()).willReturn(numberOfRooms);
            given(entity.getFloor()).willReturn(floor);
            given(entity.getFloors()).willReturn(floors);
            given(entity.getBuiltYear()).willReturn(builtYear);
            given(entity.getBuildingType()).willReturn(buildingType);
            given(entity.getTypeOfMarket()).willReturn(typeOfMarket);
            return entity;
        }

        @Test
        @DisplayName("Should map plot entity")
        void shouldMapPlotEntity() {
            // Given
            final PlotAdvertisementEntity entity = getPlotEntity();

            // When
            final var result = mapper.toDomain(entity);

            // Then
            Assertions.assertThat(result)
                    .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .returns(entity.getId(), a -> a.getId().getValue())
                    .returns(entity.getSlug(), a -> a.getSlug().getValue())
                    .returns(entity.getTitle(), a -> a.getTitle().value())
                    .returns(entity.getDescription(), a -> a.getDescription().value())
                    .returns(entity.getPrice(), a -> a.getPrice().value())
                    .returns(entity.getArea(), a -> a.getArea().value())
                    .returns(
                            entity.getPricePerSquareMeter(),
                            a -> a.getPricePerSquareMeter().getValue())
                    .returns(entity.getLocalityId(), a -> a.getLocality().id().getValue())
                    .returns(entity.getUserId(), a -> a.getOwner().getValue())
                    .returns(entity.getStatus(), Advertisement::getStatus)
                    .returns(entity.getCreatedAt(), Advertisement::getCreatedAt)
                    .returns(entity.isFeatured(), Advertisement::isFeatured)
                    .satisfies(
                            e ->
                                    Assertions.assertThat(e.getDetails())
                                            .isInstanceOf(PlotDetails.class)
                                            .asInstanceOf(
                                                    InstanceOfAssertFactories.type(
                                                            PlotDetails.class))
                                            .returns(
                                                    entity.getPlotType(),
                                                    PlotDetails::getBuildingType));

            assertClaims(entity.getClaims(), result.getDetails().getClaims());
            assertPhotos(entity.getPhotos(), result.getPhotos());
        }

        private static PlotAdvertisementEntity getPlotEntity() {
            final PlotAdvertisementEntity entity = mock();
            final UUID id = getId();
            final String slug = getSlug();
            final String title = getTitle();
            final String description = getDescription();
            final BigDecimal price = getPrice();
            final BigDecimal area = getArea();
            final BigDecimal pricePerSquareMeter = getPricePerSquareMeter();
            final UUID localityId = getId();
            final AdvertisementStatus status = getStatus();
            final Instant createdAt = getCreatedAt();
            final boolean isFeatured = isFeatured();
            final UUID userId = getId();
            final PlotBuildingType plotType = PlotBuildingType.AGRICULTURAL;
            final Set<PlotAdvertisementClaimEntity> claims =
                    Set.of(
                            new PlotAdvertisementClaimEntity(getId(), "abc", "cde"),
                            new PlotAdvertisementClaimEntity(getId(), "abc2", "cde2"));

            final Set<PlotAdvertisementPhotoEntity> photos =
                    Set.of(
                            new PlotAdvertisementPhotoEntity(getId(), 0, "https://anyUrl.com/1"),
                            new PlotAdvertisementPhotoEntity(getId(), 1, "https://anyUrl.com/2"));

            given(entity.getId()).willReturn(id);
            given(entity.getSlug()).willReturn(slug);
            given(entity.getTitle()).willReturn(title);
            given(entity.getDescription()).willReturn(description);
            given(entity.getPrice()).willReturn(price);
            given(entity.getArea()).willReturn(area);
            given(entity.getPricePerSquareMeter()).willReturn(pricePerSquareMeter);
            given(entity.getLocalityId()).willReturn(localityId);
            given(entity.getStatus()).willReturn(status);
            given(entity.getCreatedAt()).willReturn(createdAt);
            given(entity.isFeatured()).willReturn(isFeatured);
            given(entity.getUserId()).willReturn(userId);
            given(entity.getPhotos()).willReturn(photos);
            given(entity.getClaims()).willReturn(claims);
            given(entity.getPlotType()).willReturn(plotType);
            return entity;
        }
    }

    private void assertClaims(
            final Set<? extends AdvertisementClaimEntity<?>> entityClaims,
            final Set<AdvertisementClaim> domainClaims) {

        final Tuple[] entityClaimsTuples =
                entityClaims.stream()
                        .map(c -> tuple(c.getClaimKey(), c.getClaimValue()))
                        .toArray(Tuple[]::new);

        Assertions.assertThat(domainClaims)
                .extracting(AdvertisementClaim::key, AdvertisementClaim::value)
                .containsExactlyInAnyOrder(entityClaimsTuples);
    }

    private void assertPhotos(
            final Set<? extends AdvertisementPhotoEntity<?>> entityPhotos,
            final Set<AdvertisementPhoto> domainPhotos) {

        final Tuple[] entityPhotosTuples =
                entityPhotos.stream()
                        .map(c -> tuple(c.getId(), c.getPosition(), c.getUrl()))
                        .toArray(Tuple[]::new);

        Assertions.assertThat(domainPhotos)
                .extracting(
                        p -> p.getId().getValue(),
                        AdvertisementPhoto::getPosition,
                        p -> p.getUrl().value())
                .containsExactlyInAnyOrder(entityPhotosTuples);
    }

    @Nested
    final class ToFlatEntityTests {}

    @Nested
    final class ToHouseEntityTests {}

    @Nested
    final class ToCommercialEntityTests {}

    @Nested
    final class ToPlotEntityTests {}

    private static UUID getId() {
        return UUID.randomUUID();
    }

    private static String getSlug() {
        return "any-slug-12345678";
    }

    private static String getTitle() {
        return "any-title-12345678";
    }

    private static String getDescription() {
        return "any-description-12345678";
    }

    private static BigDecimal getPrice() {
        return BigDecimal.valueOf(250_000d);
    }

    private static BigDecimal getArea() {
        return BigDecimal.valueOf(25d);
    }

    private static BigDecimal getPricePerSquareMeter() {
        return BigDecimal.valueOf(2500d);
    }

    private static AdvertisementStatus getStatus() {
        return AdvertisementStatus.ACTIVE;
    }

    private static Instant getCreatedAt() {
        return Instant.now().minusMillis(25_000);
    }

    private static boolean isFeatured() {
        return true;
    }

    private static int getNumberOfRooms() {
        return 1;
    }

    private static int getFloor() {
        return 2;
    }

    private static int getFloors() {
        return getFloor() + 1;
    }

    private static int getBuiltYear() {
        return 2011;
    }

    private static TypeOfMarket getTypeOfMarket() {
        return TypeOfMarket.PRIMARY;
    }
}
