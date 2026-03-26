/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String value) {
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 254;
    private static final Locale LOCALE = Locale.forLanguageTag("pl-pl");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    public Email {
        if (value != null) {
            value = value.strip();
        }

        if (isBlank(value)) {
            throw new InvalidArgumentValueException("Value cannot be blank");
        }

        if (value.length() < MIN_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Value cannot be less than " + MIN_LENGTH + " characters");
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Value cannot be longer than " + MAX_LENGTH + " characters");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new InvalidArgumentValueException("Value is invalid");
        }

        value = value.toLowerCase(LOCALE);
    }
}
