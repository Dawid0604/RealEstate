/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.command.UserLogoutCommand;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.port.out.RefreshTokenRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserLogoutHandlerTest {
    @Mock private UserRepository userRepository;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private UserLogoutHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new UserLogoutHandler(userRepository, refreshTokenRepository);
    }

    @Test
    @DisplayName("Should throw exception when command is null")
    void shouldThrowExceptionWhenCommandIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("Command cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getCommand()))
                .isExactlyInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Should handle successfully")
    void shouldHandleSuccessfully() {
        // Given
        final UserLogoutCommand command = getCommand();
        final UUID userId = UUID.randomUUID();
        given(userRepository.findIdByEmail(command.userEmail())).willReturn(Optional.of(userId));

        // When
        handler.handle(command);

        // Then
        verify(refreshTokenRepository).deleteIfExistsByUserId(Identifier.of(userId));
    }

    private static UserLogoutCommand getCommand() {
        return new UserLogoutCommand("anyMail@mail.com");
    }
}
