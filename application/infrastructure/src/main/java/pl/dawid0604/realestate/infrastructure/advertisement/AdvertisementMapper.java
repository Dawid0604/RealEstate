/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PACKAGE;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementClaim;
import pl.dawid0604.realestate.domain.AdvertisementDetails;
import pl.dawid0604.realestate.domain.AdvertisementLocality;
import pl.dawid0604.realestate.domain.AdvertisementPhoto;
import pl.dawid0604.realestate.domain.Area;
import pl.dawid0604.realestate.domain.BuiltYear;
import pl.dawid0604.realestate.domain.CommercialDetails;
import pl.dawid0604.realestate.domain.Description;
import pl.dawid0604.realestate.domain.FlatDetails;
import pl.dawid0604.realestate.domain.Floor;
import pl.dawid0604.realestate.domain.HouseDetails;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.MoneyCurrency;
import pl.dawid0604.realestate.domain.NumberOfRooms;
import pl.dawid0604.realestate.domain.PlotDetails;
import pl.dawid0604.realestate.domain.Price;
import pl.dawid0604.realestate.domain.PricePerSquareMeter;
import pl.dawid0604.realestate.domain.Slug;
import pl.dawid0604.realestate.domain.Title;
import pl.dawid0604.realestate.domain.Url;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;

@Component
@NoArgsConstructor(access = PACKAGE)
class AdvertisementMapper {

    Advertisement toDomain(final AdvertisementEntity<?, ?> entity) {
        if (entity == null) {
            return null;
        }

        return Advertisement.reconstitute()
                .id(Identifier.of(entity.getId()))
                .slug(Slug.of(entity.getSlug()))
                .title(new Title(entity.getTitle()))
                .description(new Description(entity.getDescription()))
                .price(new Price(entity.getPrice(), MoneyCurrency.PLN))
                .area(new Area(entity.getArea()))
                .pricePerSquareMeter(
                        PricePerSquareMeter.reconstitute(
                                entity.getPricePerSquareMeter(), MoneyCurrency.PLN))
                .locality(new AdvertisementLocality(Identifier.of(entity.getLocalityId())))
                .details(mapDetails(entity))
                .status(entity.getStatus())
                .userId(Identifier.of(entity.getUserId()))
                .photos(mapToDomainPhotos(entity))
                .createdAt(entity.getCreatedAt())
                .featured(entity.isFeatured())
                .build();
    }

    @SuppressWarnings("CPD-START")
    FlatAdvertisementEntity toFlatEntity(final Advertisement advertisement) {
        if (advertisement == null) {
            return null;
        }

        if (advertisement.getAdvertisementType() != AdvertisementType.FLAT) {
            throw new IllegalArgumentException("Invalid advertisement type, expected flat");
        }

        final FlatDetails details = (FlatDetails) advertisement.getDetails();
        final FlatAdvertisementEntity entity =
                new FlatAdvertisementEntity(
                        advertisement.getId().getValue(),
                        advertisement.getSlug().getValue(),
                        advertisement.getTitle().value(),
                        advertisement.getDescription().value(),
                        advertisement.getPrice().value(),
                        advertisement.getArea().value(),
                        advertisement.getPricePerSquareMeter().getValue(),
                        advertisement.getLocality().id().getValue(),
                        advertisement.getOwner().getValue(),
                        advertisement.isFeatured(),
                        advertisement.getStatus(),
                        mapToEntityClaims(details, FlatAdvertisementClaimEntity.class),
                        mapToEntityPhotos(advertisement, FlatAdvertisementPhotoEntity.class),
                        details.getBuildingType(),
                        details.getNumberOfRooms().value(),
                        details.getFloor().value(),
                        details.getFloors().value(),
                        details.getBuiltYear().value(),
                        details.getTypeOfMarket());

        assignToClaims(entity.getClaims(), entity);
        assignToPhotos(entity.getPhotos(), entity);
        return entity;
    }

