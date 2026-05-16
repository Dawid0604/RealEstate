/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record CreatePlotAdvertisementCommand(
        String title,
        String description,
        BigDecimal price,
        UUID localityId,
        String userEmail,
        Set<AdvertisementPhoto> photos,
        String buildingType,
        BigDecimal area,
        Map<String, String> claims,
        Boolean featured)
        implements CreateAdvertisementCommand {

    public CreatePlotAdvertisementCommand {
        photos = photos != null ? Set.copyOf(photos) : emptySet();
        claims = claims != null ? Map.copyOf(claims) : emptyMap();
    }

    @Override
    public Set<AdvertisementPhoto> photos() {
        return Set.copyOf(photos);
    }

    @Override
    public Map<String, String> claims() {
        return Map.copyOf(claims);
    }
}
