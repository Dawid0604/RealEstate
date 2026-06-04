/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement.projection;

import pl.dawid0604.realestate.domain.CommercialBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

public non-sealed interface UserCommercialAdvertisementCardProjection
        extends UserAdvertisementCardProjection {

    CommercialBuildingType getBuildingType();

    Integer getNumberOfRooms();

    Integer getFloor();

    Integer getFloors();

    Integer getBuiltYear();

    TypeOfMarket getTypeOfMarket();
}
