/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isBlank;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.regex.Pattern;

public record Url(String value) {
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://");
    private static final int URL_MAX_LENGTH = 1024;

    public Url {
        if (value != null) {
            value = value.strip();
        }

        if (isBlank(value)) {
            throw new InvalidArgumentValueException("Value cannot be blank");
        }

        value = value.replace(SPACE, EMPTY);

        if (value.length() > URL_MAX_LENGTH) {
            throw new InvalidArgumentValueException(
                    "Value cannot be longer than " + URL_MAX_LENGTH + " characters");
        }

        if (!URL_PATTERN.matcher(value).find()) {
            throw new InvalidArgumentValueException("Value is invalid");
        }
    }
}
