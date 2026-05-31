/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.token;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.RefreshTokenCommand;
import pl.dawid0604.realestate.application.dto.auth.TokenResponseDto;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.RefreshToken;
import pl.dawid0604.realestate.domain.port.out.RefreshTokenRepository;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.ExpiredTokenException;
import pl.dawid0604.realestate.domain.shared.exception.InvalidTokenException;
import pl.dawid0604.realestate.domain.shared.exception.RefreshTokenNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor(access = PACKAGE)
class RefreshTokenHandler implements CommandHandler<RefreshTokenCommand, TokenResponseDto> {
    private static final int TOKEN_PREVIEW_MAX_LENGTH = 20;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public TokenResponseDto handle(final RefreshTokenCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        log.info("Refresh token attempt: token={}", getTokenPreview(command.refreshToken()));

        final String userEmail = tokenRepository.getUserEmail(command.refreshToken());
        final Identifier userId = getUserId(userEmail);

        validateRefreshToken(command, userId);
        return generateNewTokens(userEmail, userId);
    }

    private TokenResponseDto generateNewTokens(final String userEmail, final Identifier userId) {
        final TokenResponseDto tokens =
                new TokenResponseDto(
                        tokenRepository.generateAccessToken(userEmail),
                        tokenRepository.generateRefreshToken(userEmail));

        refreshTokenRepository.deleteIfExistsByUserId(userId);
        refreshTokenRepository.save(
                RefreshToken.create(
                        userId,
                        tokens.refreshToken(),
                        tokenRepository.getTokenExpirationDate(tokens.refreshToken())));

        log.info("Token refreshed, new tokens generated");
        return tokens;
    }

    private Identifier getUserId(final String userEmail) {
        return userRepository
                .findIdByEmail(userEmail)
                .map(Identifier::of)
                .orElseThrow(throwUserNotFoundException(userEmail));
    }

    private void validateRefreshToken(final RefreshTokenCommand command, final Identifier userId) {
        if (!tokenRepository.isRefreshToken(command.refreshToken())) {
            log.warn(
                    "Invalid token type, expected refresh token: token={}",
                    getTokenPreview(command.refreshToken()));

            throw new InvalidTokenException("Token is not a refresh token");
        }

        final RefreshToken refreshToken =
                refreshTokenRepository
                        .findByUserId(userId)
                        .orElseThrow(throwRefreshTokenNotFoundException());

        if (!refreshToken.tokenMatches(command.refreshToken())) {
            log.warn("Tokens does not matches: token={}", getTokenPreview(command.refreshToken()));
            throw new InvalidTokenException("Given token does not matches");
        }

        if (refreshToken.isExpired()) {
            log.warn("Token expired: token={}", getTokenPreview(command.refreshToken()));
            throw new ExpiredTokenException();
        }
    }

    private static Supplier<UserNotFoundException> throwUserNotFoundException(
            final String userEmail) {

        return () -> {
            log.warn("User account not found: email={}", userEmail);
            return new UserNotFoundException(userEmail);
        };
    }

    private static Supplier<RefreshTokenNotFoundException> throwRefreshTokenNotFoundException() {
        return () -> {
            log.warn("Refresh token not found");
            return new RefreshTokenNotFoundException();
        };
    }

    private static String getTokenPreview(final String token) {
        return token.length() >= TOKEN_PREVIEW_MAX_LENGTH
                ? token.substring(TOKEN_PREVIEW_MAX_LENGTH)
                : token;
    }

    @Override
    public Class<RefreshTokenCommand> getCommandType() {
        return RefreshTokenCommand.class;
    }
}
