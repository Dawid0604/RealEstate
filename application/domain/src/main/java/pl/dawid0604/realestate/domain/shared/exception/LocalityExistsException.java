/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class LocalityExistsException extends DomainException {
    public LocalityExistsException() {
        super("Given locality already exists");
    }
}
