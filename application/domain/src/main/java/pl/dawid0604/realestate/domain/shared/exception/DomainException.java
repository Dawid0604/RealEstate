/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public abstract sealed class DomainException extends RuntimeException
        permits AdvertisementNotFoundException, UnauthorizedAccessException, InvalidArgumentValueException, MaxPhotosExceededException, UserNotFoundException {

    protected DomainException(final String message) {
        super(message);
    }
}
