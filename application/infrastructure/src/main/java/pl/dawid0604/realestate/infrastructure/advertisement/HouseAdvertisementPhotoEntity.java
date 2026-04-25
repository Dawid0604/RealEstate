/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PACKAGE;

@Getter
@Entity
@NoArgsConstructor(access = PACKAGE)
@EqualsAndHashCode(callSuper = true)
@Table(name = "house_advertisements_photos")
non-sealed class HouseAdvertisementPhotoEntity
        extends AdvertisementPhotoEntity<HouseAdvertisementEntity> {

    HouseAdvertisementPhotoEntity(final UUID id, final int position, final String url) {
        super(id, position, url);
    }
}
