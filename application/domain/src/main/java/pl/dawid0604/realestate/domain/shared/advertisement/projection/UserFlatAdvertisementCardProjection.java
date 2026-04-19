/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement.projection;

public non-sealed interface UserFlatAdvertisementCardProjection
        extends UserAdvertisementCardProjection {

    String getBuildingType();

    Integer getNumberOfRooms();

    Integer getFloor();

    Integer getFloors();

    Integer getBuiltYear();

    String getTypeOfMarket();
}
