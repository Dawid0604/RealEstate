/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class InternalException extends DomainException {

    public InternalException(final Exception exception) {
        super("Internal error occurred", exception);
    }
}
