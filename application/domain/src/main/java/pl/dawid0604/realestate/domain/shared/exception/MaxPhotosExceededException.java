/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class MaxPhotosExceededException extends DomainException {
    public MaxPhotosExceededException(final int limit) {
        super("Number of photos cannot exceed, limit is " + limit + " photos");
    }
}
