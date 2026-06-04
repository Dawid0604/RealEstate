/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement.request;

import static java.util.Collections.emptySet;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.domain.AdvertisementStatus;

import java.util.Set;

@Schema(description = "Search user advertisements request")
public record SearchUserAdvertisementsRequest(
        @Schema(description = "Advertisements statuses") Set<AdvertisementStatus> statuses) {

    public SearchUserAdvertisementsRequest {
        statuses = statuses != null ? Set.copyOf(statuses) : emptySet();
    }

    @Override
    public Set<AdvertisementStatus> statuses() {
        return Set.copyOf(statuses);
    }
}
