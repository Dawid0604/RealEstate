package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidArea;
import pl.dawid0604.realestate.application.validation.ValidBuildingType;
import pl.dawid0604.realestate.application.validation.ValidDescription;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidLocalityId;
import pl.dawid0604.realestate.application.validation.ValidPrice;
import pl.dawid0604.realestate.application.validation.ValidTitle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreatePlotAdvertisementCommand(
        @ValidTitle String title,
        @ValidDescription String description,
        @ValidPrice BigDecimal price,
        @ValidLocalityId UUID localityId,
        @ValidEmail String userEmail,
        List<AdvertisementPhoto> photos,
        @ValidBuildingType String buildingType,
        @ValidArea BigDecimal area,
        Map<String, String> claims,
        Boolean featured)
        implements CreateAdvertisementCommand {}
