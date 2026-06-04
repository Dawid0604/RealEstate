/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.ActivateAdvertisementCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor(access = PACKAGE)
class ActivateAdvertisementHandler implements CommandHandler<ActivateAdvertisementCommand, Void> {
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Void handle(final ActivateAdvertisementCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        log.info(
                "Activating advertisement: slug={}, type={}, user={}",
                command.slug(),
                command.advertisementType(),
                command.userEmail());

        final User user =
                userRepository
                        .findByEmail(command.userEmail())
                        .orElseThrow(throwUserNotFoundException(command));

        user.verifyUser();
        Advertisement advertisement =
                advertisementRepository
                        .findBySlug(command.slug(), command.advertisementType())
                        .orElseThrow(throwAdvertisementNotFoundException(command));

        advertisement.verifyOwner(user);
        advertisement = advertisement.activate();

        advertisementRepository.save(advertisement);
        advertisement.getEvents().forEach(eventPublisher::publishEvent);

        log.info("Advertisement activated");
        return null;
    }

    private static Supplier<AdvertisementNotFoundException> throwAdvertisementNotFoundException(
            final ActivateAdvertisementCommand command) {

        return () -> {
            log.warn("Advertisement not found: slug={}", command.slug());
            return new AdvertisementNotFoundException(command.slug());
        };
    }

    private static Supplier<UserNotFoundException> throwUserNotFoundException(
            final ActivateAdvertisementCommand command) {

        return () -> {
            log.warn("User not found: email={}", command.userEmail());
            return new UserNotFoundException(command.userEmail());
        };
    }

    @Override
    public Class<ActivateAdvertisementCommand> getCommandType() {
        return ActivateAdvertisementCommand.class;
    }
}
