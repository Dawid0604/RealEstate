/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.exception;

public final class AdvertisementNotFoundException extends DomainException {
    public AdvertisementNotFoundException(final String slug) {
        super("Advertisement not found with given slug: " + slug);
    }

    public AdvertisementNotFoundException(final String slug, final Exception exception) {
        super("Advertisement not found with given slug: " + slug, exception);
    }
}
