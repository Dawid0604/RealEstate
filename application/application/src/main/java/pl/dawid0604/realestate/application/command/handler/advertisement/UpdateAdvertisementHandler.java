/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static java.util.stream.Collectors.toSet;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdateCommercialAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdateFlatAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdateHouseAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdatePlotAdvertisementCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementClaim;
import pl.dawid0604.realestate.domain.AdvertisementDetails;
import pl.dawid0604.realestate.domain.AdvertisementLocality;
import pl.dawid0604.realestate.domain.Area;
import pl.dawid0604.realestate.domain.BuildingType;
import pl.dawid0604.realestate.domain.BuiltYear;
import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.CommercialDetails;
import pl.dawid0604.realestate.domain.Description;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.FlatDetails;
import pl.dawid0604.realestate.domain.Floor;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.HouseDetails;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.MoneyCurrency;
import pl.dawid0604.realestate.domain.NumberOfRooms;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.PlotDetails;
import pl.dawid0604.realestate.domain.Price;
import pl.dawid0604.realestate.domain.Title;
import pl.dawid0604.realestate.domain.TypeOfMarket;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateAdvertisementHandler implements CommandHandler<UpdateAdvertisementCommand, Void> {
    private final AdvertisementRepository advertisementRepository;

    @Override
    public Void handle(final UpdateAdvertisementCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        Advertisement advertisement =
                advertisementRepository
                        .findBySlug(command.slug(), getAdvertisementType(command))
                        .orElseThrow(() -> new AdvertisementNotFoundException(command.slug()));

        advertisement = advertisement.updateArea(new Area(command.area()));
        advertisement = advertisement.updateDescription(new Description(command.description()));
        advertisement = advertisement.updateDetails(getDetails(command));
        advertisement = advertisement.updatePrice(new Price(command.price(), MoneyCurrency.PLN));
        advertisement = advertisement.updateTitle(new Title(command.title()));
        advertisement =
                advertisement.updateLocality(
                        new AdvertisementLocality(Identifier.of(command.localityId())));

        advertisementRepository.clearClaims(advertisement);
        advertisementRepository.save(advertisement);
        return null;
    }

    private static AdvertisementType getAdvertisementType(
            final UpdateAdvertisementCommand command) {

        return switch (command) {
            case UpdateCommercialAdvertisementCommand ignored -> AdvertisementType.COMMERCIAL;
            case UpdateFlatAdvertisementCommand ignored -> AdvertisementType.FLAT;
            case UpdateHouseAdvertisementCommand ignored -> AdvertisementType.HOUSE;
            case UpdatePlotAdvertisementCommand ignored -> AdvertisementType.PLOT;
        };
    }

    private static AdvertisementDetails<?> getDetails(final UpdateAdvertisementCommand command) {
        return switch (command) {
            case UpdateFlatAdvertisementCommand cmd ->
                    new FlatDetails(
                            BuildingType.of(FlatBuildingType.class, cmd.buildingType()),
                            mapClaims(cmd.claims()),
                            new NumberOfRooms(cmd.numberOfRooms()),
                            new Floor(cmd.floor()),
                            new Floor(cmd.floors()),
                            new BuiltYear(cmd.builtYear()),
                            TypeOfMarket.of(cmd.typeOfMarket()));

            case UpdateHouseAdvertisementCommand cmd ->
                    new HouseDetails(
                            BuildingType.of(HouseBuildingType.class, cmd.buildingType()),
                            mapClaims(cmd.claims()),
                            new NumberOfRooms(cmd.numberOfRooms()),
                            new Floor(cmd.floors()),
                            new BuiltYear(cmd.builtYear()),
                            TypeOfMarket.of(cmd.typeOfMarket()));

            case UpdateCommercialAdvertisementCommand cmd ->
                    new CommercialDetails(
                            BuildingType.of(CommercialBuildingType.class, cmd.buildingType()),
                            mapClaims(cmd.claims()),
                            new NumberOfRooms(cmd.numberOfRooms()),
                            new Floor(cmd.floor()),
                            new Floor(cmd.floors()),
                            new BuiltYear(cmd.builtYear()),
                            TypeOfMarket.of(cmd.typeOfMarket()));

            case UpdatePlotAdvertisementCommand cmd ->
                    new PlotDetails(
                            BuildingType.of(PlotBuildingType.class, cmd.buildingType()),
                            mapClaims(cmd.claims()));
        };
    }

    private static Set<AdvertisementClaim> mapClaims(final Map<String, String> claims) {
        return claims.entrySet().stream()
                .map(e -> new AdvertisementClaim(Identifier.generate(), e.getKey(), e.getValue()))
                .collect(toSet());
    }

    @Override
    public Class<UpdateAdvertisementCommand> getCommandType() {
        return UpdateAdvertisementCommand.class;
    }
}
