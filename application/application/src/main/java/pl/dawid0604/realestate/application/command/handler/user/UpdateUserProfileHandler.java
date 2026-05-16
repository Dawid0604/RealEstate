/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateUserProfileCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.ContactDetails;
import pl.dawid0604.realestate.domain.Email;
import pl.dawid0604.realestate.domain.FullName;
import pl.dawid0604.realestate.domain.PhoneNumber;
import pl.dawid0604.realestate.domain.Url;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateUserProfileHandler implements CommandHandler<UpdateUserProfileCommand, Void> {
    private final UserRepository userRepository;

    @Override
    public Void handle(final UpdateUserProfileCommand command) {
        User user =
                userRepository
                        .findByEmail(command.email())
                        .orElseThrow(() -> new UserNotFoundException(command.email()));

        user.verifyUser();
        user = user.updateAvatar(new Url(command.avatarUrl()));
        user = user.updateType(command.type());
        user = user.updateFullName(new FullName(command.firstName(), command.lastName()));
        user =
                user.updateContactDetails(
                        new ContactDetails(
                                new Email(command.notificationEmail()),
                                new PhoneNumber(command.notificationPhoneNumber())));

        userRepository.save(user);
        return null;
    }

    @Override
    public Class<UpdateUserProfileCommand> getCommandType() {
        return UpdateUserProfileCommand.class;
    }
}
