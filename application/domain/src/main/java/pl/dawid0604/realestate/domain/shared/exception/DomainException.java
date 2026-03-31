/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public abstract sealed class DomainException extends RuntimeException
        permits AdvertisementNotFoundException, InvalidArgumentValueException, LocalityNotFoundException, MaxPhotosExceededException, UnauthorizedAccessException, UserNotFoundException {

    protected DomainException(final String message) {
        super(message);
    }
}
