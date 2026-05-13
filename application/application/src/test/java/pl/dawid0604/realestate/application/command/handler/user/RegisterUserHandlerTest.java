/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static org.mockito.BDDMockito.*;

import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyEmail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import pl.dawid0604.realestate.application.command.RegisterUserCommand;
import pl.dawid0604.realestate.domain.Email;
import pl.dawid0604.realestate.domain.Password;
import pl.dawid0604.realestate.domain.PhoneNumber;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.port.out.PasswordRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.event.UserRegisteredEvent;
import pl.dawid0604.realestate.domain.shared.exception.UserExistsException;

import java.util.Objects;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RegisterUserHandlerTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordRepository passwordRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Captor private ArgumentCaptor<User> userArgumentCaptor;
    private RegisterUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new RegisterUserHandler(userRepository, passwordRepository, eventPublisher);
    }

    @Test
    @DisplayName("Should throw exception when user exists")
    void throwExceptionWhenUserExists() {
        // Given
        final RegisterUserCommand command = getCommand();

        given(userRepository.existsByEmail(command.username())).willReturn(true);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(UserExistsException.class);

        verify(userRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("Should register")
    void shouldRegister() {
        // Given
        final RegisterUserCommand command = getCommand();
        final String hashedPassword = "anyEncryptedPassword";

        given(passwordRepository.encode(command.password())).willReturn(hashedPassword);

        // When
        final UUID result = handler.handle(command);

        // Then
        verify(userRepository).save(userArgumentCaptor.capture());
        verify(eventPublisher).publishEvent(any(UserRegisteredEvent.class));
        verify(userRepository).existsByEmail(command.username());

        Assertions.assertThat(result).isEqualTo(userArgumentCaptor.getValue().getId().getValue());
        Assertions.assertThat(userArgumentCaptor.getValue())
                .satisfies(
                        user -> {
                            Assertions.assertThat(user.getEmail().value())
                                    .isEqualTo(command.username());

                            Assertions.assertThat(user.getType()).isEqualTo(UserType.DEVELOPER);

                            Assertions.assertThat(user.getPassword())
                                    .matches(Password::isHashed)
                                    .matches(p -> Objects.equals(p.getValue(), hashedPassword));

                            Assertions.assertThat(user.getFullName())
                                    .matches(
                                            n -> Objects.equals(n.firstName(), command.firstName()))
                                    .matches(n -> Objects.equals(n.lastName(), command.lastName()));

                            Assertions.assertThat(user.isAdmin()).isFalse();

                            Assertions.assertThat(user.getContactDetails())
                                    .satisfies(
                                            c -> {
                                                Assertions.assertThat(c.getEmail())
                                                        .map(Email::value)
                                                        .hasValue(command.notificationEmail());

                                                Assertions.assertThat(c.getPhoneNumber())
                                                        .map(PhoneNumber::value)
                                                        .hasValue(command.notificationPhoneNumber());
                                            });
                        });
    }

    private static RegisterUserCommand getCommand() {
        return new RegisterUserCommand(
                getDummyEmail(),
                "Password123.@d",
                "firstName",
                "lastName",
                "DEVELOPER",
                "cde@mail.com",
                "123456789");
    }
}
