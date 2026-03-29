package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidArea;
import pl.dawid0604.realestate.application.validation.ValidBuildingType;
import pl.dawid0604.realestate.application.validation.ValidBuiltYear;
import pl.dawid0604.realestate.application.validation.ValidDescription;
import pl.dawid0604.realestate.application.validation.ValidFloor;
import pl.dawid0604.realestate.application.validation.ValidFloors;
import pl.dawid0604.realestate.application.validation.ValidLocalityId;
import pl.dawid0604.realestate.application.validation.ValidNumberOfRooms;
import pl.dawid0604.realestate.application.validation.ValidPrice;
import pl.dawid0604.realestate.application.validation.ValidTitle;
import pl.dawid0604.realestate.application.validation.ValidTypeOfMarket;
import pl.dawid0604.realestate.application.validation.ValidUserId;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateCommercialAdvertisementCommand(
        @ValidTitle String title,
        @ValidDescription String description,
        @ValidPrice BigDecimal price,
        @ValidLocalityId UUID localityId,
        @ValidUserId UUID userId,
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
        implements CreateAdvertisementCommand {}
