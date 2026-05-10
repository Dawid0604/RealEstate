/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static lombok.AccessLevel.PACKAGE;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = PACKAGE)
@Table(name = "flat_advertisements_photos")
non-sealed class FlatAdvertisementPhotoEntity
        extends AdvertisementPhotoEntity<FlatAdvertisementEntity> {

    FlatAdvertisementPhotoEntity(final UUID id, final int position, final String url) {
        super(id, position, url);
    }
}
