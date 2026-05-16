/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class UserCannotBeUnbannedException extends DomainException {
    public UserCannotBeUnbannedException() {
        super("User cannot be unbanned");
    }
}
