package pl.dawid0604.realestate.application.command.handler.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.command.ActivateUserCommand;
import pl.dawid0604.realestate.domain.ContactDetails;
import pl.dawid0604.realestate.domain.Email;
import pl.dawid0604.realestate.domain.FullName;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.Password;
import pl.dawid0604.realestate.domain.PhoneNumber;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.time.Instant;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ActivateUserHandlerTest {
    @Mock private UserRepository userRepository;
    @Captor private ArgumentCaptor<User> userArgumentCaptor;
    private ActivateUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ActivateUserHandler(userRepository);
    }

    @Test
    @DisplayName("Should throw exception when email not found")
    void shouldThrowExceptionWhenEmailNotFound() {
        // Given
        final ActivateUserCommand command = getCommand();

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should activate user")
    void shouldActivateUser() {
        // Given
        final ActivateUserCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().build();

        given(userRepository.findByEmail(command.email())).willReturn(Optional.of(foundUser));

        // When
        handler.handle(command);

        // Then
        verify(userRepository).save(userArgumentCaptor.capture());
        Assertions.assertThat(userArgumentCaptor.getValue())
                .isEqualTo(foundUser)
                .matches(User::canLogin);
    }

    @Test
    @DisplayName("Should not handle exception when domain it throws")
    void shouldNotHandleExceptionWhenDomainItThrows() {
        // Given
        final ActivateUserCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().status(UserStatus.ACTIVE).build();

        given(userRepository.findByEmail(command.email())).willReturn(Optional.of(foundUser));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(InvalidArgumentValueException.class);

        verify(userRepository, never()).save(any());
    }

    private static String getDummyEmail() {
        return "xyz@mail.com";
    }

    private static ActivateUserCommand getCommand() {
        return new ActivateUserCommand(getDummyEmail());
    }

    private static User.Builder getDummyUserBuilder() {
        return User.reconstitute()
                .id(Identifier.generate())
                .createdAt(Instant.now())
                .email(new Email(getDummyEmail()))
                .password(Password.ofHashed("$xyz"))
                .fullName(new FullName("John", "Doe"))
                .status(UserStatus.INACTIVE)
                .role(UserRole.USER_ROLE)
                .contactDetails(
                        new ContactDetails(
                                new Email(getDummyEmail()), new PhoneNumber("+48123456789")));
    }
}
