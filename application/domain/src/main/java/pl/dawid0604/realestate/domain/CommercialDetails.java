/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.Set;

public final class CommercialDetails extends FlooredDetails<CommercialBuildingType> {

    public CommercialDetails(
            final CommercialBuildingType buildingType,
            final Set<AdvertisementClaim> claims,
            final NumberOfRooms numberOfRooms,
            final Floor floor,
            final Floor floors,
            final BuiltYear builtYear,
            final TypeOfMarket typeOfMarket) {

        super(buildingType, claims, numberOfRooms, floor, floors, builtYear, typeOfMarket);
    }
}
