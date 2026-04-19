/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement.projection;

public non-sealed interface HouseAdvertisementDetailsProjection
        extends AdvertisementDetailsProjection {

    String getBuildingType();

    Integer getNumberOfRooms();

    Integer getFloors();

    Integer getBuiltYear();

    String getTypeOfMarket();
}
