/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.token;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

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

@Component
@RequiredArgsConstructor(access = PACKAGE)
class RefreshTokenHandler implements CommandHandler<RefreshTokenCommand, TokenResponseDto> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public TokenResponseDto handle(final RefreshTokenCommand command) {
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

        return tokens;
    }

    private Identifier getUserId(final String userEmail) {
        return userRepository
                .findIdByEmail(userEmail)
                .map(Identifier::of)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
    }

    private void validateRefreshToken(final RefreshTokenCommand command, final Identifier userId) {
        if (!tokenRepository.isRefreshToken(command.refreshToken())) {
            throw new InvalidTokenException("Token is not a refresh token");
        }

        final RefreshToken refreshToken =
                refreshTokenRepository
                        .findByUserId(userId)
                        .orElseThrow(RefreshTokenNotFoundException::new);

        if (!refreshToken.tokenMatches(command.refreshToken())) {
            throw new InvalidTokenException("Given token is invalid or not exists");
        }

        if (refreshToken.isExpired()) {
            throw new ExpiredTokenException();
        }
    }

    @Override
    public Class<RefreshTokenCommand> getCommandType() {
        return RefreshTokenCommand.class;
    }
}
