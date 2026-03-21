/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.regex.Pattern;

public final class Password {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 72;
    private static final Pattern STRENGTH_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$");

    private final String value;

    private Password(final String value) {
        if (isBlank(value)) {
            throw new InvalidArgumentValueException("Value cannot be blank");
        }

        this.value = value.strip();
    }

    public static Password ofPlain(final String value) {
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

        return new Password(value);
    }

    public static Password ofHashed(final String value) {
        return new Password(value);
    }

    public String getValue() {
        return value;
    }
}
