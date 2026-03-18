/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.Set;

public final class HouseDetails extends AdvertisementDetails {
    private final NumberOfRooms numberOfRooms;
    private final Floor floors;
    private final BuiltYear builtYear;
    private final TypeOfMarket typeOfMarket;

    public HouseDetails(
            final Area area,
            final HouseBuildingType buildingType,
            final Set<AdvertisementClaim> claims,
            final NumberOfRooms numberOfRooms,
            final Floor floors,
            final BuiltYear builtYear,
            final TypeOfMarket typeOfMarket) {

        super(area, buildingType, claims);
        requireNonNull(numberOfRooms, "NumberOfRooms");
        requireNonNull(floors, "Floors");
        requireNonNull(builtYear, "BuiltYear");
        requireNonNull(typeOfMarket, "TypeOfMarket");

        this.numberOfRooms = numberOfRooms;
        this.floors = floors;
        this.builtYear = builtYear;
        this.typeOfMarket = typeOfMarket;
    }

    public NumberOfRooms getNumberOfRooms() {
        return numberOfRooms;
    }

    public Floor getFloors() {
        return floors;
    }

    public BuiltYear getBuiltYear() {
        return builtYear;
    }

    public TypeOfMarket getTypeOfMarket() {
        return typeOfMarket;
    }
}
