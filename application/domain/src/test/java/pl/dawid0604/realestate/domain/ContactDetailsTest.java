/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class ContactDetailsTest {

    @Test
    @DisplayName("Should throw exception when both values are null")
    void shouldThrowExceptionWhenBothValuesAreNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new ContactDetails(null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("At least one contact details must be provided");
    }

    @Test
    @DisplayName(
            "Should create instance successfully when email is present and phone number is null")
    void shouldCreateInstanceSuccessfullyWhenEmailIsPresentAndPhoneNumberIsNull() {
        // Given
        final Email email = getValidEmail();

        // When
        final ContactDetails instance = new ContactDetails(email, null);

        // Then
        Assertions.assertThat(instance.email()).isEqualTo(email);
        Assertions.assertThat(instance.phoneNumber()).isNull();
    }

    @Test
    @DisplayName(
            "Should create instance successfully when email is null and phone number is present")
    void shouldCreateInstanceSuccessfullyWhenEmailIsNullAndPhoneNumberIsPresent() {
        // Given
        final PhoneNumber phoneNumber = getValidPhoneNumber();

        // When
        final ContactDetails instance = new ContactDetails(null, phoneNumber);

        // Then
        Assertions.assertThat(instance.phoneNumber()).isEqualTo(phoneNumber);
        Assertions.assertThat(instance.email()).isNull();
    }

    @Test
    @DisplayName("Should create instance successfully when both values are present")
    void shouldCreateInstanceSuccessfullyWhenBothValuesArePresent() {
        // Given
        final Email email = getValidEmail();
        final PhoneNumber phoneNumber = getValidPhoneNumber();

        // When
        final ContactDetails instance = new ContactDetails(email, phoneNumber);

        // Then
        Assertions.assertThat(instance.phoneNumber()).isEqualTo(phoneNumber);
        Assertions.assertThat(instance.email()).isEqualTo(email);
    }

    private static Email getValidEmail() {
        return new Email("anyemail@mail.com");
    }

    private static PhoneNumber getValidPhoneNumber() {
        return new PhoneNumber("123123123");
    }
}
