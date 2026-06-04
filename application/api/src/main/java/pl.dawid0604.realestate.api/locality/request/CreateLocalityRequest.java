/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.locality.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidLocalityName;

@Schema(description = "Create locality request")
public record CreateLocalityRequest(
        @ValidLocalityName @Schema(description = "Locality name", example = "Warsaw")
                String name) {}
