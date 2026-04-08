/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import pl.dawid0604.realestate.application.validation.ValidArea;
import pl.dawid0604.realestate.application.validation.ValidBuildingType;
import pl.dawid0604.realestate.application.validation.ValidBuiltYear;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidFloor;
import pl.dawid0604.realestate.application.validation.ValidFloors;
import pl.dawid0604.realestate.application.validation.ValidLocalityId;
import pl.dawid0604.realestate.application.validation.ValidNumberOfRooms;
import pl.dawid0604.realestate.application.validation.ValidPrice;
import pl.dawid0604.realestate.application.validation.ValidTitle;
import pl.dawid0604.realestate.application.validation.ValidTypeOfMarket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateFlatAdvertisementCommand(
        @ValidTitle String title,
        String description,
        @ValidPrice BigDecimal price,
        @ValidLocalityId UUID localityId,
        @ValidEmail String userEmail,
        @ValidNumberOfRooms Integer numberOfRooms,
        @ValidFloor Integer floor,
        @ValidFloors Integer floors,
        @ValidBuiltYear Integer builtYear,
        @ValidTypeOfMarket String typeOfMarket,
        List<AdvertisementPhoto> photos,
        @ValidBuildingType String buildingType,
        @ValidArea BigDecimal area,
        Map<String, String> claims,
        Boolean featured)
        implements CreateAdvertisementCommand {

    public CreateFlatAdvertisementCommand {
        photos = photos != null ? List.copyOf(photos) : emptyList();
        claims = claims != null ? Map.copyOf(claims) : emptyMap();
    }

    @Override
    public List<AdvertisementPhoto> photos() {
        return List.copyOf(photos);
    }

    @Override
    public Map<String, String> claims() {
        return Map.copyOf(claims);
    }
}
