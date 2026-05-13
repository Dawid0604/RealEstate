/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class ExpiredTokenException extends DomainException {
    public ExpiredTokenException(final Exception exception) {
        super("Given token expired", exception);
    }
}
