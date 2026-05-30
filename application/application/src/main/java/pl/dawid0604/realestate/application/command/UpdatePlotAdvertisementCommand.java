/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import static java.util.Collections.emptyMap;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record UpdatePlotAdvertisementCommand(
        String slug,
        String title,
        String description,
        BigDecimal price,
        UUID localityId,
        String userEmail,
        String buildingType,
        BigDecimal area,
        Map<String, String> claims,
        Boolean featured)
        implements UpdateAdvertisementCommand {

    public UpdatePlotAdvertisementCommand {
        claims = claims != null ? Map.copyOf(claims) : emptyMap();
    }

    @Override
    public Map<String, String> claims() {
        return Map.copyOf(claims);
    }
}
