/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.advertisement;

import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.*;
import static pl.dawid0604.realestate.application.fixture.AdvertisementFixture.getDummyCommercialDetails;
import static pl.dawid0604.realestate.application.fixture.AdvertisementFixture.getDummyFlatDetails;
import static pl.dawid0604.realestate.application.fixture.AdvertisementFixture.getDummyHouseDetails;
import static pl.dawid0604.realestate.application.fixture.AdvertisementFixture.getDummyPlotDetails;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.command.UpdateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdateCommercialAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdateFlatAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdateHouseAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdatePlotAdvertisementCommand;
import pl.dawid0604.realestate.application.fixture.AdvertisementFixture;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementClaim;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.CommercialDetails;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.FlatDetails;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.HouseDetails;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.PlotDetails;
import pl.dawid0604.realestate.domain.TypeOfMarket;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;

@ExtendWith(MockitoExtension.class)
class UpdateAdvertisementHandlerTest {
    @Mock private AdvertisementRepository advertisementRepository;
    @Captor private ArgumentCaptor<Advertisement> advertisementArgumentCaptor;
    private UpdateAdvertisementHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new UpdateAdvertisementHandler(advertisementRepository);
    }

    @Test
    @DisplayName("Should throw exception when command is null")
    void shouldThrowExceptionWhenCommandIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("Command cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when flat advertisement not found")
    void shouldThrowExceptionWhenFlatAdvertisementNotFound() {
        // Given
        final UpdateFlatAdvertisementCommand command = getFlatCommand();

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getFlatCommand()))
                .isExactlyInstanceOf(AdvertisementNotFoundException.class);

        verify(advertisementRepository).findBySlug(command.slug(), AdvertisementType.FLAT);
    }

    @Test
    @DisplayName("Should throw exception when house advertisement not found")
    void shouldThrowExceptionWhenHouseAdvertisementNotFound() {
        // Given
        final UpdateHouseAdvertisementCommand command = getHouseCommand();

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getHouseCommand()))
                .isExactlyInstanceOf(AdvertisementNotFoundException.class);

        verify(advertisementRepository).findBySlug(command.slug(), AdvertisementType.HOUSE);
    }

    @Test
    @DisplayName("Should throw exception when commercial advertisement not found")
    void shouldThrowExceptionWhenCommercialAdvertisementNotFound() {
        // Given
        final UpdateCommercialAdvertisementCommand command = getCommercialCommand();

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getCommercialCommand()))
                .isExactlyInstanceOf(AdvertisementNotFoundException.class);

        verify(advertisementRepository).findBySlug(command.slug(), AdvertisementType.COMMERCIAL);
    }

    @Test
    @DisplayName("Should throw exception when plot advertisement not found")
    void shouldThrowExceptionWhenPlotAdvertisementNotFound() {
        // Given
        final UpdatePlotAdvertisementCommand command = getPlotCommand();

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getPlotCommand()))
                .isExactlyInstanceOf(AdvertisementNotFoundException.class);

        verify(advertisementRepository).findBySlug(command.slug(), AdvertisementType.PLOT);
    }

    @Test
    @DisplayName("Should handle flat successfully")
    void shouldHandleFlatSuccessfully() {
        // Given
        final UpdateFlatAdvertisementCommand command = getFlatCommand();
        final Advertisement advertisement =
                AdvertisementFixture.getDummyAdvertisementBuilder(getDummyFlatDetails()).build();

        given(advertisementRepository.findBySlug(command.slug(), AdvertisementType.FLAT))
                .willReturn(Optional.of(advertisement));

        // When
        handler.handle(command);

        // Then
        verify(advertisementRepository).clearClaims(advertisement);
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        assertAdvertisement(advertisementArgumentCaptor.getValue(), command);
    }

    @Test
    @DisplayName("Should handle house successfully")
    void shouldHandleHouseSuccessfully() {
        // Given
        final UpdateHouseAdvertisementCommand command = getHouseCommand();
        final Advertisement advertisement =
                AdvertisementFixture.getDummyAdvertisementBuilder(getDummyHouseDetails()).build();

        given(advertisementRepository.findBySlug(command.slug(), AdvertisementType.HOUSE))
                .willReturn(Optional.of(advertisement));

        // When
        handler.handle(command);

        // Then
        verify(advertisementRepository).clearClaims(advertisement);
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        assertAdvertisement(advertisementArgumentCaptor.getValue(), command);
    }

    @Test
    @DisplayName("Should handle commercial successfully")
    void shouldHandleCommercialSuccessfully() {
        // Given
        final UpdateCommercialAdvertisementCommand command = getCommercialCommand();
        final Advertisement advertisement =
                AdvertisementFixture.getDummyAdvertisementBuilder(getDummyCommercialDetails())
                        .build();

        given(advertisementRepository.findBySlug(command.slug(), AdvertisementType.COMMERCIAL))
                .willReturn(Optional.of(advertisement));

        // When
        handler.handle(command);

        // Then
        verify(advertisementRepository).clearClaims(advertisement);
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        assertAdvertisement(advertisementArgumentCaptor.getValue(), command);
    }

    @Test
    @DisplayName("Should handle plot successfully")
    void shouldHandlePlotSuccessfully() {
        // Given
        final UpdatePlotAdvertisementCommand command = getPlotCommand();
        final Advertisement advertisement =
                AdvertisementFixture.getDummyAdvertisementBuilder(getDummyPlotDetails()).build();

        given(advertisementRepository.findBySlug(command.slug(), AdvertisementType.PLOT))
                .willReturn(Optional.of(advertisement));

        // When
        handler.handle(command);

        // Then
        verify(advertisementRepository).clearClaims(advertisement);
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        assertAdvertisement(advertisementArgumentCaptor.getValue(), command);
    }

    private static void assertAdvertisement(
            final Advertisement advertisement, final UpdateAdvertisementCommand command) {

        Assertions.assertThat(advertisement)
                .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .returns(command.area(), a -> a.getArea().value())
                .returns(command.description(), a -> a.getDescription().value())
                .returns(command.price(), a -> a.getPrice().value())
                .returns(command.title(), a -> a.getTitle().value())
                .returns(command.localityId(), a -> a.getLocality().id().getValue());

        Assertions.assertThat(advertisement.getDetails())
                .satisfies(
                        val -> {
                            switch (command) {
                                case UpdateCommercialAdvertisementCommand commercialCommand ->
                                        Assertions.assertThat(val)
                                                .asInstanceOf(
                                                        InstanceOfAssertFactories.type(
                                                                CommercialDetails.class))
                                                .returns(
                                                        commercialCommand.buildingType(),
                                                        a -> a.getBuildingType().name())
                                                .returns(
                                                        commercialCommand.numberOfRooms(),
                                                        a -> a.getNumberOfRooms().value())
                                                .returns(
                                                        commercialCommand.floor(),
                                                        a -> a.getFloor().value())
                                                .returns(
                                                        commercialCommand.floors(),
                                                        a -> a.getFloors().value())
                                                .returns(
                                                        commercialCommand.builtYear(),
                                                        a -> a.getBuiltYear().value())
                                                .returns(
                                                        commercialCommand.typeOfMarket(),
                                                        a -> a.getTypeOfMarket().name());

                                case UpdateFlatAdvertisementCommand flatCommand ->
                                        Assertions.assertThat(val)
                                                .asInstanceOf(
                                                        InstanceOfAssertFactories.type(
                                                                FlatDetails.class))
                                                .returns(
                                                        flatCommand.buildingType(),
                                                        a -> a.getBuildingType().name())
                                                .returns(
                                                        flatCommand.numberOfRooms(),
                                                        a -> a.getNumberOfRooms().value())
                                                .returns(
                                                        flatCommand.floor(),
                                                        a -> a.getFloor().value())
                                                .returns(
                                                        flatCommand.floors(),
                                                        a -> a.getFloors().value())
                                                .returns(
                                                        flatCommand.builtYear(),
                                                        a -> a.getBuiltYear().value())
                                                .returns(
                                                        flatCommand.typeOfMarket(),
                                                        a -> a.getTypeOfMarket().name());

                                case UpdateHouseAdvertisementCommand houseCommand ->
                                        Assertions.assertThat(val)
                                                .asInstanceOf(
                                                        InstanceOfAssertFactories.type(
                                                                HouseDetails.class))
                                                .returns(
                                                        houseCommand.buildingType(),
                                                        a -> a.getBuildingType().name())
                                                .returns(
                                                        houseCommand.numberOfRooms(),
                                                        a -> a.getNumberOfRooms().value())
                                                .returns(
                                                        houseCommand.floors(),
                                                        a -> a.getFloors().value())
                                                .returns(
                                                        houseCommand.builtYear(),
                                                        a -> a.getBuiltYear().value())
                                                .returns(
                                                        houseCommand.typeOfMarket(),
                                                        a -> a.getTypeOfMarket().name());

                                case UpdatePlotAdvertisementCommand plotCommand ->
                                        Assertions.assertThat(val)
                                                .asInstanceOf(
                                                        InstanceOfAssertFactories.type(
                                                                PlotDetails.class))
                                                .returns(
                                                        plotCommand.buildingType(),
                                                        a -> a.getBuildingType().name());
                            }
                        });

        final Tuple[] incomingClaims =
                command.claims().entrySet().stream()
                        .map(c -> tuple(c.getKey(), c.getValue()))
                        .toArray(Tuple[]::new);

        Assertions.assertThat(advertisement.getDetails().getClaims())
                .extracting(AdvertisementClaim::key, AdvertisementClaim::value)
                .containsExactlyInAnyOrder(incomingClaims);
    }

    private static UpdateFlatAdvertisementCommand getFlatCommand() {
        return new UpdateFlatAdvertisementCommand(
                "any-slug-123",
                "any-title-123",
                "any-description",
                BigDecimal.valueOf(450_000),
                UUID.randomUUID(),
                UserFixture.getDummyEmail(),
                1,
                2,
                3,
                2011,
                TypeOfMarket.PRIMARY.name(),
                FlatBuildingType.APARTMENT.name(),
                BigDecimal.valueOf(45.25),
                getClaims(),
                true);
    }

    private static UpdateHouseAdvertisementCommand getHouseCommand() {
        return new UpdateHouseAdvertisementCommand(
                "any-slug-123",
                "any-title-123",
                "any-description",
                BigDecimal.valueOf(450_000),
                UUID.randomUUID(),
                UserFixture.getDummyEmail(),
                1,
                3,
                2011,
                TypeOfMarket.PRIMARY.name(),
                HouseBuildingType.DETACHED.name(),
                BigDecimal.valueOf(45.25),
                getClaims(),
                true);
    }

    private static UpdateCommercialAdvertisementCommand getCommercialCommand() {
        return new UpdateCommercialAdvertisementCommand(
                "any-slug-123",
                "any-title-123",
                "any-description",
                BigDecimal.valueOf(450_000),
                UUID.randomUUID(),
                UserFixture.getDummyEmail(),
                1,
                2,
                3,
                2011,
                TypeOfMarket.PRIMARY.name(),
                CommercialBuildingType.HALL.name(),
                BigDecimal.valueOf(45.25),
                getClaims(),
                true);
    }

    private static UpdatePlotAdvertisementCommand getPlotCommand() {
        return new UpdatePlotAdvertisementCommand(
                "any-slug-123",
                "any-title-123",
                "any-description",
                BigDecimal.valueOf(450_000),
                UUID.randomUUID(),
                UserFixture.getDummyEmail(),
                PlotBuildingType.CONSTRUCTION.name(),
                BigDecimal.valueOf(4522.25),
                getClaims(),
                true);
    }

    private static Map<String, String> getClaims() {
        return Map.of(
                "abc", "cde",
                "efh", "hjk");
    }
}
