/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.ActivateUserCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor(access = PACKAGE)
class ActivateUserHandler implements CommandHandler<ActivateUserCommand, Void> {
    private final UserRepository userRepository;

    @Override
    public Void handle(final ActivateUserCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        log.info("Activating user account: email={}", command.email());

        User user =
                userRepository.findByEmail(command.email()).orElseThrow(throwException(command));

        user = user.activate();
        userRepository.save(user);

        log.info("User account activated");
        return null;
    }

    private static Supplier<UserNotFoundException> throwException(
            final ActivateUserCommand command) {

        return () -> {
            log.warn("User account not found: email={}", command.email());
            return new UserNotFoundException(command.email());
        };
    }

    @Override
    public Class<ActivateUserCommand> getCommandType() {
        return ActivateUserCommand.class;
    }
}
