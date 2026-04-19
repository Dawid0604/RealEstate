/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.mapper.advertisement;

import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementPhotoDto;
import pl.dawid0604.realestate.application.dto.advertisement.CommercialAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.CommercialAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.FlatAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.FlatAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.HouseAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.HouseAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserCommercialAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserFlatAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserHouseAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserPlotAdvertisementCardDto;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserCommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserFlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserHouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserPlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;
import pl.dawid0604.realestate.domain.shared.user.projection.AdvertisementUserProjection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AdvertisementMapperTest {
    private AdvertisementMapper advertisementMapper;

    @BeforeEach
    void setUp() {
        advertisementMapper = Mappers.getMapper(AdvertisementMapper.class);
    }

    @ParameterizedTest
    @EnumSource(AdvertisementType.class)
    @DisplayName("Should map to AdvertisementDetailsDto properly")
    void shouldMapToAdvertisementDetailsDtoProperly(final AdvertisementType type) {
        // Given
        final AdvertisementDetailsProjection projection =
                switch (type) {
                    case FLAT -> mock(FlatAdvertisementDetailsProjection.class);
                    case HOUSE -> mock(HouseAdvertisementDetailsProjection.class);
                    case COMMERCIAL -> mock(CommercialAdvertisementDetailsProjection.class);
                    case PLOT -> mock(PlotAdvertisementDetailsProjection.class);
                };

        final String localityFullName = "abc";
        final PhotoProjection photo1 = mock();
        final PhotoProjection photo2 = mock();

        given(photo1.getId()).willReturn(UUID.randomUUID());
        given(photo2.getId()).willReturn(UUID.randomUUID());

        given(photo1.getUrl()).willReturn("url1");
        given(photo2.getUrl()).willReturn("url2");

        given(photo1.getPosition()).willReturn(0);
        given(photo2.getPosition()).willReturn(1);

        final AdvertisementClaimProjection claim1 = mock();
        final AdvertisementClaimProjection claim2 = mock();

        given(claim1.getClaimKey()).willReturn("key1");
        given(claim2.getClaimKey()).willReturn("key2");

        given(claim1.getClaimValue()).willReturn("value1");
        given(claim2.getClaimValue()).willReturn("value2");

        final AdvertisementUserProjection user = mock();

        given(user.getId()).willReturn(UUID.randomUUID());
        given(user.getFirstName()).willReturn("John");
        given(user.getLastName()).willReturn("Doe");
        given(user.getType()).willReturn(UserType.AGENCY.name());
        given(user.getContactEmail()).willReturn(UserFixture.getDummyEmail());
        given(user.getContactPhoneNumber()).willReturn("123456789");

        final Set<PhotoProjection> photos = Set.of(photo1, photo2);
        final Set<AdvertisementClaimProjection> claims = Set.of(claim1, claim2);

        given(projection.getSlug()).willReturn("slug");
        given(projection.getTitle()).willReturn("title");
        given(projection.getDescription()).willReturn("description");
        given(projection.getPrice()).willReturn(BigDecimal.valueOf(450_000));
        given(projection.getArea()).willReturn(BigDecimal.valueOf(45));
        given(projection.getPricePerSquareMeter()).willReturn(BigDecimal.valueOf(4500));
        given(projection.getStatus()).willReturn(AdvertisementStatus.ACTIVE.name());
        given(projection.getCreatedAt()).willReturn(Instant.now().minusMillis(25_000));
        given(projection.isFeatured()).willReturn(true);

        switch (type) {
            case FLAT -> {
                final FlatAdvertisementDetailsProjection flatProjection =
                        (FlatAdvertisementDetailsProjection) projection;

                given(flatProjection.getBuildingType())
                        .willReturn(FlatBuildingType.APARTMENT.name());

                given(flatProjection.getNumberOfRooms()).willReturn(3);
                given(flatProjection.getFloor()).willReturn(4);
                given(flatProjection.getFloors()).willReturn(5);
                given(flatProjection.getBuiltYear()).willReturn(1998);
            }

            case HOUSE -> {
                final HouseAdvertisementDetailsProjection houseProjection =
                        (HouseAdvertisementDetailsProjection) projection;

                given(houseProjection.getBuildingType())
                        .willReturn(HouseBuildingType.DETACHED.name());

                given(houseProjection.getNumberOfRooms()).willReturn(3);
                given(houseProjection.getFloors()).willReturn(5);
                given(houseProjection.getBuiltYear()).willReturn(1998);
            }

            case COMMERCIAL -> {
                final CommercialAdvertisementDetailsProjection commercialProjection =
                        (CommercialAdvertisementDetailsProjection) projection;

                given(commercialProjection.getBuildingType())
                        .willReturn(CommercialBuildingType.HALL.name());

                given(commercialProjection.getNumberOfRooms()).willReturn(3);
                given(commercialProjection.getFloor()).willReturn(4);
                given(commercialProjection.getFloors()).willReturn(5);
                given(commercialProjection.getBuiltYear()).willReturn(1998);
            }

            case PLOT ->
                    given(((PlotAdvertisementDetailsProjection) projection).getPlotType())
                            .willReturn(PlotBuildingType.FOREST.name());
        }

        // When
        final AdvertisementDetailsDto result =
                switch (type) {
                    case FLAT ->
                            advertisementMapper.toFlatDetailsDto(
                                    ((FlatAdvertisementDetailsProjection) projection),
                                    localityFullName,
                                    photos,
                                    claims,
                                    user);

                    case HOUSE ->
                            advertisementMapper.toHouseDetailsDto(
                                    ((HouseAdvertisementDetailsProjection) projection),
                                    localityFullName,
                                    photos,
                                    claims,
                                    user);
                    case COMMERCIAL ->
                            advertisementMapper.toCommercialDetailsDto(
                                    ((CommercialAdvertisementDetailsProjection) projection),
                                    localityFullName,
                                    photos,
                                    claims,
                                    user);
                    case PLOT ->
                            advertisementMapper.toPlotDetailsDto(
                                    ((PlotAdvertisementDetailsProjection) projection),
                                    localityFullName,
                                    photos,
                                    claims,
                                    user);
                };

        // Then
        Assertions.assertThat(result)
                .returns(projection.getSlug(), AdvertisementDetailsDto::slug)
                .returns(projection.getTitle(), AdvertisementDetailsDto::title)
                .returns(projection.getDescription(), AdvertisementDetailsDto::description)
                .returns(projection.getPrice(), AdvertisementDetailsDto::price)
                .returns(projection.getArea(), AdvertisementDetailsDto::area)
                .returns(
                        projection.getPricePerSquareMeter(),
                        AdvertisementDetailsDto::pricePerSquareMeter)
                .returns(projection.getStatus(), AdvertisementDetailsDto::status)
                .returns(projection.getCreatedAt(), AdvertisementDetailsDto::createdAt)
                .returns(projection.isFeatured(), AdvertisementDetailsDto::isFeatured);

        switch (type) {
            case FLAT -> {
                final FlatAdvertisementDetailsProjection flatProjection =
                        (FlatAdvertisementDetailsProjection) projection;

                Assertions.assertThat(result)
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(FlatAdvertisementDetailsDto.class))
                        .returns(
                                flatProjection.getBuildingType(),
                                FlatAdvertisementDetailsDto::buildingType)
                        .returns(
                                flatProjection.getNumberOfRooms(),
                                FlatAdvertisementDetailsDto::numberOfRooms)
                        .returns(flatProjection.getFloor(), FlatAdvertisementDetailsDto::floor)
                        .returns(flatProjection.getFloors(), FlatAdvertisementDetailsDto::floors)
                        .returns(
                                flatProjection.getBuiltYear(),
                                FlatAdvertisementDetailsDto::builtYear);
            }

            case COMMERCIAL -> {
                final CommercialAdvertisementDetailsProjection commercialProjection =
                        (CommercialAdvertisementDetailsProjection) projection;

                Assertions.assertThat(result)
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(
                                        CommercialAdvertisementDetailsDto.class))
                        .returns(
                                commercialProjection.getBuildingType(),
                                CommercialAdvertisementDetailsDto::buildingType)
                        .returns(
                                commercialProjection.getNumberOfRooms(),
                                CommercialAdvertisementDetailsDto::numberOfRooms)
                        .returns(
                                commercialProjection.getFloor(),
                                CommercialAdvertisementDetailsDto::floor)
                        .returns(
                                commercialProjection.getFloors(),
                                CommercialAdvertisementDetailsDto::floors)
                        .returns(
                                commercialProjection.getBuiltYear(),
                                CommercialAdvertisementDetailsDto::builtYear);
            }

            case HOUSE -> {
                final HouseAdvertisementDetailsProjection houseProjection =
                        (HouseAdvertisementDetailsProjection) projection;

                Assertions.assertThat(result)
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(HouseAdvertisementDetailsDto.class))
                        .returns(
                                houseProjection.getBuildingType(),
                                HouseAdvertisementDetailsDto::buildingType)
                        .returns(
                                houseProjection.getNumberOfRooms(),
                                HouseAdvertisementDetailsDto::numberOfRooms)
                        .returns(houseProjection.getFloors(), HouseAdvertisementDetailsDto::floors)
                        .returns(
                                houseProjection.getBuiltYear(),
                                HouseAdvertisementDetailsDto::builtYear);
            }

            case PLOT ->
                    Assertions.assertThat(result)
                            .asInstanceOf(
                                    InstanceOfAssertFactories.type(
                                            PlotAdvertisementDetailsDto.class))
                            .returns(
                                    ((PlotAdvertisementDetailsProjection) projection).getPlotType(),
                                    PlotAdvertisementDetailsDto::plotType);
        }

        Assertions.assertThat(result.owner())
                .returns(user.getId(), FlatAdvertisementDetailsDto.Owner::id)
                .returns(user.getUserAvatarUrl(), FlatAdvertisementDetailsDto.Owner::avatarUrl)
                .returns(user.getType(), FlatAdvertisementDetailsDto.Owner::type)
                .returns(
                        user.getContactPhoneNumber(),
                        FlatAdvertisementDetailsDto.Owner::contactPhoneNumber)
                .returns(user.getContactEmail(), FlatAdvertisementDetailsDto.Owner::contactEmail)
                .returns(
                        user.getFirstName() + " " + user.getLastName(),
                        FlatAdvertisementDetailsDto.Owner::fullName);

        Assertions.assertThat(result.photos())
                .hasSize(photos.size())
                .extracting(
                        AdvertisementPhotoDto::id,
                        AdvertisementPhotoDto::url,
                        AdvertisementPhotoDto::position)
                .containsExactlyInAnyOrder(
                        tuple(photo1.getId(), photo1.getUrl(), photo1.getPosition()),
                        tuple(photo2.getId(), photo2.getUrl(), photo2.getPosition()));

        Assertions.assertThat(result.claims())
                .hasSize(claims.size())
                .extracting(
                        AdvertisementDetailsDto.Claim::claimKey,
                        AdvertisementDetailsDto.Claim::claimValue)
                .containsExactlyInAnyOrder(
                        tuple(claim1.getClaimKey(), claim1.getClaimValue()),
                        tuple(claim2.getClaimKey(), claim2.getClaimValue()));
    }

    @ParameterizedTest
    @EnumSource(AdvertisementType.class)
    @DisplayName("Should map to UserAdvertisementCardDto properly")
    void shouldMapToUserAdvertisementCardDtoProperly(final AdvertisementType type) {
        // Given
        final UserAdvertisementCardProjection projection =
                switch (type) {
                    case FLAT -> mock(UserFlatAdvertisementCardProjection.class);
                    case HOUSE -> mock(UserHouseAdvertisementCardProjection.class);
                    case COMMERCIAL -> mock(UserCommercialAdvertisementCardProjection.class);
                    case PLOT -> mock(UserPlotAdvertisementCardProjection.class);
                };

        final String localityFullName = "abc";
        final PhotoProjection photo1 = mock();
        final PhotoProjection photo2 = mock();

        given(photo1.getId()).willReturn(UUID.randomUUID());
        given(photo2.getId()).willReturn(UUID.randomUUID());

        given(photo1.getUrl()).willReturn("url1");
        given(photo2.getUrl()).willReturn("url2");

        given(photo1.getPosition()).willReturn(0);
        given(photo2.getPosition()).willReturn(1);

        final Set<PhotoProjection> photos = Set.of(photo1, photo2);

        given(projection.getSlug()).willReturn("slug");
        given(projection.getTitle()).willReturn("title");
        given(projection.getPrice()).willReturn(BigDecimal.valueOf(450_000));
        given(projection.getArea()).willReturn(BigDecimal.valueOf(45));
        given(projection.getPricePerSquareMeter()).willReturn(BigDecimal.valueOf(4500));
        given(projection.getStatus()).willReturn(AdvertisementStatus.ACTIVE.name());
        given(projection.getCreatedAt()).willReturn(Instant.now().minusMillis(25_000));
        given(projection.isFeatured()).willReturn(true);

        switch (type) {
            case FLAT -> {
                final UserFlatAdvertisementCardProjection flatProjection =
                        (UserFlatAdvertisementCardProjection) projection;

                given(flatProjection.getBuildingType())
                        .willReturn(FlatBuildingType.APARTMENT.name());

                given(flatProjection.getNumberOfRooms()).willReturn(3);
                given(flatProjection.getFloor()).willReturn(4);
                given(flatProjection.getFloors()).willReturn(5);
                given(flatProjection.getBuiltYear()).willReturn(1998);
            }

            case HOUSE -> {
                final UserHouseAdvertisementCardProjection houseProjection =
                        (UserHouseAdvertisementCardProjection) projection;

                given(houseProjection.getBuildingType())
                        .willReturn(HouseBuildingType.DETACHED.name());

                given(houseProjection.getNumberOfRooms()).willReturn(3);
                given(houseProjection.getFloors()).willReturn(5);
                given(houseProjection.getBuiltYear()).willReturn(1998);
            }

            case COMMERCIAL -> {
                final UserCommercialAdvertisementCardProjection commercialProjection =
                        (UserCommercialAdvertisementCardProjection) projection;

                given(commercialProjection.getBuildingType())
                        .willReturn(CommercialBuildingType.HALL.name());

                given(commercialProjection.getNumberOfRooms()).willReturn(3);
                given(commercialProjection.getFloor()).willReturn(4);
                given(commercialProjection.getFloors()).willReturn(5);
                given(commercialProjection.getBuiltYear()).willReturn(1998);
            }

            case PLOT ->
                    given(((UserPlotAdvertisementCardProjection) projection).getPlotType())
                            .willReturn(PlotBuildingType.FOREST.name());
        }

        // When
        final UserAdvertisementCardDto result =
                switch (type) {
                    case FLAT ->
                            advertisementMapper.toUserFlatCardDto(
                                    ((UserFlatAdvertisementCardProjection) projection),
                                    localityFullName,
                                    photos);

                    case HOUSE ->
                            advertisementMapper.toUserHouseCardDto(
                                    ((UserHouseAdvertisementCardProjection) projection),
                                    localityFullName,
                                    photos);
                    case COMMERCIAL ->
                            advertisementMapper.toUserCommercialCardDto(
                                    ((UserCommercialAdvertisementCardProjection) projection),
                                    localityFullName,
                                    photos);
                    case PLOT ->
                            advertisementMapper.toUserPlotCardDto(
                                    ((UserPlotAdvertisementCardProjection) projection),
                                    localityFullName,
                                    photos);
                };

        // Then
        Assertions.assertThat(result)
                .returns(projection.getSlug(), UserAdvertisementCardDto::slug)
                .returns(projection.getTitle(), UserAdvertisementCardDto::title)
                .returns(projection.getPrice(), UserAdvertisementCardDto::price)
                .returns(projection.getArea(), UserAdvertisementCardDto::area)
                .returns(
                        projection.getPricePerSquareMeter(),
                        UserAdvertisementCardDto::pricePerSquareMeter)
                .returns(projection.getStatus(), UserAdvertisementCardDto::status)
                .returns(projection.getCreatedAt(), UserAdvertisementCardDto::createdAt)
                .returns(projection.isFeatured(), UserAdvertisementCardDto::isFeatured);

        switch (type) {
            case FLAT -> {
                final UserFlatAdvertisementCardProjection flatProjection =
                        (UserFlatAdvertisementCardProjection) projection;

                Assertions.assertThat(result)
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(UserFlatAdvertisementCardDto.class))
                        .returns(
                                flatProjection.getBuildingType(),
                                UserFlatAdvertisementCardDto::buildingType)
                        .returns(
                                flatProjection.getNumberOfRooms(),
                                UserFlatAdvertisementCardDto::numberOfRooms)
                        .returns(flatProjection.getFloor(), UserFlatAdvertisementCardDto::floor)
                        .returns(flatProjection.getFloors(), UserFlatAdvertisementCardDto::floors)
                        .returns(
                                flatProjection.getBuiltYear(),
                                UserFlatAdvertisementCardDto::builtYear);
            }

            case COMMERCIAL -> {
                final UserCommercialAdvertisementCardProjection commercialProjection =
                        (UserCommercialAdvertisementCardProjection) projection;

                Assertions.assertThat(result)
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(
                                        UserCommercialAdvertisementCardDto.class))
                        .returns(
                                commercialProjection.getBuildingType(),
                                UserCommercialAdvertisementCardDto::buildingType)
                        .returns(
                                commercialProjection.getNumberOfRooms(),
                                UserCommercialAdvertisementCardDto::numberOfRooms)
                        .returns(
                                commercialProjection.getFloor(),
                                UserCommercialAdvertisementCardDto::floor)
                        .returns(
                                commercialProjection.getFloors(),
                                UserCommercialAdvertisementCardDto::floors)
                        .returns(
                                commercialProjection.getBuiltYear(),
                                UserCommercialAdvertisementCardDto::builtYear);
            }

            case HOUSE -> {
                final UserHouseAdvertisementCardProjection houseProjection =
                        (UserHouseAdvertisementCardProjection) projection;

                Assertions.assertThat(result)
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(UserHouseAdvertisementCardDto.class))
                        .returns(
                                houseProjection.getBuildingType(),
                                UserHouseAdvertisementCardDto::buildingType)
                        .returns(
                                houseProjection.getNumberOfRooms(),
                                UserHouseAdvertisementCardDto::numberOfRooms)
                        .returns(houseProjection.getFloors(), UserHouseAdvertisementCardDto::floors)
                        .returns(
                                houseProjection.getBuiltYear(),
                                UserHouseAdvertisementCardDto::builtYear);
            }

            case PLOT ->
                    Assertions.assertThat(result)
                            .asInstanceOf(
                                    InstanceOfAssertFactories.type(
                                            UserPlotAdvertisementCardDto.class))
                            .returns(
                                    ((UserPlotAdvertisementCardProjection) projection)
                                            .getPlotType(),
                                    UserPlotAdvertisementCardDto::plotType);
        }

        Assertions.assertThat(result.photos())
                .hasSize(photos.size())
                .extracting(
                        AdvertisementPhotoDto::id,
                        AdvertisementPhotoDto::url,
                        AdvertisementPhotoDto::position)
                .containsExactlyInAnyOrder(
                        tuple(photo1.getId(), photo1.getUrl(), photo1.getPosition()),
                        tuple(photo2.getId(), photo2.getUrl(), photo2.getPosition()));
    }

    @ParameterizedTest
    @EnumSource(AdvertisementType.class)
    @DisplayName("Should map to AdvertisementCardDto properly")
    void shouldMapToAdvertisementCardDtoProperly(final AdvertisementType type) {
        // Given
        final AdvertisementCardProjection projection =
                switch (type) {
                    case FLAT -> mock(FlatAdvertisementCardProjection.class);
                    case HOUSE -> mock(HouseAdvertisementCardProjection.class);
                    case COMMERCIAL -> mock(CommercialAdvertisementCardProjection.class);
                    case PLOT -> mock(PlotAdvertisementCardProjection.class);
                };

        final String localityFullName = "abc";
        final PhotoProjection photo1 = mock();
        final PhotoProjection photo2 = mock();

        given(photo1.getId()).willReturn(UUID.randomUUID());
        given(photo2.getId()).willReturn(UUID.randomUUID());

        given(photo1.getUrl()).willReturn("url1");
        given(photo2.getUrl()).willReturn("url2");

        given(photo1.getPosition()).willReturn(0);
        given(photo2.getPosition()).willReturn(1);

        final Set<PhotoProjection> photos = Set.of(photo1, photo2);

        given(projection.getSlug()).willReturn("slug");
        given(projection.getTitle()).willReturn("title");
        given(projection.getPrice()).willReturn(BigDecimal.valueOf(450_000));
        given(projection.getArea()).willReturn(BigDecimal.valueOf(45));
        given(projection.getPricePerSquareMeter()).willReturn(BigDecimal.valueOf(4500));
        given(projection.getStatus()).willReturn(AdvertisementStatus.ACTIVE.name());
        given(projection.getCreatedAt()).willReturn(Instant.now().minusMillis(25_000));
        given(projection.isFeatured()).willReturn(true);

        switch (type) {
            case FLAT -> {
                final FlatAdvertisementCardProjection flatProjection =
                        (FlatAdvertisementCardProjection) projection;

                given(flatProjection.getBuildingType())
                        .willReturn(FlatBuildingType.APARTMENT.name());

                given(flatProjection.getNumberOfRooms()).willReturn(3);
                given(flatProjection.getFloor()).willReturn(4);
                given(flatProjection.getFloors()).willReturn(5);
                given(flatProjection.getBuiltYear()).willReturn(1998);
            }

            case HOUSE -> {
                final HouseAdvertisementCardProjection houseProjection =
                        (HouseAdvertisementCardProjection) projection;

                given(houseProjection.getBuildingType())
                        .willReturn(HouseBuildingType.DETACHED.name());

                given(houseProjection.getNumberOfRooms()).willReturn(3);
                given(houseProjection.getFloors()).willReturn(5);
                given(houseProjection.getBuiltYear()).willReturn(1998);
            }

            case COMMERCIAL -> {
                final CommercialAdvertisementCardProjection commercialProjection =
                        (CommercialAdvertisementCardProjection) projection;

                given(commercialProjection.getBuildingType())
                        .willReturn(CommercialBuildingType.HALL.name());

                given(commercialProjection.getNumberOfRooms()).willReturn(3);
                given(commercialProjection.getFloor()).willReturn(4);
                given(commercialProjection.getFloors()).willReturn(5);
                given(commercialProjection.getBuiltYear()).willReturn(1998);
            }

            case PLOT ->
                    given(((PlotAdvertisementCardProjection) projection).getPlotType())
                            .willReturn(PlotBuildingType.FOREST.name());
        }

        // When
        final AdvertisementCardDto result =
                switch (type) {
                    case FLAT ->
                            advertisementMapper.toFlatCardDto(
                                    ((FlatAdvertisementCardProjection) projection),
                                    localityFullName,
                                    photos);

                    case HOUSE ->
                            advertisementMapper.toHouseCardDto(
                                    ((HouseAdvertisementCardProjection) projection),
                                    localityFullName,
                                    photos);
                    case COMMERCIAL ->
                            advertisementMapper.toCommercialCardDto(
                                    ((CommercialAdvertisementCardProjection) projection),
                                    localityFullName,
                                    photos);
                    case PLOT ->
                            advertisementMapper.toPlotCardDto(
                                    ((PlotAdvertisementCardProjection) projection),
                                    localityFullName,
                                    photos);
                };

        // Then
        Assertions.assertThat(result)
                .returns(projection.getSlug(), AdvertisementCardDto::slug)
                .returns(projection.getTitle(), AdvertisementCardDto::title)
                .returns(projection.getPrice(), AdvertisementCardDto::price)
                .returns(projection.getArea(), AdvertisementCardDto::area)
                .returns(
                        projection.getPricePerSquareMeter(),
                        AdvertisementCardDto::pricePerSquareMeter)
                .returns(projection.getCreatedAt(), AdvertisementCardDto::createdAt)
                .returns(projection.isFeatured(), AdvertisementCardDto::isFeatured);

        switch (type) {
            case FLAT -> {
                final FlatAdvertisementCardProjection flatProjection =
                        (FlatAdvertisementCardProjection) projection;

                Assertions.assertThat(result)
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(FlatAdvertisementCardDto.class))
                        .returns(
                                flatProjection.getBuildingType(),
                                FlatAdvertisementCardDto::buildingType)
                        .returns(
                                flatProjection.getNumberOfRooms(),
                                FlatAdvertisementCardDto::numberOfRooms)
                        .returns(flatProjection.getFloor(), FlatAdvertisementCardDto::floor)
                        .returns(flatProjection.getFloors(), FlatAdvertisementCardDto::floors)
                        .returns(
                                flatProjection.getBuiltYear(), FlatAdvertisementCardDto::builtYear);
            }

            case COMMERCIAL -> {
                final CommercialAdvertisementCardProjection commercialProjection =
                        (CommercialAdvertisementCardProjection) projection;

                Assertions.assertThat(result)
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(
                                        CommercialAdvertisementCardDto.class))
                        .returns(
                                commercialProjection.getBuildingType(),
                                CommercialAdvertisementCardDto::buildingType)
                        .returns(
                                commercialProjection.getNumberOfRooms(),
                                CommercialAdvertisementCardDto::numberOfRooms)
                        .returns(
                                commercialProjection.getFloor(),
                                CommercialAdvertisementCardDto::floor)
                        .returns(
                                commercialProjection.getFloors(),
                                CommercialAdvertisementCardDto::floors)
                        .returns(
                                commercialProjection.getBuiltYear(),
                                CommercialAdvertisementCardDto::builtYear);
            }

            case HOUSE -> {
                final HouseAdvertisementCardProjection houseProjection =
                        (HouseAdvertisementCardProjection) projection;

                Assertions.assertThat(result)
                        .asInstanceOf(
                                InstanceOfAssertFactories.type(HouseAdvertisementCardDto.class))
                        .returns(
                                houseProjection.getBuildingType(),
                                HouseAdvertisementCardDto::buildingType)
                        .returns(
                                houseProjection.getNumberOfRooms(),
                                HouseAdvertisementCardDto::numberOfRooms)
                        .returns(houseProjection.getFloors(), HouseAdvertisementCardDto::floors)
                        .returns(
                                houseProjection.getBuiltYear(),
                                HouseAdvertisementCardDto::builtYear);
            }

            case PLOT ->
                    Assertions.assertThat(result)
                            .asInstanceOf(
                                    InstanceOfAssertFactories.type(PlotAdvertisementCardDto.class))
                            .returns(
                                    ((PlotAdvertisementCardProjection) projection).getPlotType(),
                                    PlotAdvertisementCardDto::plotType);
        }

        Assertions.assertThat(result.photos())
                .hasSize(photos.size())
                .extracting(
                        AdvertisementPhotoDto::id,
                        AdvertisementPhotoDto::url,
                        AdvertisementPhotoDto::position)
                .containsExactlyInAnyOrder(
                        tuple(photo1.getId(), photo1.getUrl(), photo1.getPosition()),
                        tuple(photo2.getId(), photo2.getUrl(), photo2.getPosition()));
    }

    @Nested
    final class ToPhotoTests {

        @Test
        @DisplayName("Should return null when projection is null")
        void shouldReturnNullWhenProjectionIsNull() {
            // Given
            // When
            final AdvertisementPhotoDto result = advertisementMapper.toPhoto(null);

            // Then
            Assertions.assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should map photo")
        void shouldMapPhoto() {
            // Given
            final PhotoProjection projection = mock();

            given(projection.getId()).willReturn(UUID.randomUUID());
            given(projection.getUrl()).willReturn("anyUrl");
            given(projection.getPosition()).willReturn(1);

            // When
            final AdvertisementPhotoDto result = advertisementMapper.toPhoto(projection);

            // Then
            Assertions.assertThat(result)
                    .returns(projection.getId(), AdvertisementPhotoDto::id)
                    .returns(projection.getUrl(), AdvertisementPhotoDto::url)
                    .returns(projection.getPosition(), AdvertisementPhotoDto::position);
        }
    }

    @Nested
    final class ToOwnerTests {

        @Test
        @DisplayName("Should return null when projection is null")
        void shouldReturnNullWhenProjectionIsNull() {
            // Given
            // When
            final AdvertisementDetailsDto.Owner result = advertisementMapper.toOwner(null);

            // Then
            Assertions.assertThat(result).isNull();
        }

        @ParameterizedTest
        @CsvSource({
            "John,Doe,John Doe",
            "John,,John",
            ",Doe,Doe",
            ",,",
            " John , Doe ,John Doe",
        })
        @DisplayName("Should map owner")
        void shouldMapOwner(
                final String firstName, final String lastName, final String expectedFullName) {

            // Given
            final AdvertisementUserProjection projection = mock();

            given(projection.getId()).willReturn(UUID.randomUUID());
            given(projection.getFirstName()).willReturn(firstName);
            given(projection.getLastName()).willReturn(lastName);
            given(projection.getType()).willReturn(UserType.DEVELOPER.name());
            given(projection.getContactPhoneNumber()).willReturn("123456789");
            given(projection.getContactEmail()).willReturn(UserFixture.getDummyEmail());

            // When
            final AdvertisementDetailsDto.Owner result = advertisementMapper.toOwner(projection);

            // Then
            Assertions.assertThat(result)
                    .returns(projection.getId(), AdvertisementDetailsDto.Owner::id)
                    .returns(expectedFullName, AdvertisementDetailsDto.Owner::fullName)
                    .returns(
                            projection.getUserAvatarUrl(), AdvertisementDetailsDto.Owner::avatarUrl)
                    .returns(projection.getType(), AdvertisementDetailsDto.Owner::type)
                    .returns(
                            projection.getContactPhoneNumber(),
                            AdvertisementDetailsDto.Owner::contactPhoneNumber)
                    .returns(
                            projection.getContactEmail(),
                            AdvertisementDetailsDto.Owner::contactEmail);
        }
    }
}
