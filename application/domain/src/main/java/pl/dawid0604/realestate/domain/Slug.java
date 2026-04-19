/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;

import com.github.slugify.Slugify;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class Slug {
    private final String value;
    private static final String SEPARATOR = "-";
    private static final int SUFFIX_LENGTH = 6;
    private static final int SLUG_MIN_LENGTH = 10;
    private static final int SLUG_MAX_LENGTH = 100;
    private static final Locale LOCALE = Locale.forLanguageTag("pl-pl");
    private static final Pattern VALID_SLUG_PATTERN = Pattern.compile("[a-z0-9-]+");

    private static final Slugify SLUGIFY =
            Slugify.builder().lowerCase(true).locale(LOCALE).underscoreSeparator(false).build();

    private Slug(final String slug) {
        if (isBlank(slug)) {
            throw new InvalidArgumentValueException("Slug cannot be blank");
        }

        if (!VALID_SLUG_PATTERN.matcher(slug).matches()) {
            throw new InvalidArgumentValueException("Given slug is invalid");
        }

        if (slug.length() < SLUG_MIN_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Slug cannot be shorter than " + SLUG_MIN_LENGTH + " characters");
        }

        if (slug.length() > SLUG_MAX_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Slug cannot be longer than " + SLUG_MAX_LENGTH + " characters");
        }

        this.value = slug;
    }

    public static Slug of(final String slug) {
        return new Slug(slug);
    }

    public static Slug create(final Title title) {
        if (title == null) {
            throw new InvalidArgumentValueException("Title cannot be null");
        }

        final String slug = SLUGIFY.slugify(title.value()) + SEPARATOR + getRandomSuffix();
        return new Slug(slug);
    }

    private static String getRandomSuffix() {
        return RandomStringUtils.secure().nextAlphanumeric(SUFFIX_LENGTH).toLowerCase(LOCALE);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof final Slug slug && Objects.equals(slug.value, value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
