/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public sealed interface SearchAdvertisementsCriteria
        permits SearchCommercialAdvertisementsCriteria,
                SearchFlatAdvertisementsCriteria,
                SearchHouseAdvertisementsCriteria,
                SearchPlotAdvertisementsCriteria {

    BigDecimal areaFrom();

    BigDecimal areaTo();

    BigDecimal priceFrom();

    BigDecimal priceTo();

    BigDecimal pricePerSquareMeterFrom();

    BigDecimal pricePerSquareMeterTo();

    int page();

    int pageSize();

    Set<String> offerFrom();

    Set<String> types();

    UUID localityId();

    LocalDate dateFrom();

    LocalDate dateTo();
}
