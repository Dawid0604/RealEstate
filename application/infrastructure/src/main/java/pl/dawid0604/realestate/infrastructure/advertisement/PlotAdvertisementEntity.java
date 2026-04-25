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
import pl.dawid0604.realestate.domain.PlotBuildingType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "plot_advertisements")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("PMD.ImmutableField")
non-sealed class PlotAdvertisementEntity
        extends AdvertisementEntity<PlotAdvertisementClaimEntity, PlotAdvertisementPhotoEntity> {

    @Enumerated(STRING)
    private PlotBuildingType plotType;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    PlotAdvertisementEntity(
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
            final Set<PlotAdvertisementClaimEntity> claims,
            final Set<PlotAdvertisementPhotoEntity> photos,
            final PlotBuildingType plotType) {

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

        this.plotType = plotType;
    }
}
