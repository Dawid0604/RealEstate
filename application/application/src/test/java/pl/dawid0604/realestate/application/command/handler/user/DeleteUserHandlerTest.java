/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyEmail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.command.DeleteUserCommand;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class DeleteUserHandlerTest {
    @Mock private UserRepository userRepository;
    private DeleteUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DeleteUserHandler(userRepository);
    }

    @Test
    @DisplayName("Should throw exception when user not exists")
    void shouldThrowExceptionWhenUserNotExists() {
        // Given
        final DeleteUserCommand command = getCommand();

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).deleteByEmail(any());
        verify(userRepository).existsByEmail(command.email());
    }

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        // Given
        final DeleteUserCommand command = getCommand();

        given(userRepository.existsByEmail(command.email())).willReturn(true);

        // When
        handler.handle(command);

        // Then
        verify(userRepository).deleteByEmail(command.email());
    }

    private static DeleteUserCommand getCommand() {
        return new DeleteUserCommand(getDummyEmail());
    }
}
