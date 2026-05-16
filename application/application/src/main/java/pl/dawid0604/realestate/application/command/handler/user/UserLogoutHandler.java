/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UserLogoutCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.port.out.RefreshTokenRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UserLogoutHandler implements CommandHandler<UserLogoutCommand, Void> {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Void handle(final UserLogoutCommand command) {
        final Identifier userId = getUserId(command.userEmail());
        refreshTokenRepository.deleteIfExistsByUserId(userId);
        return null;
    }

    @Override
    public Class<UserLogoutCommand> getCommandType() {
        return UserLogoutCommand.class;
    }

    private Identifier getUserId(final String userEmail) {
        return userRepository
                .findIdByEmail(userEmail)
                .map(Identifier::of)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
    }
}
