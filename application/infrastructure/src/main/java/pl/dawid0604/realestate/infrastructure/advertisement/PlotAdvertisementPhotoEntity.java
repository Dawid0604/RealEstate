/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static lombok.AccessLevel.PACKAGE;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = PACKAGE)
@Table(name = "plot_advertisements_photos")
non-sealed class PlotAdvertisementPhotoEntity
        extends AdvertisementPhotoEntity<PlotAdvertisementEntity> {

    PlotAdvertisementPhotoEntity(final UUID id, final int position, final String url) {
        super(id, position, url);
    }
}
