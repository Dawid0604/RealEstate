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
@SuppressWarnings("PMD.ImmutableField")
@NoArgsConstructor(access = PROTECTED)
abstract sealed class AdvertisementClaimEntity<T extends AdvertisementEntity<?, ?>>
        extends BaseEntity
        permits CommercialAdvertisementClaimEntity,
                FlatAdvertisementClaimEntity,
                HouseAdvertisementClaimEntity,
                PlotAdvertisementClaimEntity {

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "advertisement_id")
    private T advertisement;

    private String claimKey;
    private String claimValue;

    AdvertisementClaimEntity(final UUID id, final String claimKey, final String claimValue) {
        super(id);
        this.claimKey = claimKey;
        this.claimValue = claimValue;
    }
}
