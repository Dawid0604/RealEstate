/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class InvalidArgumentValueException extends DomainException {
    public InvalidArgumentValueException(final String message) {
        super(message);
    }
}
