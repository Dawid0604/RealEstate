/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static jakarta.persistence.EnumType.STRING;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "house_advertisements")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("PMD.ImmutableField")
non-sealed class HouseAdvertisementEntity
        extends AdvertisementEntity<HouseAdvertisementClaimEntity, HouseAdvertisementPhotoEntity> {

    @Enumerated(STRING)
    private HouseBuildingType buildingType;

    private Integer numberOfRooms;
    private Integer floors;
    private Integer builtYear;

    @Enumerated(STRING)
    private TypeOfMarket typeOfMarket;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    HouseAdvertisementEntity(
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
            final Set<HouseAdvertisementClaimEntity> claims,
            final Set<HouseAdvertisementPhotoEntity> photos,
            final HouseBuildingType buildingType,
            final Integer numberOfRooms,
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
        this.floors = floors;
        this.builtYear = builtYear;
        this.typeOfMarket = typeOfMarket;
    }
}
