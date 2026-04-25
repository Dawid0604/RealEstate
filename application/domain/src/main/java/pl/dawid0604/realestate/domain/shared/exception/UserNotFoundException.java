/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

import java.util.UUID;

public final class UserNotFoundException extends DomainException {
    public UserNotFoundException(final String email) {
        super("User not found with given email: " + email);
    }

    public UserNotFoundException(final UUID uuid) {
        super("User not found with given id: " + uuid);
    }
}
