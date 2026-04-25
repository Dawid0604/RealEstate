/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static jakarta.persistence.EnumType.STRING;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "flat_advertisements")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("PMD.ImmutableField")
non-sealed class FlatAdvertisementEntity
        extends AdvertisementEntity<FlatAdvertisementClaimEntity, FlatAdvertisementPhotoEntity> {

    @Enumerated(STRING)
    private FlatBuildingType buildingType;

    private Integer numberOfRooms;
    private Integer floor;
    private Integer floors;
    private Integer builtYear;

    @Enumerated(STRING)
    private TypeOfMarket typeOfMarket;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    FlatAdvertisementEntity(
            final UUID id,
            final String slug,
            final String title,
            final String description,
            final BigDecimal price,
            final BigDecimal area,
            final BigDecimal pricePerSquareMeter,
            final UUID localityId,
            final UUID userId,
            final boolean isFeatured,
            final AdvertisementStatus status,
            final Set<FlatAdvertisementClaimEntity> claims,
            final Set<FlatAdvertisementPhotoEntity> photos,
            final FlatBuildingType buildingType,
            final Integer numberOfRooms,
            final Integer floor,
            final Integer floors,
            final Integer builtYear,
            final TypeOfMarket typeOfMarket) {

        super(
                id,
                slug,
                title,
                description,
                price,
                area,
                pricePerSquareMeter,
                localityId,
                userId,
                isFeatured,
                status,
                claims,
                photos);

        this.buildingType = buildingType;
        this.numberOfRooms = numberOfRooms;
        this.floor = floor;
        this.floors = floors;
        this.builtYear = builtYear;
        this.typeOfMarket = typeOfMarket;
    }
}
