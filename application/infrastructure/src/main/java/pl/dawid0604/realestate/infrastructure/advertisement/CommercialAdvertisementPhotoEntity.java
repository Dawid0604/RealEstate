/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "commercial_advertisements_photos")
non-sealed class CommercialAdvertisementPhotoEntity
        extends AdvertisementPhotoEntity<CommercialAdvertisementEntity> {

    CommercialAdvertisementPhotoEntity(final UUID id, final int position, final String url) {
        super(id, position, url);
    }
}
