package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidPageNumber;
import pl.dawid0604.realestate.api.validation.ValidPageSize;
import pl.dawid0604.realestate.domain.AdvertisementStatus;

import java.util.Set;

@Schema(description = "Search user advertisements request")
public record SearchUserAdvertisementsRequest(
        @ValidPageNumber @Schema(description = "Number of page", example = "1") int page,
        @ValidPageSize @Schema(description = "Page size", example = "25") int pageSize,
        @Schema(description = "Advertisements statuses") Set<AdvertisementStatus> statuses) {}
