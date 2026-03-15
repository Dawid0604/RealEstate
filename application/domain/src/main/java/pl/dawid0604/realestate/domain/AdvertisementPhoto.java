/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.regex.Pattern;

public final class AdvertisementPhoto {
    private final Identifier id;
    private final String url;
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://");

    private AdvertisementPhoto(final Identifier id, final String url) {
        if (id == null) {
            throw new InvalidArgumentValueException("Id cannot be null");
        }

        if (isBlank(url)) {
            throw new InvalidArgumentValueException("Url cannot be blank");
        }

        if (!URL_PATTERN.matcher(url).find()) {
            throw new InvalidArgumentValueException("Url must be a valid URL");
        }

        this.id = id;
        this.url = url;
    }

    public static AdvertisementPhoto of(final Identifier id, final String url) {
        return new AdvertisementPhoto(id, url);
    }

    public static AdvertisementPhoto create(final String url) {
        return new AdvertisementPhoto(Identifier.generate(), url);
    }

    public Identifier getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
