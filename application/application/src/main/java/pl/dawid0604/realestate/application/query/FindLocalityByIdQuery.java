/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import java.util.UUID;

public record FindLocalityByIdQuery(UUID localityId) implements Query {}
