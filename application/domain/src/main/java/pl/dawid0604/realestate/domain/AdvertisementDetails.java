/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.HashSet;
import java.util.Set;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public abstract sealed class AdvertisementDetails<B extends BuildingType>
        permits FlooredDetails, HouseDetails, PlotDetails {

    private final Area area;
    private final B buildingType;
    private final Set<AdvertisementClaim> claims;

    protected AdvertisementDetails(
            final Area area, final B buildingType, final Set<AdvertisementClaim> claims) {

        requireNonNull(area, "Area");
        requireNonNull(buildingType, "BuildingType");

        this.area = area;
        this.buildingType = buildingType;
        this.claims = claims == null ? new HashSet<>() : new HashSet<>(claims);
    }

    public final Set<AdvertisementClaim> getClaims() {
        return Set.copyOf(claims);
    }

    public final Area getArea() {
        return area;
    }

    public final B getBuildingType() {
        return buildingType;
    }

    protected final void requireNonNull(final Object field, final String name) {
        if (field == null) {
            throw new InvalidArgumentValueException(name + " cannot be null");
        }
    }
}
