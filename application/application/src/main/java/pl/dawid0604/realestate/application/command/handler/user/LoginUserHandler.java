/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.LoginUserCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.PasswordRepository;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.InvalidCredentialsException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;
import pl.dawid0604.realestate.domain.shared.user.LoginResponse;

// TODO: test it
@Component
@RequiredArgsConstructor(access = PACKAGE)
class LoginUserHandler implements CommandHandler<LoginUserCommand, LoginResponse> {
    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final TokenRepository tokenRepository;

    @Override
    public LoginResponse handle(final LoginUserCommand command) {
        User user =
                userRepository
                        .findByEmail(command.email())
                        .orElseThrow(() -> new UserNotFoundException(command.email()));

        user.verifyUser();
;
        if (!passwordRepository.matches(command.password(), user.getPassword().getValue())) {
            throw new InvalidCredentialsException();
        }

        final LoginResponse response =
                new LoginResponse(
                        tokenRepository.generateAccessToken(user.getEmail().value()),
                        tokenRepository.generateRefreshToken(user.getEmail().value()));

        user.handleLogin();
        userRepository.save(user);
        return response;
    }

    @Override
    public Class<LoginUserCommand> getCommandType() {
        return LoginUserCommand.class;
    }
}
