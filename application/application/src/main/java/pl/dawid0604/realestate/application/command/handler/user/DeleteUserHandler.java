/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.realestate.application.command.DeleteUserCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class DeleteUserHandler implements CommandHandler<DeleteUserCommand, Void> {
    private final UserRepository userRepository;

    @Override
    public Void handle(final DeleteUserCommand command) {
        if (!userRepository.existsByEmail(command.email())) {
            throw new UserNotFoundException(command.email());
        }

        userRepository.deleteByEmail(command.email());
        return null;
    }

    @Override
    public Class<DeleteUserCommand> getCommandType() {
        return DeleteUserCommand.class;
    }
}
