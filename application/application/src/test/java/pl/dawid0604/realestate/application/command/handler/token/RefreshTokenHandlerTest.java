/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.token;

import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.command.RefreshTokenCommand;
import pl.dawid0604.realestate.application.dto.auth.TokenResponseDto;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.RefreshToken;
import pl.dawid0604.realestate.domain.port.out.RefreshTokenRepository;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.ExpiredTokenException;
import pl.dawid0604.realestate.domain.shared.exception.InvalidTokenException;
import pl.dawid0604.realestate.domain.shared.exception.RefreshTokenNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class RefreshTokenHandlerTest {
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private UserRepository userRepository;
    @Captor private ArgumentCaptor<RefreshToken> refreshTokenArgumentCaptor;
    private RefreshTokenHandler handler;

    @BeforeEach
    void setUp() {
        this.handler =
                new RefreshTokenHandler(refreshTokenRepository, tokenRepository, userRepository);
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
    @DisplayName("Should throw exception when token is not of refresh type")
    void shouldThrowExceptionWhenTokenIsNotOfRefreshType() {
        // Given
        final RefreshTokenCommand command = getCommand();
        final UUID userId = UUID.randomUUID();
        final String email = UserFixture.getDummyEmail();

        given(tokenRepository.getUserEmail(command.refreshToken())).willReturn(email);
        given(userRepository.findIdByEmail(email)).willReturn(Optional.of(userId));
        given(tokenRepository.isRefreshToken(command.refreshToken())).willReturn(false);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getCommand()))
                .isExactlyInstanceOf(InvalidTokenException.class)
                .hasMessage("Token is not a refresh token");
    }

    @Test
    @DisplayName("Should throw exception when refresh token not found")
    void shouldThrowExceptionWhenRefreshTokenNotFound() {
        // Given
        final RefreshTokenCommand command = getCommand();
        final UUID userId = UUID.randomUUID();
        final String email = UserFixture.getDummyEmail();

        given(tokenRepository.getUserEmail(command.refreshToken())).willReturn(email);
        given(userRepository.findIdByEmail(email)).willReturn(Optional.of(userId));
        given(tokenRepository.isRefreshToken(command.refreshToken())).willReturn(true);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getCommand()))
                .isExactlyInstanceOf(RefreshTokenNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw exception when token does not matches")
    void shouldThrowExceptionWhenTokenDoesNotMatches() {
        // Given
        final RefreshTokenCommand command = getCommand();
        final UUID userId = UUID.randomUUID();
        final String email = UserFixture.getDummyEmail();
        final RefreshToken refreshToken =
                RefreshToken.create(
                        Identifier.generate(), "abc-xyz", Instant.now().plusMillis(125_000));

        given(tokenRepository.getUserEmail(command.refreshToken())).willReturn(email);
        given(userRepository.findIdByEmail(email)).willReturn(Optional.of(userId));
        given(tokenRepository.isRefreshToken(command.refreshToken())).willReturn(true);
        given(refreshTokenRepository.findByUserId(Identifier.of(userId)))
                .willReturn(Optional.of(refreshToken));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getCommand()))
                .isExactlyInstanceOf(InvalidTokenException.class)
                .hasMessage("Given token does not matches");
    }

    @Test
    @DisplayName("Should throw exception when token is expired")
    void shouldThrowExceptionWhenTokenIsExpired() {
        // Given
        final RefreshTokenCommand command = getCommand();
        final UUID userId = UUID.randomUUID();
        final String email = UserFixture.getDummyEmail();
        final String hashedToken =
                "0eb17643d4e9261163783a420859c92c7d212fa9624106a12b510afbec266120";

        final RefreshToken refreshToken =
                RefreshToken.reconstitute(
                        Identifier.generate(),
                        Identifier.of(userId),
                        hashedToken,
                        Instant.now(),
                        Instant.now().minusMillis(125_000));

        given(tokenRepository.getUserEmail(command.refreshToken())).willReturn(email);
        given(userRepository.findIdByEmail(email)).willReturn(Optional.of(userId));
        given(tokenRepository.isRefreshToken(command.refreshToken())).willReturn(true);
        given(refreshTokenRepository.findByUserId(Identifier.of(userId)))
                .willReturn(Optional.of(refreshToken));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(getCommand()))
                .isExactlyInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("Should handle successfully")
    void shouldHandleSuccessfully() {
        // Given
        final RefreshTokenCommand command = getCommand();
        final UUID userId = UUID.randomUUID();
        final String email = UserFixture.getDummyEmail();
        final String hashedToken =
                "0eb17643d4e9261163783a420859c92c7d212fa9624106a12b510afbec266120";

        final Instant newExpirationDate = Instant.now().plusMillis(250_000);
        final String generatedAccessToken = "new-access-token";
        final String generatedRefreshToken = "new-refresh-token";
        final String hashedGeneratedRefreshToken =
                "c40dd1765d767caae2588f0ee1de9181d8a44cc9306261eb2c9e526351188338";

        final RefreshToken refreshToken =
                RefreshToken.reconstitute(
                        Identifier.generate(),
                        Identifier.of(userId),
                        hashedToken,
                        Instant.now(),
                        Instant.now().plusMillis(125_000));

        given(tokenRepository.getUserEmail(command.refreshToken())).willReturn(email);
        given(userRepository.findIdByEmail(email)).willReturn(Optional.of(userId));
        given(tokenRepository.isRefreshToken(command.refreshToken())).willReturn(true);
        given(refreshTokenRepository.findByUserId(Identifier.of(userId)))
                .willReturn(Optional.of(refreshToken));

        given(tokenRepository.generateAccessToken(email)).willReturn(generatedAccessToken);
        given(tokenRepository.generateRefreshToken(email)).willReturn(generatedRefreshToken);
        given(tokenRepository.getTokenExpirationDate(generatedRefreshToken))
                .willReturn(newExpirationDate);

        // When
        final TokenResponseDto result = handler.handle(command);

        // Then
        Assertions.assertThat(result)
                .returns(generatedAccessToken, TokenResponseDto::accessToken)
                .returns(generatedRefreshToken, TokenResponseDto::refreshToken);

        verify(refreshTokenRepository).deleteIfExistsByUserId(Identifier.of(userId));
        verify(refreshTokenRepository).save(refreshTokenArgumentCaptor.capture());

        Assertions.assertThat(refreshTokenArgumentCaptor.getValue())
                .returns(hashedGeneratedRefreshToken, RefreshToken::getToken)
                .returns(userId, r -> r.getUserId().getValue())
                .returns(newExpirationDate, RefreshToken::getExpiresAt);
    }

    private static RefreshTokenCommand getCommand() {
        return new RefreshTokenCommand("refresh-token");
    }
}
