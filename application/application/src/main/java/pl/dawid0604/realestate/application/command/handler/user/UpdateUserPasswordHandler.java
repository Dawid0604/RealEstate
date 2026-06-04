/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateUserPasswordCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Password;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.PasswordRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.DifferentPasswordException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateUserPasswordHandler implements CommandHandler<UpdateUserPasswordCommand, Void> {
    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;

    @Override
    public Void handle(final UpdateUserPasswordCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        log.info("Updating user account password: email={}", command.email());

        User user =
                userRepository.findByEmail(command.email()).orElseThrow(throwException(command));

        user.verifyUser();

        if (!passwordRepository.matches(command.currentPassword(), user.getPassword().getValue())) {
            log.warn("Password update failed - invalid credentials: email={}", command.email());
            throw new DifferentPasswordException();
        }

        user = user.updatePassword(Password.ofPlain(command.newPassword()));
        userRepository.save(user);

        log.info("User account password updated");
        return null;
    }

    private static Supplier<UserNotFoundException> throwException(
            final UpdateUserPasswordCommand command) {

        return () -> {
            log.warn("User account not found: email={}", command.email());
            return new UserNotFoundException(command.email());
        };
    }

    @Override
    public Class<UpdateUserPasswordCommand> getCommandType() {
        return UpdateUserPasswordCommand.class;
    }
}