    CommercialAdvertisementEntity toCommercialEntity(final Advertisement advertisement) {
        if (advertisement == null) {
            return null;
        }

        if (advertisement.getAdvertisementType() != AdvertisementType.COMMERCIAL) {
            throw new IllegalArgumentException("Invalid advertisement type, expected commercial");
        }

        final CommercialDetails details = (CommercialDetails) advertisement.getDetails();
        final CommercialAdvertisementEntity entity =
                new CommercialAdvertisementEntity(
                        advertisement.getId().getValue(),
                        advertisement.getSlug().getValue(),
                        advertisement.getTitle().value(),
                        advertisement.getDescription().value(),
                        advertisement.getPrice().value(),
                        advertisement.getArea().value(),
                        advertisement.getPricePerSquareMeter().getValue(),
                        advertisement.getLocality().id().getValue(),
                        advertisement.getOwner().getValue(),
                        advertisement.isFeatured(),
                        advertisement.getStatus(),
                        mapToEntityClaims(details, CommercialAdvertisementClaimEntity.class),
                        mapToEntityPhotos(advertisement, CommercialAdvertisementPhotoEntity.class),
                        details.getBuildingType(),
                        details.getNumberOfRooms().value(),
                        details.getFloor().value(),
                        details.getFloors().value(),
                        details.getBuiltYear().value(),
                        details.getTypeOfMarket());

        assignToClaims(entity.getClaims(), entity);
        assignToPhotos(entity.getPhotos(), entity);
        return entity;
    }

    HouseAdvertisementEntity toHouseEntity(final Advertisement advertisement) {
        if (advertisement == null) {
            return null;
        }

        if (advertisement.getAdvertisementType() != AdvertisementType.HOUSE) {
            throw new IllegalArgumentException("Invalid advertisement type, expected house");
        }

        final HouseDetails details = (HouseDetails) advertisement.getDetails();
        final HouseAdvertisementEntity entity =
                new HouseAdvertisementEntity(
                        advertisement.getId().getValue(),
                        advertisement.getSlug().getValue(),
                        advertisement.getTitle().value(),
                        advertisement.getDescription().value(),
                        advertisement.getPrice().value(),
                        advertisement.getArea().value(),
                        advertisement.getPricePerSquareMeter().getValue(),
                        advertisement.getLocality().id().getValue(),
                        advertisement.getOwner().getValue(),
                        advertisement.isFeatured(),
                        advertisement.getStatus(),
                        mapToEntityClaims(details, HouseAdvertisementClaimEntity.class),
                        mapToEntityPhotos(advertisement, HouseAdvertisementPhotoEntity.class),
                        details.getBuildingType(),
                        details.getNumberOfRooms().value(),
                        details.getFloors().value(),
                        details.getBuiltYear().value(),
                        details.getTypeOfMarket());

        assignToClaims(entity.getClaims(), entity);
        assignToPhotos(entity.getPhotos(), entity);
        return entity;
    }

    PlotAdvertisementEntity toPlotEntity(final Advertisement advertisement) {
        if (advertisement == null) {
            return null;
        }

        if (advertisement.getAdvertisementType() != AdvertisementType.PLOT) {
            throw new IllegalArgumentException("Invalid advertisement type, expected plot");
        }

        final PlotDetails details = (PlotDetails) advertisement.getDetails();
        final PlotAdvertisementEntity entity =
                new PlotAdvertisementEntity(
                        advertisement.getId().getValue(),
                        advertisement.getSlug().getValue(),
                        advertisement.getTitle().value(),
                        advertisement.getDescription().value(),
                        advertisement.getPrice().value(),
                        advertisement.getArea().value(),
                        advertisement.getPricePerSquareMeter().getValue(),
                        advertisement.getLocality().id().getValue(),
                        advertisement.getOwner().getValue(),
                        advertisement.isFeatured(),
                        advertisement.getStatus(),
                        mapToEntityClaims(details, PlotAdvertisementClaimEntity.class),
                        mapToEntityPhotos(advertisement, PlotAdvertisementPhotoEntity.class),
                        details.getBuildingType());

        assignToClaims(entity.getClaims(), entity);
        assignToPhotos(entity.getPhotos(), entity);
        return entity;
    }

    private static <T extends AdvertisementEntity<?, ?>> void assignToPhotos(
            final Set<? extends AdvertisementPhotoEntity<T>> photos, final T entity) {

        photos.forEach(p -> p.setAdvertisement(entity));
    }

    private static <T extends AdvertisementEntity<?, ?>> void assignToClaims(
            final Set<? extends AdvertisementClaimEntity<T>> claims, final T entity) {

        claims.forEach(c -> c.setAdvertisement(entity));
    }

