/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.TypeOfMarket;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;

@Entity
@Getter
@Immutable
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "user_advertisements_view")
class UserAdvertisementViewEntity {

    @Id @EqualsAndHashCode.Include private UUID id;

    private String slug;
    private String title;
    private BigDecimal price;
    private BigDecimal area;
    private BigDecimal pricePerSquareMeter;
    private Instant createdAt;
    private UUID localityId;
    private boolean featured;
    private Integer numberOfRooms;
    private Integer floor;
    private Integer floors;
    private Integer builtYear;
    private UUID userId;
    private String plotType;
    private String buildingType;

    @Enumerated(EnumType.STRING)
    private AdvertisementStatus status;

    @Enumerated(EnumType.STRING)
    private TypeOfMarket typeOfMarket;

    @Enumerated(EnumType.STRING)
    private AdvertisementType type;
}
