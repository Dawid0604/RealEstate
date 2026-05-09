/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dawid0604.realestate.domain.AdvertisementStatus;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("PMD.ImmutableField")
abstract sealed class AdvertisementEntity<
                T extends AdvertisementClaimEntity<?>, Y extends AdvertisementPhotoEntity<?>>
        extends BaseEntity
        permits CommercialAdvertisementEntity,
                FlatAdvertisementEntity,
                HouseAdvertisementEntity,
                PlotAdvertisementEntity {

    private String slug;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal area;
    private BigDecimal pricePerSquareMeter;
    private UUID localityId;
    private UUID userId;
    private boolean isFeatured;

    @Enumerated(STRING)
    private AdvertisementStatus status;

    @OneToMany(mappedBy = "advertisement", orphanRemoval = true, cascade = ALL)
    private Set<T> claims;

    @OneToMany(mappedBy = "advertisement", orphanRemoval = true, cascade = ALL)
    private Set<Y> photos;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    AdvertisementEntity(
            final UUID id,
            final String slug,
            final String title,
            final String description,
            final BigDecimal price,
            final BigDecimal area,
            final BigDecimal pricePerSquareMeter,
            final UUID localityId,
            final UUID userId,
            final boolean isFeatured,
            final AdvertisementStatus status,
            final Set<T> claims,
            final Set<Y> photos) {

        super(id);
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.price = price;
        this.area = area;
        this.pricePerSquareMeter = pricePerSquareMeter;
        this.localityId = localityId;
        this.userId = userId;
        this.isFeatured = isFeatured;
        this.status = status;
        this.claims = claims;
        this.photos = photos;
    }
}
