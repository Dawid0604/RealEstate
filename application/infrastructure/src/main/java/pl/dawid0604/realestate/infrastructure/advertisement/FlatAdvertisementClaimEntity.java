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
@Table(name = "flat_advertisements_claims")
non-sealed class FlatAdvertisementClaimEntity
        extends AdvertisementClaimEntity<FlatAdvertisementEntity> {

    FlatAdvertisementClaimEntity(final UUID id, final String claimKey, final String claimValue) {
        super(id, claimKey, claimValue);
    }
}
