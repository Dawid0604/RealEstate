/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.password;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class PasswordAdapterTest {
    @Mock private PasswordEncoder passwordEncoder;
    private PasswordAdapter passwordAdapter;

    @BeforeEach
    void setUp() {
        passwordAdapter = new PasswordAdapter(passwordEncoder);
    }

    @Nested
    final class EncodeTests {

        @NullSource
        @ParameterizedTest
        @ValueSource(strings = "abc")
        @DisplayName("Should encode without exceptions")
        void shouldEncodeWithoutExceptions(final String value) {
            // Given
            // When
            // Then
            Assertions.assertThatCode(() -> passwordAdapter.encode(value))
                    .doesNotThrowAnyException();
            verify(passwordEncoder).encode(value);
        }

        @Test
        @DisplayName("Should encode and return encoded value")
        void shouldEncodeAndReturnEncodedValue() {
            // Given
            final String value = "abc";
            final String encodedValue = "$abcde";

            given(passwordEncoder.encode(value)).willReturn(encodedValue);

            // When
            final String result = passwordAdapter.encode(value);

            // Then
            Assertions.assertThat(result).isEqualTo(encodedValue);
        }
    }

    @Nested
    final class MatchesTests {

        @ParameterizedTest
        @CsvSource({"xyz,abc", "xyz,xyz", "xyz,", ",abc", ","})
        @DisplayName("Should verify without exceptions")
        void shouldVerifyWithoutExceptions(
                final String plainPassword, final String encodedPassword) {

            // Given
            // When
            // Then
            Assertions.assertThatCode(() -> passwordAdapter.matches(plainPassword, encodedPassword))
                    .doesNotThrowAnyException();

            verify(passwordEncoder).matches(plainPassword, encodedPassword);
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        @DisplayName("Should return verification result value")
        void shouldReturnVerificationResultValue(final boolean verificationValue) {
            // Given
            final String plainPassword = "abc";
            final String encodedPassword = "$abcde";

            given(passwordEncoder.matches(plainPassword, encodedPassword))
                    .willReturn(verificationValue);

            // When
            final boolean result = passwordAdapter.matches(plainPassword, encodedPassword);

            // Then
            Assertions.assertThat(result).isEqualTo(verificationValue);
        }
    }
}
