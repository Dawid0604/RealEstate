/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.Set;

public final class FlatDetails extends FlooredDetails<FlatBuildingType> {

    public FlatDetails(
            final FlatBuildingType buildingType,
            final Set<AdvertisementClaim> claims,
            final NumberOfRooms numberOfRooms,
            final Floor floor,
            final Floor floors,
            final BuiltYear builtYear,
            final TypeOfMarket typeOfMarket) {

        super(buildingType, claims, numberOfRooms, floor, floors, builtYear, typeOfMarket);
    }
}
