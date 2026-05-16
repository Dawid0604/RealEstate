/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class UserAlreadyActiveException extends DomainException {
    public UserAlreadyActiveException() {
        super("User is already active");
    }
}
