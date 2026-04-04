package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateUserEmailCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Email;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateUserEmailHandler implements CommandHandler<UpdateUserEmailCommand, Void> {
    private final UserRepository userRepository;

    @Override
    public Void handle(final UpdateUserEmailCommand command) {
        User user =
                userRepository
                        .findByEmail(command.email())
                        .orElseThrow(() -> new UserNotFoundException(command.email()));

        user.verifyUser();
        user = user.updateEmail(new Email(command.newEmail()));
        userRepository.save(user);
        return null;
    }

    @Override
    public Class<UpdateUserEmailCommand> getCommandType() {
        return UpdateUserEmailCommand.class;
    }
}
