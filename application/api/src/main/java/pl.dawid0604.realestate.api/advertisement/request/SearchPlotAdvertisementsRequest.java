package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidArea;
import pl.dawid0604.realestate.api.validation.ValidLocalityId;
import pl.dawid0604.realestate.api.validation.ValidPageNumber;
import pl.dawid0604.realestate.api.validation.ValidPageSize;
import pl.dawid0604.realestate.api.validation.ValidPrice;
import pl.dawid0604.realestate.domain.PlotBuildingType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Search plot advertisements request")
public record SearchPlotAdvertisementsRequest(
        @ValidArea @Schema(description = "Area from value", example = "34.5") BigDecimal areaFrom,
        @ValidArea @Schema(description = "Area to value", example = "44.5") BigDecimal areaTo,
        @ValidPrice @Schema(description = "Price from value", example = "250000")
                BigDecimal priceFrom,
        @ValidPrice @Schema(description = "Price to value", example = "350000") BigDecimal priceTo,
        @ValidPrice @Schema(description = "PricePerSquareMeter from value", example = "3500")
                BigDecimal pricePerSquareMeterFrom,
        @ValidPrice @Schema(description = "PricePerSquareMeter to value", example = "4500")
                BigDecimal pricePerSquareMeterTo,
        @ValidPageNumber @Schema(description = "Number of page, starting from 0", example = "0")
                int page,
        @ValidPageSize @Schema(description = "Page size", example = "25") int pageSize,
        @Schema(description = "Building types") Set<PlotBuildingType> types,
        @ValidLocalityId
                @Schema(
                        description = "Locality id",
                        example = "019e2325-d92b-70ad-94e3-609123e34a79")
                UUID localityId,
        @Schema(description = "Date from value", example = "2026-01-01") LocalDate dateFrom,
        @Schema(description = "Date to value", example = "2026-02-02") LocalDate dateTo) {}
