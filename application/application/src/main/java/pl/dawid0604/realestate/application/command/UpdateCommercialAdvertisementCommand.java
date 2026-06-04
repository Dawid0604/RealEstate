/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import static java.util.Collections.emptyMap;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record UpdateCommercialAdvertisementCommand(
        String slug,
        String title,
        String description,
        BigDecimal price,
        UUID localityId,
        String userEmail,
        Integer numberOfRooms,
        Integer floor,
        Integer floors,
        Integer builtYear,
        String typeOfMarket,
        String buildingType,
        BigDecimal area,
        Map<String, String> claims,
        Boolean featured)
        implements UpdateAdvertisementCommand {

    public UpdateCommercialAdvertisementCommand {
        claims = claims != null ? Map.copyOf(claims) : emptyMap();
    }

    @Override
    public Map<String, String> claims() {
        return Map.copyOf(claims);
    }
}
