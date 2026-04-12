/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidPageNumber;
import pl.dawid0604.realestate.application.validation.ValidPageSize;

import java.util.Set;

public record UserAdvertisementsQuery(
        @ValidEmail String email,
        @ValidPageNumber int page,
        @ValidPageSize int pageSize,
        Set<String> statuses)
        implements Query {}
