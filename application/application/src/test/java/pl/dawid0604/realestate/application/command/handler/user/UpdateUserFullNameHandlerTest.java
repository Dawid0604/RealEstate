/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyEmail;
import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyUserBuilder;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.domain.FullName;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.ForbiddenException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UpdateUserFullNameHandlerTest {
    @Mock private UserRepository userRepository;
    @Captor private ArgumentCaptor<User> userArgumentCaptor;
    @Captor private ArgumentCaptor<FullName> fullNameArgumentCaptor;
    private UpdateUserFullNameHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateUserFullNameHandler(userRepository);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        final UpdateUserFullNameCommand command = getCommand();

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
        final UpdateUserFullNameCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().status(UserStatus.INACTIVE).build();

        given(userRepository.findByEmail(command.email())).willReturn(Optional.of(foundUser));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(ForbiddenException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update full name")
    void shouldUpdateFullName() {
        // Given
        final UpdateUserFullNameCommand command = getCommand();
        final User foundUser = spy(getDummyUserBuilder().build());

        given(userRepository.findByEmail(command.email())).willReturn(Optional.of(foundUser));

        // When
        handler.handle(command);

        // Then
        verify(userRepository).save(userArgumentCaptor.capture());
        verify(foundUser).updateFullName(fullNameArgumentCaptor.capture());

        Assertions.assertThat(userArgumentCaptor.getValue()).isEqualTo(foundUser);
        Assertions.assertThat(fullNameArgumentCaptor.getValue())
                .satisfies(
                        v -> {
                            Assertions.assertThat(v.firstName()).isEqualTo(command.newFirstName());
                            Assertions.assertThat(v.lastName()).isEqualTo(command.newLastName());
                        });
    }

    private static UpdateUserFullNameCommand getCommand() {
        return new UpdateUserFullNameCommand(getDummyEmail(), "abc", "cde");
    }
}
