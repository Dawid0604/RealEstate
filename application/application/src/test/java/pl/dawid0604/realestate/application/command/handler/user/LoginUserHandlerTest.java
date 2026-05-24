/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.command.LoginUserCommand;
import pl.dawid0604.realestate.application.dto.auth.TokenResponseDto;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.domain.RefreshToken;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.PasswordRepository;
import pl.dawid0604.realestate.domain.port.out.RefreshTokenRepository;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.ForbiddenException;
import pl.dawid0604.realestate.domain.shared.exception.InvalidCredentialsException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.time.Instant;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LoginUserHandlerTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordRepository passwordRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Captor private ArgumentCaptor<RefreshToken> refreshTokenArgumentCaptor;
    private LoginUserHandler handler;

    @BeforeEach
    void setUp() {
        this.handler =
                new LoginUserHandler(
                        userRepository,
                        passwordRepository,
                        tokenRepository,
                        refreshTokenRepository);
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
    @DisplayName("Should throw exception when password does not match")
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        // Given
        final User user = UserFixture.getDummyUserBuilder().build();
        given(userRepository.findByEmail(getCommand().email())).willReturn(Optional.of(user));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getCommand()))
                .isExactlyInstanceOf(InvalidCredentialsException.class);

        verify(passwordRepository).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Should handle successfully")
    void shouldHandleSuccessfully() {
        // Given
        final LoginUserCommand command = getCommand();
        final User user = spy(UserFixture.getDummyUserBuilder().build());
        final String accessToken = "any-access-token";
        final String refreshToken = "any-refresh-token";
        final Instant tokenExpirationDate = Instant.now().plusSeconds(125_000);

        given(userRepository.findByEmail(getCommand().email())).willReturn(Optional.of(user));
        given(passwordRepository.matches(command.password(), user.getPassword().getValue()))
                .willReturn(true);

        given(tokenRepository.getTokenExpirationDate(refreshToken)).willReturn(tokenExpirationDate);
        given(tokenRepository.generateAccessToken(user.getEmail().value())).willReturn(accessToken);
        given(tokenRepository.generateRefreshToken(user.getEmail().value()))
                .willReturn(refreshToken);

        // When
        final TokenResponseDto result = handler.handle(command);

        // Then
        Assertions.assertThat(result)
                .isNotNull()
                .returns(accessToken, TokenResponseDto::accessToken)
                .returns(refreshToken, TokenResponseDto::refreshToken);

        verify(user).handleLogin();
        verify(refreshTokenRepository).deleteIfExistsByUserId(user.getId());
        verify(refreshTokenRepository).save(refreshTokenArgumentCaptor.capture());

        Assertions.assertThat(refreshTokenArgumentCaptor.getValue())
                .satisfies(
                        val -> {
                            Assertions.assertThat(val.getId()).isNotNull();
                            Assertions.assertThat(val.getUserId()).isEqualTo(user.getId());
                            Assertions.assertThat(val.getToken())
                                    .isNotBlank()
                                    .isNotEqualTo(refreshToken);

                            Assertions.assertThat(val.getExpiresAt())
                                    .isEqualTo(tokenExpirationDate);
                        });
    }

    private static LoginUserCommand getCommand() {
        return new LoginUserCommand("anyMail@mail.com", "anyPassword");
    }
}
