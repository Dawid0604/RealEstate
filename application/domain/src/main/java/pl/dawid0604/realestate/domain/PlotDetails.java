/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.Set;

public final class PlotDetails extends AdvertisementDetails<PlotBuildingType> {
    private final TypeOfMarket typeOfMarket;

    public PlotDetails(final PlotBuildingType buildingType, final Set<AdvertisementClaim> claims) {
        super(buildingType, claims);
        this.typeOfMarket = TypeOfMarket.SECONDARY;
    }

    public TypeOfMarket getTypeOfMarket() {
        return typeOfMarket;
    }
}
