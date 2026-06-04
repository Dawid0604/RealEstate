/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Objects;
import java.util.regex.Pattern;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class Password {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 72;
    private static final Pattern STRENGTH_PATTERN =
            Pattern.compile(
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&,.])[A-Za-z\\d@$!%*?&.,]+$");

    private final String value;
    private final boolean hashed;

    private Password(final String value, final boolean hashed) {
        if (isBlank(value)) {
            throw new InvalidArgumentValueException("Value cannot be blank");
        }

        if (!hashed) {
            verifyPlainPassword(value);
        }

        this.value = value;
        this.hashed = hashed;
    }

    private static void verifyPlainPassword(final String value) {
        if (value.length() < MIN_LENGTH) {
            throw new InvalidArgumentValueException("Value cannot be less than " + MIN_LENGTH);
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidArgumentValueException("Value cannot be longer than " + MAX_LENGTH);
        }

        if (!STRENGTH_PATTERN.matcher(value).matches()) {
            throw new InvalidArgumentValueException(
                    "Password must contain uppercase, lowercase, digit and special character");
        }
    }

    public static Password ofPlain(final String value) {
        return new Password(value, false);
    }

    public static Password ofHashed(final String value) {
        return new Password(value, true);
    }

    public String getValue() {
        return value;
    }

    public boolean isHashed() {
        return hashed;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof final Password other && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
