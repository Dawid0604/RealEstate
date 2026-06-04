/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.DeleteUserCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(access = PACKAGE)
class DeleteUserHandler implements CommandHandler<DeleteUserCommand, Void> {
    private final UserRepository userRepository;

    @Override
    public Void handle(final DeleteUserCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        log.info("Deleting user account: email={}", command.email());

        if (!userRepository.existsByEmail(command.email())) {
            log.warn("User account not found: email={}", command.email());
            throw new UserNotFoundException(command.email());
        }

        userRepository.deleteByEmail(command.email());
        log.info("User account deleted");
        return null;
    }

    @Override
    public Class<DeleteUserCommand> getCommandType() {
        return DeleteUserCommand.class;
    }
}
