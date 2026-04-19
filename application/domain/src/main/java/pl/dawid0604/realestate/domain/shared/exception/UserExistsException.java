/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class UserExistsException extends DomainException {
    public UserExistsException(final String email) {
        super("User with given email exists, email=" + email);
    }
}
