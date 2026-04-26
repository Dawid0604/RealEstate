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
@Table(name = "commercial_advertisements_claims")
non-sealed class CommercialAdvertisementClaimEntity
        extends AdvertisementClaimEntity<CommercialAdvertisementEntity> {

    CommercialAdvertisementClaimEntity(
            final UUID id, final String claimKey, final String claimValue) {

        super(id, claimKey, claimValue);
    }
}
