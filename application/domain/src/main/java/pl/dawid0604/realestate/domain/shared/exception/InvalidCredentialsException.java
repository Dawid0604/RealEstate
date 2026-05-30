/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Given credentials are invalid");
    }
}
