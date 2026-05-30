/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class RefreshTokenNotFoundException extends DomainException {

    public RefreshTokenNotFoundException() {
        super("Given refresh token is unavailable");
    }
}
