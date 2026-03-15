/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.github.slugify.Slugify;

import org.apache.commons.lang3.RandomStringUtils;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.Locale;

public final class Slug {
    private final String value;
    private static final String SEPARATOR = "-";
    private static final int SUFFIX_LENGTH = 6;
    private static final Locale LOCALE = Locale.forLanguageTag("pl-pl");

    private static final Slugify SLUGIFY =
            Slugify.builder().lowerCase(true).locale(LOCALE).underscoreSeparator(false).build();

    private Slug(final String slug) {
        if (isBlank(slug)) {
            throw new InvalidArgumentValueException("Slug cannot be blank");
        }

        this.value = slug;
    }

    public static Slug of(final String slug) {
        return new Slug(slug);
    }

    public static Slug create(final String title) {
        if (isBlank(title)) {
            throw new InvalidArgumentValueException("Title cannot be blank");
        }

        final String randomSuffix =
                RandomStringUtils.secure().nextAlphanumeric(SUFFIX_LENGTH).toLowerCase(LOCALE);

        final String slug = SLUGIFY.slugify(title) + SEPARATOR + randomSuffix;
        return new Slug(slug);
    }

    public String getValue() {
        return value;
    }
}
