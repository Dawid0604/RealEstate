/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateUserContactDetailsCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.ContactDetails;
import pl.dawid0604.realestate.domain.Email;
import pl.dawid0604.realestate.domain.PhoneNumber;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateUserContactDetailsHandler
        implements CommandHandler<UpdateUserContactDetailsCommand, Void> {

    private final UserRepository userRepository;

    @Override
    public Void handle(final UpdateUserContactDetailsCommand command) {
        User user =
                userRepository
                        .findByEmail(command.email())
                        .orElseThrow(() -> new UserNotFoundException(command.email()));

        user.verifyUser();
        user =
                user.updateContactDetails(
                        new ContactDetails(
                                new Email(command.newNotificationEmail()),
                                new PhoneNumber(command.newNotificationPhoneNumber())));

        userRepository.save(user);
        return null;
    }

    @Override
    public Class<UpdateUserContactDetailsCommand> getCommandType() {
        return UpdateUserContactDetailsCommand.class;
    }
}
