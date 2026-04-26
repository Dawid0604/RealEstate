/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static lombok.AccessLevel.PACKAGE;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = PACKAGE)
@EqualsAndHashCode(callSuper = true)
@Table(name = "flat_advertisements_photos")
non-sealed class FlatAdvertisementPhotoEntity
        extends AdvertisementPhotoEntity<FlatAdvertisementEntity> {

    FlatAdvertisementPhotoEntity(final UUID id, final int position, final String url) {
        super(id, position, url);
    }
}
