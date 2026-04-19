/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateUserAvatarCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Url;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateUserAvatarHandler implements CommandHandler<UpdateUserAvatarCommand, Void> {
    private final UserRepository userRepository;

    @Override
    public Void handle(final UpdateUserAvatarCommand command) {
        User user =
                userRepository
                        .findByEmail(command.email())
                        .orElseThrow(() -> new UserNotFoundException(command.email()));

        user.verifyUser();
        user = user.updateAvatar(new Url(command.newAvatarUrl()));
        userRepository.save(user);
        return null;
    }

    @Override
    public Class<UpdateUserAvatarCommand> getCommandType() {
        return UpdateUserAvatarCommand.class;
    }
}
