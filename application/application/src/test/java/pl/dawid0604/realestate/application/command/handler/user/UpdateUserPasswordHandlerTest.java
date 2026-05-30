/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyEmail;
import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyUserBuilder;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.command.UpdateUserPasswordCommand;
import pl.dawid0604.realestate.domain.Password;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.PasswordRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.DifferentPasswordException;
import pl.dawid0604.realestate.domain.shared.exception.ForbiddenException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class UpdateUserPasswordHandlerTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordRepository passwordRepository;
    @Captor private ArgumentCaptor<User> userArgumentCaptor;
    @Captor private ArgumentCaptor<Password> passwordArgumentCaptor;
    private UpdateUserPasswordHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateUserPasswordHandler(userRepository, passwordRepository);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        final UpdateUserPasswordCommand command = getCommand();

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user is inactive")
    void shouldThrowExceptionWhenUserIsInactive() {
        // Given
        final UpdateUserPasswordCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().status(UserStatus.INACTIVE).build();

        given(userRepository.findByEmail(command.email())).willReturn(Optional.of(foundUser));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(ForbiddenException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when passwords does not match")
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        // Given
        final UpdateUserPasswordCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().password(Password.ofHashed("$abc")).build();

        given(userRepository.findByEmail(command.email())).willReturn(Optional.of(foundUser));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(DifferentPasswordException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update password")
    void shouldUpdatePassword() {
        // Given
        final UpdateUserPasswordCommand command = getCommand();
        final User foundUser = spy(getDummyUserBuilder().build());

        given(userRepository.findByEmail(command.email())).willReturn(Optional.of(foundUser));
        given(
                        passwordRepository.matches(
                                command.currentPassword(), foundUser.getPassword().getValue()))
                .willReturn(true);

        // When
        handler.handle(command);

        // Then
        verify(userRepository).save(userArgumentCaptor.capture());
        verify(foundUser).updatePassword(passwordArgumentCaptor.capture());

        Assertions.assertThat(userArgumentCaptor.getValue()).isEqualTo(foundUser);
        Assertions.assertThat(passwordArgumentCaptor.getValue())
                .satisfies(
                        v -> Assertions.assertThat(v.getValue()).isEqualTo(command.newPassword()));
    }

    private static UpdateUserPasswordCommand getCommand() {
        return new UpdateUserPasswordCommand(getDummyEmail(), "ABcDef123.@", "MyP4$$word");
    }
}
