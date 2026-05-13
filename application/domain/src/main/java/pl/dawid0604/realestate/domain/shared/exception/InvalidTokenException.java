/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class InvalidTokenException extends DomainException {
    public InvalidTokenException(final Exception exception) {
        super("Given token is invalid", exception);
    }
}
