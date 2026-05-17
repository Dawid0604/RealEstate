/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public abstract sealed class DomainException extends RuntimeException
        permits AdvertisementNotFoundException, DifferentPasswordException, ExpiredTokenException, ForbiddenException, InternalException, InvalidArgumentValueException, InvalidCredentialsException, InvalidTokenException, LocalityExistsException, LocalityNotFoundException, MaxPhotosExceededException, RefreshTokenNotFoundException, UserAlreadyActiveException, UserBannedException, UserCannotBeActivatedException, UserCannotBeUnbannedException, UserExistsException, UserNotFoundException {

    protected DomainException(final String message) {
        super(message);
    }

    protected DomainException(final String message, final Exception exception) {
        super(message, exception);
    }
}
