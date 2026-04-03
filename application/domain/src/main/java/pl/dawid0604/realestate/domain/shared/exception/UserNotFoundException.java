/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class UserNotFoundException extends DomainException {
    public UserNotFoundException(final String email) {
        super("User not found, email=" + email);
    }
}
