/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class AdvertisementPhoto {
    private final Identifier id;
    private final Url url;

    private AdvertisementPhoto(final Identifier id, final Url url) {
        if (id == null) {
            throw new InvalidArgumentValueException("Id cannot be null");
        }

        if (url == null) {
            throw new InvalidArgumentValueException("Url cannot be null");
        }

        this.id = id;
        this.url = url;
    }

    public static AdvertisementPhoto of(final Identifier id, final Url url) {
        return new AdvertisementPhoto(id, url);
    }

    public static AdvertisementPhoto create(final Url url) {
        return new AdvertisementPhoto(Identifier.generate(), url);
    }

    public Identifier getId() {
        return id;
    }

    public Url getUrl() {
        return url;
    }
}
