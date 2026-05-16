/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.DeactivateAdvertisementCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class DeactivateAdvertisementHandler
        implements CommandHandler<DeactivateAdvertisementCommand, Void> {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Void handle(final DeactivateAdvertisementCommand command) {
        final User user =
                userRepository
                        .findByEmail(command.userEmail())
                        .orElseThrow(() -> new UserNotFoundException(command.userEmail()));

        user.verifyUser();
        Advertisement advertisement =
                advertisementRepository
                        .findBySlug(command.slug(), command.advertisementType())
                        .orElseThrow(() -> new AdvertisementNotFoundException(command.slug()));

        advertisement = advertisement.deactivate();
        advertisementRepository.save(advertisement);
        advertisement.getEvents().forEach(eventPublisher::publishEvent);
        return null;
    }

    @Override
    public Class<DeactivateAdvertisementCommand> getCommandType() {
        return DeactivateAdvertisementCommand.class;
    }
}
