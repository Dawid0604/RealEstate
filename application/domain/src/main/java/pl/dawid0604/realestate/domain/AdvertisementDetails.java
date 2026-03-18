/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.HashSet;
import java.util.Set;

public abstract sealed class AdvertisementDetails
        permits FlooredDetails, HouseDetails, PlotDetails {

    private final Area area;
    private final BuildingType type;
    private final Set<AdvertisementClaim> claims;

    protected AdvertisementDetails(
            final Area area,
            final BuildingType buildingType,
            final Set<AdvertisementClaim> claims) {

        requireNonNull(area, "Area");
        requireNonNull(buildingType, "BuildingType");

        this.area = area;
        this.type = buildingType;
        this.claims = claims == null ? new HashSet<>() : new HashSet<>(claims);
    }

    public final Set<AdvertisementClaim> getClaims() {
        return Set.copyOf(claims);
    }

    public final Area getArea() {
        return area;
    }

    public final BuildingType getType() {
        return type;
    }

    protected final void requireNonNull(final Object field, final String name) {
        if (field == null) {
            throw new InvalidArgumentValueException(name + " cannot be null");
        }
    }
}
