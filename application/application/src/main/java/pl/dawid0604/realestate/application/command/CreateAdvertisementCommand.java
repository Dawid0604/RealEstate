package pl.dawid0604.realestate.application.command;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public sealed interface CreateAdvertisementCommand extends Command
        permits CreateCommercialAdvertisementCommand,
                CreateFlatAdvertisementCommand,
                CreateHouseAdvertisementCommand,
                CreatePlotAdvertisementCommand {

    String title();

    String description();

    BigDecimal price();

    UUID localityId();

    String buildingType();

    BigDecimal area();

    Map<String, String> claims();

    default Map<String, String> claimsOrEmpty() {
        return claims() == null ? emptyMap() : claims();
    }

    UUID userId();

    List<AdvertisementPhoto> photos();

    default List<AdvertisementPhoto> photosOrEmpty() {
        return photos() == null ? emptyList() : photos();
    }

    Boolean featured();

    record AdvertisementPhoto(String url, int position) {}
}
