/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public sealed interface UpdateAdvertisementCommand extends Command
        permits UpdateCommercialAdvertisementCommand,
                UpdateFlatAdvertisementCommand,
                UpdateHouseAdvertisementCommand,
                UpdatePlotAdvertisementCommand {

    String slug();

    String title();

    String description();

    BigDecimal price();

    UUID localityId();

    String buildingType();

    BigDecimal area();

    Map<String, String> claims();

    String userEmail();
}
