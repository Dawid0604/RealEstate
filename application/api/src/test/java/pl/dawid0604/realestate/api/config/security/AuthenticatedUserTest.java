/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.core.GrantedAuthority;

import pl.dawid0604.realestate.domain.UserRole;

class AuthenticatedUserTest {

    @Test
    @DisplayName("Should create authenticated user")
    void shouldCreateAuthenticatedUser() {
        // Given
        // When
        final AuthenticatedUser authenticatedUser = new AuthenticatedUser(getEmail(), getRole());

        // Then
        Assertions.assertThat(authenticatedUser)
                .returns(getEmail(), AuthenticatedUser::getUsername)
                .returns(null, AuthenticatedUser::getPassword);

        Assertions.assertThat(authenticatedUser.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder(getRole().name());
    }

    @Test
    @DisplayName("Should be equal")
    void shouldBeEqual() {
        // Given
        // When
        final AuthenticatedUser authenticatedUser = new AuthenticatedUser(getEmail(), getRole());
        final AuthenticatedUser authenticatedUser2 = new AuthenticatedUser(getEmail(), getRole());

        // Then
        Assertions.assertThat(authenticatedUser).isEqualTo(authenticatedUser2);
    }

    @ParameterizedTest
    @CsvSource({"abcd,abcde", "abcd,abcdE", "abcd,ABCDE"})
    @DisplayName("Should not be equal")
    void shouldNotBeEqual(final String password, final String password2) {
        // Given
        // When
        final AuthenticatedUser authenticatedUser = new AuthenticatedUser(password, getRole());
        final AuthenticatedUser authenticatedUser2 = new AuthenticatedUser(password2, getRole());

        // Then
        Assertions.assertThat(authenticatedUser).isNotEqualTo(authenticatedUser2);
    }

    private static String getEmail() {
        return "anyMail@mail.com";
    }

    private static UserRole getRole() {
        return UserRole.ROLE_USER;
    }
}
