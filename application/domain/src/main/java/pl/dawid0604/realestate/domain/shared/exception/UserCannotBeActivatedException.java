/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class UserCannotBeActivatedException extends DomainException {
    public UserCannotBeActivatedException() {
        super("User is already active");
    }
}
