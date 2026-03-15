/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.Set;

public final class PlotDetails extends AdvertisementDetails {
    private final TypeOfMarket typeOfMarket;

    public PlotDetails(
            final Area area,
            final PlotBuildingType buildingType,
            final Set<AdvertisementClaim> claims) {

        super(area, buildingType, claims);
        this.typeOfMarket = TypeOfMarket.SECONDARY;
    }
}
