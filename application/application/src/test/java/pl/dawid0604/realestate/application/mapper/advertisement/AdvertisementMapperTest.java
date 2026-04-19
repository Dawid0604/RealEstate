/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.mapper.advertisement;

import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementPhotoDto;
import pl.dawid0604.realestate.application.dto.advertisement.CommercialAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.FlatAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.HouseAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementDetailsProjection;
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
    @DisplayName("Should map to FlatDetailsDto properly")
    void shouldMapToFlatDetailsDtoProperly(final AdvertisementType type) {
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
}
