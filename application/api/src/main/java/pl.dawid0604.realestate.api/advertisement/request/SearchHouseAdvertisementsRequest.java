package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidArea;
import pl.dawid0604.realestate.api.validation.ValidBuiltYear;
import pl.dawid0604.realestate.api.validation.ValidFloor;
import pl.dawid0604.realestate.api.validation.ValidLocalityId;
import pl.dawid0604.realestate.api.validation.ValidNumberOfRooms;
import pl.dawid0604.realestate.api.validation.ValidPrice;
import pl.dawid0604.realestate.domain.HouseBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Search house advertisements request")
public record SearchHouseAdvertisementsRequest(
        @ValidArea @Schema(description = "Area from value", example = "34.5") BigDecimal areaFrom,
        @ValidArea @Schema(description = "Area to value", example = "44.5") BigDecimal areaTo,
        @ValidPrice @Schema(description = "Price from value", example = "250000")
                BigDecimal priceFrom,
        @ValidPrice @Schema(description = "Price to value", example = "350000") BigDecimal priceTo,
        @ValidPrice @Schema(description = "PricePerSquareMeter from value", example = "3500")
                BigDecimal pricePerSquareMeterFrom,
        @ValidPrice @Schema(description = "PricePerSquareMeter to value", example = "4500")
                BigDecimal pricePerSquareMeterTo,
        @Schema(description = "Building types") Set<HouseBuildingType> types,
        @ValidLocalityId
                @Schema(
                        description = "Locality id",
                        example = "019e2325-d92b-70ad-94e3-609123e34a79")
                UUID localityId,
        @Schema(description = "Date from value", example = "2026-01-01") LocalDate dateFrom,
        @Schema(description = "Date to value", example = "2026-02-02") LocalDate dateTo,
        @Schema(description = "Type of markets") Set<TypeOfMarket> typeOfMarkets,
        @ValidFloor @Schema(description = "Floors from value", example = "1") Integer floorsFrom,
        @ValidFloor @Schema(description = "Floors from value", example = "2") Integer floorsTo,
        @ValidNumberOfRooms @Schema(description = "Number of rooms from value", example = "3")
                Integer numberOfRoomsFrom,
        @ValidNumberOfRooms @Schema(description = "Number of rooms to value", example = "4")
                Integer numberOfRoomsTo,
        @ValidBuiltYear @Schema(description = "Built year from value", example = "2011")
                Integer builtYearFrom,
        @Schema(description = "Built year to value", example = "2012") Integer builtYearTo) {}
