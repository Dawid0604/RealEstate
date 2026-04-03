package pl.dawid0604.realestate.application.command.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateAdvertisementLocalityCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.Locality;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.LocalityNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateAdvertisementLocalityHandler
        implements CommandHandler<UpdateAdvertisementLocalityCommand, Void> {

    private final AdvertisementRepository advertisementRepository;
    private final LocalityRepository localityRepository;
    private final UserRepository userRepository;

    @Override
    public Void handle(final UpdateAdvertisementLocalityCommand command) {
        if (!localityRepository.existsById(command.newLocalityId())) {
            throw new LocalityNotFoundException(command.newLocalityId());
        }

        final User user =
                userRepository
                        .findByEmail(command.userEmail())
                        .orElseThrow(() -> new UserNotFoundException(command.userEmail()));

        user.verifyUser();
        Advertisement advertisement =
                advertisementRepository
                        .findBySlug(command.slug())
                        .orElseThrow(() -> new AdvertisementNotFoundException(command.slug()));

        advertisement.verifyOwner(user);
        advertisement =
                advertisement.updateLocality(new Locality(Identifier.of(command.newLocalityId())));

        advertisementRepository.save(advertisement);
        return null;
    }

    @Override
    public Class<UpdateAdvertisementLocalityCommand> getCommandType() {
        return UpdateAdvertisementLocalityCommand.class;
    }
}
