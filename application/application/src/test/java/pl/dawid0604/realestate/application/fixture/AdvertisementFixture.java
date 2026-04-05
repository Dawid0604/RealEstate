package pl.dawid0604.realestate.application.fixture;

import lombok.experimental.UtilityClass;

import pl.dawid0604.realestate.domain.*;

import java.math.BigDecimal;
import java.time.Instant;

@UtilityClass
public class AdvertisementFixture {

    public static Advertisement.Builder getDummyAdvertisementBuilder(
            final AdvertisementDetails<?> details) {

        final Title title = new Title("any title content");

        return Advertisement.reconstitute()
                .id(Identifier.generate())
                .createdAt(Instant.now())
                .title(title)
                .description(new Description("any description content"))
                .price(new Money(BigDecimal.valueOf(2_500_00), MoneyCurrency.PLN))
                .locality(new Locality(Identifier.generate()))
                .details(details)
                .status(AdvertisementStatus.ACTIVE)
                .userId(Identifier.generate())
                .slug(Slug.create(title));
    }

    public static FlatDetails getDummyFlatDetails() {
        return new FlatDetails(
                new Area(BigDecimal.valueOf(50.25)),
                FlatBuildingType.APARTMENT,
                null,
                new NumberOfRooms(1),
                new Floor(3),
                new Floor(4),
                new BuiltYear(2011),
                TypeOfMarket.PRIMARY);
    }

    public static CommercialDetails getDummyCommercialDetails() {
        return new CommercialDetails(
                new Area(BigDecimal.valueOf(50.25)),
                CommercialBuildingType.HALL,
                null,
                new NumberOfRooms(1),
                new Floor(3),
                new Floor(4),
                new BuiltYear(2011),
                TypeOfMarket.PRIMARY);
    }

    public static HouseDetails getDummyHouseDetails() {
        return new HouseDetails(
                new Area(BigDecimal.valueOf(50.25)),
                HouseBuildingType.DETACHED,
                null,
                new NumberOfRooms(1),
                new Floor(3),
                new BuiltYear(2011),
                TypeOfMarket.PRIMARY);
    }

    public static PlotDetails getDummyPlotDetails() {
        return new PlotDetails(
                new Area(BigDecimal.valueOf(50.25)), PlotBuildingType.AGRICULTURAL, null);
    }

    public static Slug getDummySlug() {
        return Slug.of("abcdefghijklmn");
    }
}
