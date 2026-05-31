/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.BanUserCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor(access = PACKAGE)
class BanUserHandler implements CommandHandler<BanUserCommand, Void> {
    private final UserRepository userRepository;

    @Override
    public Void handle(final BanUserCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        log.info("Banning user account: email={}", command.email());

        User user =
                userRepository.findByEmail(command.email()).orElseThrow(throwException(command));

        user = user.ban();
        userRepository.save(user);

        log.info("User account banned");
        return null;
    }

    private static Supplier<UserNotFoundException> throwException(final BanUserCommand command) {
        return () -> {
            log.warn("User account not found: email={}", command.email());
            return new UserNotFoundException(command.email());
        };
    }

    @Override
    public Class<BanUserCommand> getCommandType() {
        return BanUserCommand.class;
    }
}
