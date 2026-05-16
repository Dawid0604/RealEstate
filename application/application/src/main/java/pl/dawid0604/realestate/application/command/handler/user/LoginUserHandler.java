/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.LoginUserCommand;
import pl.dawid0604.realestate.application.dto.auth.TokenResponseDto;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.RefreshToken;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.PasswordRepository;
import pl.dawid0604.realestate.domain.port.out.RefreshTokenRepository;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.InvalidCredentialsException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.time.Instant;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class LoginUserHandler implements CommandHandler<LoginUserCommand, TokenResponseDto> {
    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public TokenResponseDto handle(final LoginUserCommand command) {
        User user =
                userRepository
                        .findByEmail(command.email())
                        .orElseThrow(() -> new UserNotFoundException(command.email()));

        user.verifyUser();

        if (!passwordRepository.matches(command.password(), user.getPassword().getValue())) {
            throw new InvalidCredentialsException();
        }

        final TokenResponseDto response =
                new TokenResponseDto(
                        tokenRepository.generateAccessToken(user.getEmail().value()),
                        tokenRepository.generateRefreshToken(user.getEmail().value()));

        refreshTokenRepository.deleteIfExistsByUserId(user.getId());
        refreshTokenRepository.save(generateRefreshToken(user.getId(), response.refreshToken()));

        user.handleLogin();
        userRepository.save(user);
        return response;
    }

    @Override
    public Class<LoginUserCommand> getCommandType() {
        return LoginUserCommand.class;
    }

    private RefreshToken generateRefreshToken(final Identifier userId, final String refreshToken) {
        final Instant expirationDate = tokenRepository.getTokenExpirationDate(refreshToken);
        return RefreshToken.create(userId, refreshToken, expirationDate);
    }
}
