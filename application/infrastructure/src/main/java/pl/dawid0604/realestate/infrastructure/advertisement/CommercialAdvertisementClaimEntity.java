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
@Table(name = "commercial_advertisements_claims")
non-sealed class CommercialAdvertisementClaimEntity
        extends AdvertisementClaimEntity<CommercialAdvertisementEntity> {

    CommercialAdvertisementClaimEntity(
            final UUID id, final String claimKey, final String claimValue) {

        super(id, claimKey, claimValue);
    }
}
