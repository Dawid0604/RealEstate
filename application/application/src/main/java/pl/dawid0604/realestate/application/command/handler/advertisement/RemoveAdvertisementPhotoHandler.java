/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.RemoveAdvertisementPhotoCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class RemoveAdvertisementPhotoHandler
        implements CommandHandler<RemoveAdvertisementPhotoCommand, Void> {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    @Override
    public Void handle(final RemoveAdvertisementPhotoCommand command) {
        final User user =
                userRepository
                        .findByEmail(command.userEmail())
                        .orElseThrow(() -> new UserNotFoundException(command.userEmail()));

        user.verifyUser();
        Advertisement advertisement =
                advertisementRepository
                        .findBySlug(
                                command.slug(), AdvertisementType.of(command.advertisementType()))
                        .orElseThrow(() -> new AdvertisementNotFoundException(command.slug()));

        advertisement.verifyOwner(user);
        advertisement = advertisement.removePhoto(Identifier.of(command.photoId()));

        advertisementRepository.save(advertisement);
        return null;
    }

    @Override
    public Class<RemoveAdvertisementPhotoCommand> getCommandType() {
        return RemoveAdvertisementPhotoCommand.class;
    }
}
