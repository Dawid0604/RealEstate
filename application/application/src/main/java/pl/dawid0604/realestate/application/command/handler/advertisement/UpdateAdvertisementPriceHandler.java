package pl.dawid0604.realestate.application.command.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateAdvertisementPriceCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.Money;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateAdvertisementPriceHandler
        implements CommandHandler<UpdateAdvertisementPriceCommand, Void> {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Void handle(final UpdateAdvertisementPriceCommand command) {
        final User user =
                userRepository
                        .findById(command.userId())
                        .orElseThrow(() -> new UserNotFoundException(command.userId()));

        user.verifyUser();
        Advertisement advertisement =
                advertisementRepository
                        .findBySlug(command.slug())
                        .orElseThrow(() -> new AdvertisementNotFoundException(command.slug()));

        advertisement.verifyOwner(user);
        advertisement =
                advertisement.updatePrice(
                        new Money(command.newPrice(), advertisement.getPrice().currency()));

        advertisementRepository.save(advertisement);
        advertisement.getEvents().forEach(eventPublisher::publishEvent);
        return null;
    }

    @Override
    public Class<UpdateAdvertisementPriceCommand> getCommandType() {
        return UpdateAdvertisementPriceCommand.class;
    }
}
