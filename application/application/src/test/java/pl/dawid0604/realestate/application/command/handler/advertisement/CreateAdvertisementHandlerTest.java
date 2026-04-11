/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.advertisement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyEmail;
import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyUserBuilder;

import static java.util.Collections.emptySet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.command.CreateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.CreateCommercialAdvertisementCommand;
import pl.dawid0604.realestate.application.command.CreateFlatAdvertisementCommand;
import pl.dawid0604.realestate.application.command.CreateHouseAdvertisementCommand;
import pl.dawid0604.realestate.application.command.CreatePlotAdvertisementCommand;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementClaim;
import pl.dawid0604.realestate.domain.AdvertisementPhoto;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.CommercialDetails;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.FlatDetails;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.HouseDetails;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.MoneyCurrency;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.PlotDetails;
import pl.dawid0604.realestate.domain.TypeOfMarket;
import pl.dawid0604.realestate.domain.Url;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UnauthorizedAccessException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class CreateAdvertisementHandlerTest {
    @Mock private AdvertisementRepository advertisementRepository;
    @Mock private UserRepository userRepository;
    @Captor private ArgumentCaptor<Advertisement> advertisementArgumentCaptor;
    private CreateAdvertisementHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateAdvertisementHandler(advertisementRepository, userRepository);
    }

    @ParameterizedTest
    @MethodSource("commandsDataProvider")
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound(final CreateAdvertisementCommand command) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).save(any());
        verify(advertisementRepository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource("commandsDataProvider")
    @DisplayName("Should throw exception when user is inactive")
    void shouldThrowExceptionWhenUserIsInactive(final CreateAdvertisementCommand command) {
        // Given
        final User foundUser = getDummyUserBuilder().status(UserStatus.INACTIVE).build();

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(UnauthorizedAccessException.class);

        verify(userRepository, never()).save(any());
        verify(advertisementRepository, never()).save(any());
    }

    @ParameterizedTest
    @DisplayName("Should create")
    @MethodSource("commandsDataProvider")
    void shouldCreate(final CreateAdvertisementCommand command) {
        // Given
        final User foundUser = getDummyUserBuilder().build();

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));

        // When
        final UUID result = handler.handle(command);

        // Then
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        Assertions.assertThat(advertisementArgumentCaptor.getValue())
                .satisfies(
                        advertisement -> {
                            Assertions.assertThat(advertisement.getId().getValue())
                                    .isEqualTo(result);

                            Assertions.assertThat(advertisement.getTitle().value())
                                    .isEqualTo(command.title());

                            Assertions.assertThat(advertisement.getDescription().value())
                                    .isEqualTo(command.description());

                            Assertions.assertThat(advertisement.getArea().value())
                                    .isEqualByComparingTo(command.area());

                            Assertions.assertThat(advertisement.getPrice())
                                    .satisfies(
                                            price -> {
                                                Assertions.assertThat(price.value())
                                                        .isEqualByComparingTo(command.price());

                                                Assertions.assertThat(price.currency())
                                                        .isEqualTo(MoneyCurrency.PLN);
                                            });

                            Assertions.assertThat(advertisement.getLocality().id().getValue())
                                    .isEqualTo(command.localityId());

                            Assertions.assertThat(advertisement.getOwner())
                                    .isEqualTo(foundUser.getId());

                            Assertions.assertThat(advertisement.isFeatured()).isFalse();
                            Assertions.assertThat(advertisement.getPhotos()).isEmpty();
                            Assertions.assertThat(advertisement.getDetails());
                        });
    }

    @ParameterizedTest
    @MethodSource("commandsDataProvider")
    @DisplayName("Should assign proper details")
    void shouldAssignProperDetails(final CreateAdvertisementCommand command) {
        // Given
        final User foundUser = getDummyUserBuilder().build();

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));

        // When
        handler.handle(command);

        // Then
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        Assertions.assertThat(advertisementArgumentCaptor.getValue().getDetails())
                .satisfies(
                        details -> {
                            switch (command) {
                                case CreateCommercialAdvertisementCommand commercialCommand ->
                                        assertCommercialDetails(
                                                (CommercialDetails) details,
                                                commercialCommand,
                                                emptySet());

                                case CreateFlatAdvertisementCommand flatCommand ->
                                        assertFlatDetails(
                                                (FlatDetails) details, flatCommand, emptySet());

                                case CreateHouseAdvertisementCommand houseCommand ->
                                        assertHouseDetails(
                                                (HouseDetails) details, houseCommand, emptySet());

                                case CreatePlotAdvertisementCommand ignored ->
                                        assertPlotDetails((PlotDetails) details, emptySet());
                            }
                        });
    }

    @ParameterizedTest
    @DisplayName("Should assign claims properly")
    @MethodSource("commandsWithCustomClaimsDataProvider")
    void shouldAssignClaimsProperly(
            final CreateAdvertisementCommand command,
            final Set<AdvertisementClaim> expectedClaims) {

        // Given
        final User foundUser = getDummyUserBuilder().build();

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));

        // When
        handler.handle(command);

        // Then
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        Assertions.assertThat(advertisementArgumentCaptor.getValue().getDetails())
                .satisfies(
                        details ->
                                Assertions.assertThat(details.getClaims())
                                        .isEqualTo(expectedClaims));
    }

    @ParameterizedTest
    @DisplayName("Should assign photos properly")
    @MethodSource("commandsWithCustomPhotosDataProvider")
    void shouldAssignPhotosProperly(
            final CreateAdvertisementCommand command,
            final Set<AdvertisementPhoto> expectedPhotos) {

        // Given
        final User foundUser = getDummyUserBuilder().build();

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));

        // When
        handler.handle(command);

        // Then
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        Assertions.assertThat(advertisementArgumentCaptor.getValue().getPhotos())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrderElementsOf(expectedPhotos);
    }

    private static void assertCommercialDetails(
            final CommercialDetails details,
            final CreateCommercialAdvertisementCommand command,
            final Set<AdvertisementClaim> expectedClaims) {

        Assertions.assertThat(details)
                .satisfies(
                        commercialDetails -> {
                            Assertions.assertThat(commercialDetails.getBuildingType())
                                    .isExactlyInstanceOf(CommercialBuildingType.class);

                            Assertions.assertThat(commercialDetails.getTypeOfMarket())
                                    .isExactlyInstanceOf(TypeOfMarket.class);

                            Assertions.assertThat(commercialDetails.getClaims())
                                    .isEqualTo(expectedClaims);

                            Assertions.assertThat(commercialDetails.getNumberOfRooms().value())
                                    .isEqualTo(command.numberOfRooms());

                            Assertions.assertThat(commercialDetails.getFloor().value())
                                    .isEqualTo(command.floor());

                            Assertions.assertThat(commercialDetails.getFloors().value())
                                    .isEqualTo(command.floors());

                            Assertions.assertThat(commercialDetails.getBuiltYear().value())
                                    .isEqualTo(command.builtYear());
                        });
    }

    private static void assertFlatDetails(
            final FlatDetails details,
            final CreateFlatAdvertisementCommand command,
            final Set<AdvertisementClaim> expectedClaims) {

        Assertions.assertThat(details)
                .satisfies(
                        flatDetails -> {
                            Assertions.assertThat(flatDetails.getBuildingType())
                                    .isExactlyInstanceOf(FlatBuildingType.class);

                            Assertions.assertThat(flatDetails.getTypeOfMarket())
                                    .isExactlyInstanceOf(TypeOfMarket.class);

                            Assertions.assertThat(flatDetails.getClaims())
                                    .isEqualTo(expectedClaims);

                            Assertions.assertThat(flatDetails.getNumberOfRooms().value())
                                    .isEqualTo(command.numberOfRooms());

                            Assertions.assertThat(flatDetails.getFloor().value())
                                    .isEqualTo(command.floor());

                            Assertions.assertThat(flatDetails.getFloors().value())
                                    .isEqualTo(command.floors());

                            Assertions.assertThat(flatDetails.getBuiltYear().value())
                                    .isEqualTo(command.builtYear());
                        });
    }

    private static void assertHouseDetails(
            final HouseDetails details,
            final CreateHouseAdvertisementCommand command,
            final Set<AdvertisementClaim> expectedClaims) {

        Assertions.assertThat(details)
                .satisfies(
                        houseDetails -> {
                            Assertions.assertThat(houseDetails.getBuildingType())
                                    .isExactlyInstanceOf(HouseBuildingType.class);

                            Assertions.assertThat(houseDetails.getTypeOfMarket())
                                    .isExactlyInstanceOf(TypeOfMarket.class);

                            Assertions.assertThat(houseDetails.getClaims())
                                    .isEqualTo(expectedClaims);

                            Assertions.assertThat(houseDetails.getNumberOfRooms().value())
                                    .isEqualTo(command.numberOfRooms());

                            Assertions.assertThat(houseDetails.getFloors().value())
                                    .isEqualTo(command.floors());

                            Assertions.assertThat(houseDetails.getBuiltYear().value())
                                    .isEqualTo(command.builtYear());
                        });
    }

    private static void assertPlotDetails(
            final PlotDetails details, final Set<AdvertisementClaim> expectedClaims) {

        Assertions.assertThat(details)
                .satisfies(
                        plotDetails -> {
                            Assertions.assertThat(plotDetails.getBuildingType())
                                    .isExactlyInstanceOf(PlotBuildingType.class);

                            Assertions.assertThat(plotDetails.getTypeOfMarket())
                                    .isExactlyInstanceOf(TypeOfMarket.class);

                            Assertions.assertThat(plotDetails.getClaims())
                                    .isEqualTo(expectedClaims);
                        });
    }

    private static Stream<Arguments> commandsDataProvider() {
        return Stream.of(
                Arguments.of(getFlatCommand(null, null)),
                Arguments.of(getHouseCommand(null, null)),
                Arguments.of(getCommercialCommand(null, null)),
                Arguments.of(getPlotCommand(null, null)));
    }

    private static Stream<Arguments> commandsWithCustomClaimsDataProvider() {
        final Map<String, String> claims = Map.of("abc", "cde");
        final Set<AdvertisementClaim> expectedClaims = Set.of(new AdvertisementClaim("abc", "cde"));

        return Stream.of(
                Arguments.of(getFlatCommand(null, null), emptySet()),
                Arguments.of(getFlatCommand(Map.of(), null), emptySet()),
                Arguments.of(getFlatCommand(claims, null), expectedClaims),
                Arguments.of(getHouseCommand(null, null), emptySet()),
                Arguments.of(getHouseCommand(Map.of(), null), emptySet()),
                Arguments.of(getHouseCommand(claims, null), expectedClaims),
                Arguments.of(getCommercialCommand(null, null), emptySet()),
                Arguments.of(getCommercialCommand(Map.of(), null), emptySet()),
                Arguments.of(getCommercialCommand(claims, null), expectedClaims),
                Arguments.of(getPlotCommand(null, null), emptySet()),
                Arguments.of(getPlotCommand(Map.of(), null), emptySet()),
                Arguments.of(getPlotCommand(claims, null), expectedClaims));
    }

    private static Stream<Arguments> commandsWithCustomPhotosDataProvider() {
        final List<CreateAdvertisementCommand.AdvertisementPhoto> photos =
                List.of(new CreateAdvertisementCommand.AdvertisementPhoto("https://abc", 0));

        final Set<AdvertisementPhoto> expectedPhotos =
                Set.of(
                        AdvertisementPhoto.create(
                                new Url(photos.getFirst().url()), photos.getFirst().position()));

        return Stream.of(
                Arguments.of(getFlatCommand(null, null), emptySet()),
                Arguments.of(getFlatCommand(null, List.of()), emptySet()),
                Arguments.of(getFlatCommand(null, photos), expectedPhotos),
                Arguments.of(getHouseCommand(null, null), emptySet()),
                Arguments.of(getHouseCommand(null, List.of()), emptySet()),
                Arguments.of(getHouseCommand(null, photos), expectedPhotos),
                Arguments.of(getCommercialCommand(null, null), emptySet()),
                Arguments.of(getCommercialCommand(null, List.of()), emptySet()),
                Arguments.of(getCommercialCommand(null, photos), expectedPhotos),
                Arguments.of(getPlotCommand(null, null), emptySet()),
                Arguments.of(getPlotCommand(null, List.of()), emptySet()),
                Arguments.of(getPlotCommand(null, photos), expectedPhotos));
    }

    private static CreateFlatAdvertisementCommand getFlatCommand(
            final Map<String, String> claims,
            final List<CreateAdvertisementCommand.AdvertisementPhoto> photos) {

        return new CreateFlatAdvertisementCommand(
                "any title content",
                "any description content",
                BigDecimal.valueOf(1_950_000d),
                Identifier.generate().getValue(),
                getDummyEmail(),
                1,
                3,
                4,
                2011,
                TypeOfMarket.PRIMARY.toString(),
                photos,
                FlatBuildingType.APARTMENT.toString(),
                BigDecimal.valueOf(45.25d),
                claims,
                false);
    }

    private static CreateHouseAdvertisementCommand getHouseCommand(
            final Map<String, String> claims,
            final List<CreateAdvertisementCommand.AdvertisementPhoto> photos) {

        return new CreateHouseAdvertisementCommand(
                "any title content",
                "any description content",
                BigDecimal.valueOf(1_950_000d),
                Identifier.generate().getValue(),
                getDummyEmail(),
                1,
                4,
                2011,
                TypeOfMarket.PRIMARY.toString(),
                photos,
                HouseBuildingType.DETACHED.toString(),
                BigDecimal.valueOf(45.25d),
                claims,
                false);
    }

    private static CreateCommercialAdvertisementCommand getCommercialCommand(
            final Map<String, String> claims,
            final List<CreateAdvertisementCommand.AdvertisementPhoto> photos) {

        return new CreateCommercialAdvertisementCommand(
                "any title content",
                "any description content",
                BigDecimal.valueOf(1_950_000d),
                Identifier.generate().getValue(),
                getDummyEmail(),
                1,
                3,
                4,
                2011,
                TypeOfMarket.PRIMARY.toString(),
                photos,
                CommercialBuildingType.HALL.toString(),
                BigDecimal.valueOf(45.25d),
                claims,
                false);
    }

    private static CreatePlotAdvertisementCommand getPlotCommand(
            final Map<String, String> claims,
            final List<CreateAdvertisementCommand.AdvertisementPhoto> photos) {

        return new CreatePlotAdvertisementCommand(
                "any title content",
                "any description content",
                BigDecimal.valueOf(1_950_000d),
                Identifier.generate().getValue(),
                getDummyEmail(),
                photos,
                PlotBuildingType.FOREST.toString(),
                BigDecimal.valueOf(1_450.25d),
                claims,
                false);
    }
}
