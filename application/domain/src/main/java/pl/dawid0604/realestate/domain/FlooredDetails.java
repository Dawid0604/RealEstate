/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.Set;

public abstract sealed class FlooredDetails extends AdvertisementDetails
        permits FlatDetails, CommercialDetails {

    private final NumberOfRooms numberOfRooms;
    private final Floor floor;
    private final Floor floors;
    private final BuiltYear builtYear;
    private final TypeOfMarket typeOfMarket;

    protected FlooredDetails(
            final Area area,
            final FlatBuildingType buildingType,
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

        if (floor.value() > floors.value()) {
            throw new InvalidArgumentValueException("Floor cannot be higher than floors");
        }

        this.numberOfRooms = numberOfRooms;
        this.floor = floor;
        this.floors = floors;
        this.builtYear = builtYear;
        this.typeOfMarket = typeOfMarket;
    }
}
