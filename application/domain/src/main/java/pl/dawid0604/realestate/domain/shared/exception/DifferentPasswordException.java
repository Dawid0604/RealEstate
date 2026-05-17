/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class DifferentPasswordException extends DomainException {
    public DifferentPasswordException() {
        super("Given passwords does not match");
    }
}
