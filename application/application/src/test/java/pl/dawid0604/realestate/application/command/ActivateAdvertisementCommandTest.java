package pl.dawid0604.realestate.application.command;

import static pl.dawid0604.realestate.application.fixture.AdvertisementFixture.getDummySlug;
import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyEmail;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

class ActivateAdvertisementCommandTest {
    private static Validator validator;
    private static ValidatorFactory validatorFactory;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Nested
    final class SlugTests {

        @Test
        @DisplayName("Should pass for valid slug")
        void shouldPassForValidSlug() {
            // Given
            final ActivateAdvertisementCommand command =
                    new ActivateAdvertisementCommand(getDummySlug().getValue(), getDummyEmail());

            // When
            final var violations = validator.validate(command);

            // Then
            Assertions.assertThat(violations).isEmpty();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should fail for blank slug")
        void shouldFailForBlankSlug(final String value) {
            // Given
            final ActivateAdvertisementCommand command =
                    new ActivateAdvertisementCommand(value, getDummyEmail());

            // When
            final var violations = validator.validate(command);

            // Then
            Assertions.assertThat(violations)
                    .anyMatch(
                            v ->
                                    v.getPropertyPath().toString().equals("slug")
                                            && v.getMessage().equals("Slug cannot be blank"));
        }

        @ParameterizedTest
        @MethodSource("boundarySlugDataProvider")
        @DisplayName("Should pass for boundary slug length")
        void shouldPassForBoundarySlugLength(final String value) {
            // Given
            final ActivateAdvertisementCommand command =
                    new ActivateAdvertisementCommand(value, getDummyEmail());

            // When
            final var violations = validator.validate(command);

            // Then
            Assertions.assertThat(violations).isEmpty();
        }

        private static Stream<Arguments> boundarySlugDataProvider() {
            return Stream.of(
                    Arguments.of(RandomStringUtils.secure().nextAlphanumeric(10)),
                    Arguments.of(RandomStringUtils.secure().nextAlphanumeric(99)),
                    Arguments.of(RandomStringUtils.secure().nextAlphanumeric(100)));
        }
    }

    @Nested
    final class UserEmailTests {

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "jan@example.com",
                    "jan.kowalski@example.com",
                    "jan+tag@example.com",
                    "jan@subdomain.example.com",
                    "jan123@example.pl",
                    "123@example.com",
                    "jan@example.co.uk"
                })
        @DisplayName("Should pass for valid user email")
        void shouldPassForValidUserEmail(final String value) {
            // Given
            final ActivateAdvertisementCommand command =
                    new ActivateAdvertisementCommand(getDummySlug().getValue(), value);

            // When
            final var violations = validator.validate(command);

            // Then
            Assertions.assertThat(violations).isEmpty();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should fail for blank user email")
        void shouldFailForBlankSlug(final String value) {
            // Given
            final ActivateAdvertisementCommand command =
                    new ActivateAdvertisementCommand(getDummySlug().getValue(), value);

            // When
            final var violations = validator.validate(command);

            // Then
            Assertions.assertThat(violations)
                    .anyMatch(
                            v ->
                                    v.getPropertyPath().toString().equals("userEmail")
                                            && v.getMessage().equals("Email cannot be blank"));
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "notanemail",
                    "@example.com",
                    "jan@",
                    "jan@.com",
                    "jan@example.",
                    "jan @example.com",
                    "jan@example.c"
                })
        @DisplayName("Should fail for invalid user email")
        void shouldFailForInvalidSlug(final String value) {
            // Given
            final ActivateAdvertisementCommand command =
                    new ActivateAdvertisementCommand(getDummySlug().getValue(), value);

            // When
            final var violations = validator.validate(command);

            // Then
            Assertions.assertThat(violations)
                    .anyMatch(
                            v ->
                                    v.getPropertyPath().toString().equals("userEmail")
                                            && v.getMessage().equals("Email must be valid"));
        }
    }
}
