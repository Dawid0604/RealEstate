package pl.dawid0604.realestate.application.command.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.DisableFeaturedStateAdvertisementCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class DisableFeaturedStateAdvertisementHandler
        implements CommandHandler<DisableFeaturedStateAdvertisementCommand, Void> {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    @Override
    public Void handle(final DisableFeaturedStateAdvertisementCommand command) {
        final User user =
                userRepository
                        .findById(command.userId())
                        .orElseThrow(() -> new UserNotFoundException(command.userId()));

        user.verifyUser();
        Advertisement advertisement =
                advertisementRepository
                        .findBySlug(command.slug())
                        .orElseThrow(() -> new AdvertisementNotFoundException(command.slug()));

        advertisement = advertisement.disableFeaturedState();
        advertisementRepository.save(advertisement);
        return null;
    }

    @Override
    public Class<DisableFeaturedStateAdvertisementCommand> getCommandType() {
        return DisableFeaturedStateAdvertisementCommand.class;
    }
}
