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
@Table(name = "house_advertisements_claims")
non-sealed class HouseAdvertisementClaimEntity
        extends AdvertisementClaimEntity<HouseAdvertisementEntity> {

    HouseAdvertisementClaimEntity(final UUID id, final String claimKey, final String claimValue) {
        super(id, claimKey, claimValue);
    }
}
