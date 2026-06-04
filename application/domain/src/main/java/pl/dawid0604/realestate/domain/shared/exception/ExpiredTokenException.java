/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class ExpiredTokenException extends DomainException {
    private static final String MESSAGE = "Given token expired";

    public ExpiredTokenException(final Exception exception) {
        super(MESSAGE, exception);
    }

    public ExpiredTokenException() {
        super(MESSAGE);
    }
}
