/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.util.UUID;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("PMD.ImmutableField")
abstract sealed class AdvertisementPhotoEntity<T extends AdvertisementEntity<?, ?>>
        extends BaseEntity
        permits CommercialAdvertisementPhotoEntity,
                FlatAdvertisementPhotoEntity,
                HouseAdvertisementPhotoEntity,
                PlotAdvertisementPhotoEntity {

    private String url;
    private int position;

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "advertisement_id")
    private T advertisement;

    AdvertisementPhotoEntity(final UUID id, final int position, final String url) {
        super(id);
        this.position = position;
        this.url = url;
    }
}
