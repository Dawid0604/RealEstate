/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.projection.advertisement;

public non-sealed interface CommercialAdvertisementDetailsProjection
        extends AdvertisementDetailsProjection {

    String getBuildingType();

    Integer getNumberOfRooms();

    Integer getFloor();

    Integer getFloors();

    Integer getBuiltYear();

    String getTypeOfMarket();
}
