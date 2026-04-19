/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import static java.util.stream.Collectors.toSet;

import org.springframework.util.CollectionUtils;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidPageNumber;
import pl.dawid0604.realestate.application.validation.ValidPageSize;
import pl.dawid0604.realestate.domain.AdvertisementStatus;

import java.util.Arrays;
import java.util.Set;

public record UserAdvertisementsQuery(
        @ValidEmail String email,
        @ValidPageNumber int page,
        @ValidPageSize int pageSize,
        Set<String> statuses)
        implements Query {

    private static final Set<String> DEFAULT_STATUSES =
            Arrays.stream(AdvertisementStatus.values())
                    .map(AdvertisementStatus::name)
                    .collect(toSet());

    public UserAdvertisementsQuery {
        statuses = CollectionUtils.isEmpty(statuses) ? DEFAULT_STATUSES : Set.copyOf(statuses);
    }

    @Override
    public Set<String> statuses() {
        return Set.copyOf(statuses);
    }
}
