/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class AdvertisementPhoto {
    private final Identifier id;
    private final Url url;
    private final int position;

    private AdvertisementPhoto(final Identifier id, final Url url, final int position) {
        if (id == null) {
            throw new InvalidArgumentValueException("Id cannot be null");
        }

        if (url == null) {
            throw new InvalidArgumentValueException("Url cannot be null");
        }

        if (position < 0) {
            throw new InvalidArgumentValueException("Position cannot be negative");
        }

        this.id = id;
        this.url = url;
        this.position = position;
    }

    public static AdvertisementPhoto of(final Identifier id, final Url url, final int position) {
        return new AdvertisementPhoto(id, url, position);
    }

    public static AdvertisementPhoto create(final Url url, final int position) {
        return new AdvertisementPhoto(Identifier.generate(), url, position);
    }

    public Identifier getId() {
        return id;
    }

    public Url getUrl() {
        return url;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof final AdvertisementPhoto other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
