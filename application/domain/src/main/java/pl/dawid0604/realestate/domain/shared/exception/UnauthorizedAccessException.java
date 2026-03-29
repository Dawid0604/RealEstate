/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class UnauthorizedAccessException extends DomainException {
    public UnauthorizedAccessException(final String message) {
        super(message);
    }
}
