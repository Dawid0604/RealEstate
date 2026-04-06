/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.Set;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public abstract sealed class FlooredDetails<B extends BuildingType> extends AdvertisementDetails<B>
        permits FlatDetails, CommercialDetails {

    private final NumberOfRooms numberOfRooms;
    private final Floor floor;
    private final Floor floors;
    private final BuiltYear builtYear;
    private final TypeOfMarket typeOfMarket;

    protected FlooredDetails(
            final Area area,
            final B buildingType,
            final Set<AdvertisementClaim> claims,
            final NumberOfRooms numberOfRooms,
            final Floor floor,
            final Floor floors,
            final BuiltYear builtYear,
            final TypeOfMarket typeOfMarket) {

        super(area, buildingType, claims);
        requireNonNull(numberOfRooms, "NumberOfRooms");
        requireNonNull(floor, "Floor");
        requireNonNull(floors, "Floors");
        requireNonNull(builtYear, "BuiltYear");
        requireNonNull(typeOfMarket, "TypeOfMarket");
        requireValidFloor(floor, floors);

        this.numberOfRooms = numberOfRooms;
        this.floor = floor;
        this.floors = floors;
        this.builtYear = builtYear;
        this.typeOfMarket = typeOfMarket;
    }

    public final NumberOfRooms getNumberOfRooms() {
        return numberOfRooms;
    }

    public final Floor getFloor() {
        return floor;
    }

    public final Floor getFloors() {
        return floors;
    }

    public final BuiltYear getBuiltYear() {
        return builtYear;
    }

    public final TypeOfMarket getTypeOfMarket() {
        return typeOfMarket;
    }

    private static void requireValidFloor(final Floor floor, final Floor floors) {
        if (floor.value() != null && floors.value() != null && floor.value() > floors.value()) {
            throw new InvalidArgumentValueException("Floor cannot be higher than floors");
        }
    }
}
