/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class ForbiddenException extends DomainException {
    public ForbiddenException(final String message) {
        super(message);
    }
}
