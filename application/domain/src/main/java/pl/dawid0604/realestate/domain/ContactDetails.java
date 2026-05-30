/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record ContactDetails(Email email, PhoneNumber phoneNumber) {
    public ContactDetails {
        if (email == null && phoneNumber == null) {
            throw new InvalidArgumentValueException(
                    "At least one contact details must be provided");
        }
    }
}
