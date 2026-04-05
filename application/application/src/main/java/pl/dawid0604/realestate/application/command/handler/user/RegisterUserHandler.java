/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.RegisterUserCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.ContactDetails;
import pl.dawid0604.realestate.domain.Email;
import pl.dawid0604.realestate.domain.FullName;
import pl.dawid0604.realestate.domain.Password;
import pl.dawid0604.realestate.domain.PhoneNumber;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.port.out.PasswordEncoder;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserExistsException;

import java.util.UUID;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class RegisterUserHandler implements CommandHandler<RegisterUserCommand, UUID> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UUID handle(final RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new UserExistsException(command.email());
        }

        User user =
                User.create()
                        .email(new Email(command.email()))
                        .password(Password.ofHashed(passwordEncoder.encode(command.password())))
                        .fullName(new FullName(command.firstName(), command.lastName()))
                        .role(UserRole.USER_ROLE)
                        .contactDetails(
                                new ContactDetails(
                                        new Email(command.notificationEmail()),
                                        new PhoneNumber(command.phoneNumber())))
                        .build();

        user = user.register();
        userRepository.save(user);

        user.getEvents().forEach(eventPublisher::publishEvent);
        return user.getId().getValue();
    }

    @Override
    public Class<RegisterUserCommand> getCommandType() {
        return RegisterUserCommand.class;
    }
}
