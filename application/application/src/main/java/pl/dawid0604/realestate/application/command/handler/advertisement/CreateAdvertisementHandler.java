package pl.dawid0604.realestate.application.command.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static java.util.stream.Collectors.toSet;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.CreateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.CreateCommercialAdvertisementCommand;
import pl.dawid0604.realestate.application.command.CreateFlatAdvertisementCommand;
import pl.dawid0604.realestate.application.command.CreateHouseAdvertisementCommand;
import pl.dawid0604.realestate.application.command.CreatePlotAdvertisementCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementClaim;
import pl.dawid0604.realestate.domain.AdvertisementDetails;
import pl.dawid0604.realestate.domain.AdvertisementPhoto;
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
import pl.dawid0604.realestate.domain.Locality;
import pl.dawid0604.realestate.domain.Money;
import pl.dawid0604.realestate.domain.MoneyCurrency;
import pl.dawid0604.realestate.domain.NumberOfRooms;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.PlotDetails;
import pl.dawid0604.realestate.domain.Title;
import pl.dawid0604.realestate.domain.TypeOfMarket;
import pl.dawid0604.realestate.domain.Url;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class CreateAdvertisementHandler implements CommandHandler<CreateAdvertisementCommand, UUID> {
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    @Override
    public UUID handle(final CreateAdvertisementCommand command) {
        final User user =
                userRepository
                        .findByEmail(command.userEmail())
                        .orElseThrow(() -> new UserNotFoundException(command.userEmail()));

        user.verifyUser();
        final var builder = Advertisement.create();

        builder.title(new Title(command.title()));
        builder.description(new Description(command.description()));
        builder.price(new Money(command.price(), MoneyCurrency.PLN));
        builder.locality(new Locality(Identifier.of(command.localityId())));
        builder.userId(user.getId());
        builder.photos(getPhotos(command.photosOrEmpty()));
        builder.featured(command.featured());
        builder.details(getDetails(command));

        final Advertisement builtAdvertisement = builder.build();
        advertisementRepository.save(builtAdvertisement);

        return builtAdvertisement.getId().getValue();
    }

    @Override
    public Class<CreateAdvertisementCommand> getCommandType() {
        return CreateAdvertisementCommand.class;
    }

    private static Set<AdvertisementPhoto> getPhotos(
            final Collection<CreateAdvertisementCommand.AdvertisementPhoto> photos) {

        return photos.stream()
                .map(p -> AdvertisementPhoto.create(new Url(p.url()), p.position()))
                .collect(toSet());
    }

    private static AdvertisementDetails<?> getDetails(final CreateAdvertisementCommand command) {
        return switch (command) {
            case CreateFlatAdvertisementCommand cmd ->
                    new FlatDetails(
                            new Area(cmd.area()),
                            BuildingType.of(FlatBuildingType.class, cmd.buildingType()),
                            mapClaims(cmd.claimsOrEmpty()),
                            new NumberOfRooms(cmd.numberOfRooms()),
                            new Floor(cmd.floor()),
                            new Floor(cmd.floors()),
                            new BuiltYear(cmd.builtYear()),
                            TypeOfMarket.of(cmd.typeOfMarket()));

            case CreateHouseAdvertisementCommand cmd ->
                    new HouseDetails(
                            new Area(cmd.area()),
                            BuildingType.of(HouseBuildingType.class, cmd.buildingType()),
                            mapClaims(cmd.claimsOrEmpty()),
                            new NumberOfRooms(cmd.numberOfRooms()),
                            new Floor(cmd.floors()),
                            new BuiltYear(cmd.builtYear()),
                            TypeOfMarket.of(cmd.typeOfMarket()));

            case CreateCommercialAdvertisementCommand cmd ->
                    new CommercialDetails(
                            new Area(cmd.area()),
                            BuildingType.of(CommercialBuildingType.class, cmd.buildingType()),
                            mapClaims(cmd.claimsOrEmpty()),
                            new NumberOfRooms(cmd.numberOfRooms()),
                            new Floor(cmd.floor()),
                            new Floor(cmd.floors()),
                            new BuiltYear(cmd.builtYear()),
                            TypeOfMarket.of(cmd.typeOfMarket()));

            case CreatePlotAdvertisementCommand cmd ->
                    new PlotDetails(
                            new Area(cmd.area()),
                            BuildingType.of(PlotBuildingType.class, cmd.buildingType()),
                            mapClaims(cmd.claimsOrEmpty()));
        };
    }

    private static Set<AdvertisementClaim> mapClaims(final Map<String, String> claims) {
        return claims.entrySet().stream()
                .map(e -> new AdvertisementClaim(e.getKey(), e.getValue()))
                .collect(toSet());
    }
}
