/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

import java.util.UUID;

public final class LocalityNotFoundException extends DomainException {
    public LocalityNotFoundException(final UUID localityId) {
        super("Locality not found with given id: " + localityId.toString());
    }
}
