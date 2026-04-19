/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.ActivateUserCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class ActivateUserHandler implements CommandHandler<ActivateUserCommand, Void> {
    private final UserRepository userRepository;

    @Override
    public Void handle(final ActivateUserCommand command) {
        User user =
                userRepository
                        .findByEmail(command.email())
                        .orElseThrow(() -> new UserNotFoundException(command.email()));

        user = user.activate();
        userRepository.save(user);
        return null;
    }

    @Override
    public Class<ActivateUserCommand> getCommandType() {
        return ActivateUserCommand.class;
    }
}
