/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.fixture;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.experimental.UtilityClass;
import pl.dawid0604.realestate.domain.*;

@UtilityClass
public class AdvertisementFixture {

    public static Advertisement.Builder getDummyAdvertisementBuilder(
            final AdvertisementDetails<?> details) {

        final Title title = new Title("any title content");

        return Advertisement.reconstitute()
                .id(Identifier.generate())
                .createdAt(Instant.now())
                .area(new Area(BigDecimal.valueOf(45.5)))
                .pricePerSquareMeter(
                        PricePerSquareMeter.reconstitute(
                                BigDecimal.valueOf(2500), MoneyCurrency.PLN))
                .title(title)
                .description(new Description("any description content"))
                .price(new Price(BigDecimal.valueOf(2_500_00), MoneyCurrency.PLN))
                .locality(new AdvertisementLocality(Identifier.generate()))
                .details(details)
                .status(AdvertisementStatus.ACTIVE)
                .userId(Identifier.generate())
                .slug(Slug.create(title));
    }

    public static FlatDetails getDummyFlatDetails() {
        return new FlatDetails(
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
                HouseBuildingType.DETACHED,
                null,
                new NumberOfRooms(1),
                new Floor(3),
                new BuiltYear(2011),
                TypeOfMarket.PRIMARY);
    }

    public static PlotDetails getDummyPlotDetails() {
        return new PlotDetails(PlotBuildingType.AGRICULTURAL, null);
    }
}
