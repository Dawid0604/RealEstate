/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateUserTypeCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateUserTypeHandler implements CommandHandler<UpdateUserTypeCommand, Void> {
    private final UserRepository userRepository;

    @Override
    public Void handle(final UpdateUserTypeCommand command) {
        User user =
                userRepository
                        .findByEmail(command.email())
                        .orElseThrow(() -> new UserNotFoundException(command.email()));

        user.verifyUser();
        user = user.updateType(UserType.of(command.type()));

        userRepository.save(user);
        return null;
    }

    @Override
    public Class<UpdateUserTypeCommand> getCommandType() {
        return UpdateUserTypeCommand.class;
    }
}
