/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateUserPasswordCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Password;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.PasswordEncoder;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.DifferentPasswordException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateUserPasswordHandler implements CommandHandler<UpdateUserPasswordCommand, Void> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Void handle(final UpdateUserPasswordCommand command) {
        User user =
                userRepository
                        .findByEmail(command.email())
                        .orElseThrow(() -> new UserNotFoundException(command.email()));

        user.verifyUser();

        if (!passwordEncoder.matches(command.currentPassword(), user.getPassword().getValue())) {
            throw new DifferentPasswordException();
        }

        user = user.updatePassword(Password.ofPlain(command.newPassword()));
        userRepository.save(user);

        return null;
    }

    @Override
    public Class<UpdateUserPasswordCommand> getCommandType() {
        return UpdateUserPasswordCommand.class;
    }
}
