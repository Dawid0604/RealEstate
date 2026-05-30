/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

import pl.dawid0604.realestate.application.command.UpdateUserProfileCommand;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.domain.FullName;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.ForbiddenException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class UpdateUserProfileHandlerTest {
    @Mock private UserRepository userRepository;
    @Captor private ArgumentCaptor<User> userArgumentCaptor;
    private UpdateUserProfileHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new UpdateUserProfileHandler(userRepository);
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

        verify(userRepository).findByEmail(getCommand().email());
    }

    @Test
    @DisplayName("Should throw exception when user is not active")
    void shouldThrowExceptionWhenUserIsNotActive() {
        // Given
        final User user = UserFixture.getDummyUserBuilder().status(UserStatus.INACTIVE).build();
        given(userRepository.findByEmail(getCommand().email())).willReturn(Optional.of(user));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getCommand()))
                .isExactlyInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("Should handle successfully")
    void shouldHandleSuccessfully() {
        // Given
        final UpdateUserProfileCommand command = getCommand();
        final User user = UserFixture.getDummyUserBuilder().build();
        given(userRepository.findByEmail(command.email())).willReturn(Optional.of(user));

        // When
        handler.handle(command);

        // Then
        verify(userRepository).save(userArgumentCaptor.capture());

        Assertions.assertThat(userArgumentCaptor.getValue())
                .satisfies(
                        val -> {
                            Assertions.assertThat(val.getAvatar().value())
                                    .isEqualTo(command.avatarUrl());

                            Assertions.assertThat(val.getType()).isEqualTo(command.type());
                            Assertions.assertThat(val.getFullName())
                                    .returns(command.firstName(), FullName::firstName)
                                    .returns(command.lastName(), FullName::lastName);

                            Assertions.assertThat(val.getContactDetails())
                                    .returns(command.notificationEmail(), c -> c.email().value())
                                    .returns(
                                            command.notificationPhoneNumber(),
                                            c -> c.phoneNumber().value());
                        });
    }

    private static UpdateUserProfileCommand getCommand() {
        return new UpdateUserProfileCommand(
                "anymail@mail.com",
                "https://any-user-avatar.com/abc/1",
                "anynotification@mail.com",
                "999888777",
                "Johny",
                "Doesony",
                UserType.DEVELOPER);
    }
}
