/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public abstract sealed class DomainException extends RuntimeException
        permits AdvertisementNotFoundException,
                DifferentPasswordException,
                ExpiredTokenException,
                InternalException,
                InvalidArgumentValueException,
                InvalidCredentialsException,
                InvalidTokenException,
                LocalityNotFoundException,
                MaxPhotosExceededException,
                RefreshTokenNotFoundException,
                UnauthorizedAccessException,
                UserAlreadyActiveException,
                UserBannedException,
                UserCannotBeActivatedException,
                UserCannotBeUnbannedException,
                UserExistsException,
                UserNotFoundException {

    protected DomainException(final String message) {
        super(message);
    }

    protected DomainException(final String message, final Exception exception) {
        super(message, exception);
    }
}
