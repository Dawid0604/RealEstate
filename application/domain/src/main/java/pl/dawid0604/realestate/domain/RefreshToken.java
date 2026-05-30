/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.Instant;
import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class RefreshToken extends AggregateRoot {
    private final Identifier id;
    private final Identifier userId;
    private final String token;
    private final Instant createdAt;
    private final Instant expiresAt;

    private RefreshToken(
            final boolean isCreateMode,
            final Identifier id,
            final Identifier userId,
            final String token,
            final Instant createdAt,
            final Instant expiresAt) {

        requireNonNull(id, "Id");
        requireNonNull(userId, "UserId");
        requireNonNull(expiresAt, "ExpiresAt");
        requireNonNull(createdAt, "CreatedAt");

        if (isBlank(token)) {
            throw new InvalidArgumentValueException("Token cannot be blank");
        }

        if (isCreateMode && expiresAt.isBefore(Instant.now())) {
            throw new InvalidArgumentValueException("ExpiresAt cannot be from the past");
        }

        if (createdAt.isAfter(Instant.now())) {
            throw new InvalidArgumentValueException("CreatedAt cannot be from the future");
        }

        this.id = id;
        this.userId = userId;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public static RefreshToken create(
            final Identifier userId, final String token, final Instant expiresAt) {

        return new RefreshToken(
                true, Identifier.generate(), userId, hashToken(token), Instant.now(), expiresAt);
    }

    public static RefreshToken reconstitute(
            final Identifier id,
            final Identifier userId,
            final String token,
            final Instant createdAt,
            final Instant expiresAt) {

        return new RefreshToken(false, id, userId, token, createdAt, expiresAt);
    }

    public boolean tokenMatches(final String incomingToken) {
        return Objects.equals(this.token, hashToken(incomingToken));
    }

    private static String hashToken(final String token) {
        return isNotBlank(token) ? DigestUtils.sha256Hex(token) : token;
    }

    public Identifier getId() {
        return id;
    }

    public Identifier getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof final RefreshToken other && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
