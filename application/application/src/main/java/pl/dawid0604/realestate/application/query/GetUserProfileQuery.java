/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import java.util.Objects;
import java.util.UUID;

public record GetUserProfileQuery(UUID userId) implements Query {

    public GetUserProfileQuery {
        Objects.requireNonNull(userId, "UserId cannot be null");
    }
}
