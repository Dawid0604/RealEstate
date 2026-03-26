/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.Optional;

public final class ContactDetails {
    private final Email email;
    private final PhoneNumber phoneNumber;

    public ContactDetails(final Email email, final PhoneNumber phoneNumber) {
        if (email == null && phoneNumber == null) {
            throw new InvalidArgumentValueException(
                    "At least one contact details must be provided");
        }

        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Optional<Email> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<PhoneNumber> getPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }
}
