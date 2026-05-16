/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import static java.util.stream.Collectors.toSet;

import org.springframework.util.CollectionUtils;

import pl.dawid0604.realestate.domain.AdvertisementStatus;

import java.util.Arrays;
import java.util.Set;

public record UserAdvertisementsQuery(
        String email, int page, int pageSize, Set<AdvertisementStatus> statuses) implements Query {

    private static final Set<AdvertisementStatus> DEFAULT_STATUSES =
            Arrays.stream(AdvertisementStatus.values()).collect(toSet());

    public UserAdvertisementsQuery {
        statuses = CollectionUtils.isEmpty(statuses) ? DEFAULT_STATUSES : Set.copyOf(statuses);
    }

    @Override
    public Set<AdvertisementStatus> statuses() {
        return Set.copyOf(statuses);
    }
}
