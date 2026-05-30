/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config.security;

import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.port.out.UserRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceCustomTest {
    @Mock private UserRepository userRepository;
    private UserDetailsServiceCustom userDetailsService;

    @BeforeEach
    void setUp() {
        this.userDetailsService = new UserDetailsServiceCustom(userRepository);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUerNotFound() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> userDetailsService.loadUserByUsername(getUsername()))
                .isExactlyInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("Should load user successfully")
    void shouldLoadUserSuccessfully() {
        // Given
        final UserRole role = UserRole.ROLE_USER;
        given(userRepository.findUserRoleByEmail(getUsername())).willReturn(Optional.of(role));

        // When
        final UserDetails result = userDetailsService.loadUserByUsername(getUsername());

        // Then
        Assertions.assertThat(result).isInstanceOf(AuthenticatedUser.class);
    }

    private static String getUsername() {
        return "anyMail@mail.com";
    }
}