    @SuppressWarnings("CPD-END")
    private static <T extends AdvertisementPhotoEntity<?>> Set<T> mapToEntityPhotos(
            final Advertisement advertisement, final Class<T> type) {

        return Stream.of(advertisement.getPhotos())
                .flatMap(Set::stream)
                .map(
                        p ->
                                switch (advertisement.getAdvertisementType()) {
                                    case FLAT ->
                                            new FlatAdvertisementPhotoEntity(
                                                    p.getId().getValue(),
                                                    p.getPosition(),
                                                    p.getUrl().value());

                                    case HOUSE ->
                                            new HouseAdvertisementPhotoEntity(
                                                    p.getId().getValue(),
                                                    p.getPosition(),
                                                    p.getUrl().value());

                                    case COMMERCIAL ->
                                            new CommercialAdvertisementPhotoEntity(
                                                    p.getId().getValue(),
                                                    p.getPosition(),
                                                    p.getUrl().value());

                                    case PLOT ->
                                            new PlotAdvertisementPhotoEntity(
                                                    p.getId().getValue(),
                                                    p.getPosition(),
                                                    p.getUrl().value());
                                })
                .map(type::cast)
                .collect(toSet());
    }

    private static <T extends AdvertisementClaimEntity<?>> Set<T> mapToEntityClaims(
            final AdvertisementDetails<?> details, final Class<T> type) {

        return Stream.ofNullable(details.getClaims())
                .flatMap(Set::stream)
                .map(
                        c ->
                                switch (details) {
                                    case FlatDetails ignored ->
                                            new FlatAdvertisementClaimEntity(
                                                    c.id().getValue(), c.key(), c.value());

                                    case CommercialDetails ignored ->
                                            new CommercialAdvertisementClaimEntity(
                                                    c.id().getValue(), c.key(), c.value());

                                    case HouseDetails ignored ->
                                            new HouseAdvertisementClaimEntity(
                                                    c.id().getValue(), c.key(), c.value());

                                    case PlotDetails ignored ->
                                            new PlotAdvertisementClaimEntity(
                                                    c.id().getValue(), c.key(), c.value());
                                })
                .map(type::cast)
                .collect(toSet());
    }

    private static Set<AdvertisementPhoto> mapToDomainPhotos(
            final AdvertisementEntity<?, ?> entity) {

        return Stream.ofNullable(entity.getPhotos())
                .flatMap(Set::stream)
                .map(
                        p ->
                                AdvertisementPhoto.of(
                                        Identifier.of(p.getId()),
                                        new Url(p.getUrl()),
                                        p.getPosition()))
                .collect(toSet());
    }

    private static Set<AdvertisementClaim> mapToDomainClaims(
            final Set<? extends AdvertisementClaimEntity<?>> claims) {

        return Stream.ofNullable(claims)
                .flatMap(Set::stream)
                .map(c -> new AdvertisementClaim(getClaimId(c), c.getClaimKey(), c.getClaimValue()))
                .collect(toSet());
    }

    private static Identifier getClaimId(final AdvertisementClaimEntity<?> claim) {
        return claim.getId() != null ? Identifier.of(claim.getId()) : Identifier.generate();
    }

    private static AdvertisementDetails<?> mapDetails(final AdvertisementEntity<?, ?> entity) {
        return switch (entity) {
            case CommercialAdvertisementEntity commercialEntity ->
                    new CommercialDetails(
                            commercialEntity.getBuildingType(),
                            mapToDomainClaims(commercialEntity.getClaims()),
                            new NumberOfRooms(commercialEntity.getNumberOfRooms()),
                            new Floor(commercialEntity.getFloor()),
                            new Floor(commercialEntity.getFloors()),
                            new BuiltYear(commercialEntity.getBuiltYear()),
                            commercialEntity.getTypeOfMarket());

            case FlatAdvertisementEntity flatEntity ->
                    new FlatDetails(
                            flatEntity.getBuildingType(),
                            mapToDomainClaims(flatEntity.getClaims()),
                            new NumberOfRooms(flatEntity.getNumberOfRooms()),
                            new Floor(flatEntity.getFloor()),
                            new Floor(flatEntity.getFloors()),
                            new BuiltYear(flatEntity.getBuiltYear()),
                            flatEntity.getTypeOfMarket());

            case HouseAdvertisementEntity houseEntity ->
                    new HouseDetails(
                            houseEntity.getBuildingType(),
                            mapToDomainClaims(houseEntity.getClaims()),
                            new NumberOfRooms(houseEntity.getNumberOfRooms()),
                            new Floor(houseEntity.getFloors()),
                            new BuiltYear(houseEntity.getBuiltYear()),
                            houseEntity.getTypeOfMarket());

            case PlotAdvertisementEntity plotEntity ->
                    new PlotDetails(
                            plotEntity.getPlotType(), mapToDomainClaims(plotEntity.getClaims()));
        };
    }
}
