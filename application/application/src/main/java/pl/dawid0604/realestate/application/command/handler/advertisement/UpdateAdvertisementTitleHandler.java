package pl.dawid0604.realestate.application.command.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.UpdateAdvertisementTitleCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.Title;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UpdateAdvertisementTitleHandler
        implements CommandHandler<UpdateAdvertisementTitleCommand, Void> {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    @Override
    public Void handle(final UpdateAdvertisementTitleCommand command) {
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
        advertisement = advertisement.updateTitle(new Title(command.newTitle()));

        advertisementRepository.save(advertisement);
        return null;
    }

    @Override
    public Class<UpdateAdvertisementTitleCommand> getCommandType() {
        return UpdateAdvertisementTitleCommand.class;
    }
}
