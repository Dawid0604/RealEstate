/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public abstract sealed class DomainException extends RuntimeException
        permits ForbiddenException, InvalidArgumentValueException, MaxPhotosExceededException {

    protected DomainException(final String message) {
        super(message);
    }
}
