/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement.projection;

import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

public non-sealed interface HouseAdvertisementDetailsProjection
        extends AdvertisementDetailsProjection {

    HouseBuildingType getBuildingType();

    Integer getNumberOfRooms();

    Integer getFloors();

    Integer getBuiltYear();

    TypeOfMarket getTypeOfMarket();
}
