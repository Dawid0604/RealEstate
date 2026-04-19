/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

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

    String userEmail();

    List<AdvertisementPhoto> photos();

    Boolean featured();

    record AdvertisementPhoto(String url, int position) {}
}
