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
@Table(name = "flat_advertisements_claims")
non-sealed class FlatAdvertisementClaimEntity
        extends AdvertisementClaimEntity<FlatAdvertisementEntity> {

    FlatAdvertisementClaimEntity(final UUID id, final String claimKey, final String claimValue) {
        super(id, claimKey, claimValue);
    }
}
