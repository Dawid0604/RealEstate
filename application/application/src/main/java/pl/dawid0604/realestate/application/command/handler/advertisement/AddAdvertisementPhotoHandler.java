/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.AddAdvertisementPhotoCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementPhoto;
import pl.dawid0604.realestate.domain.Url;
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
class AddAdvertisementPhotoHandler implements CommandHandler<AddAdvertisementPhotoCommand, Void> {
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    @Override
    public Void handle(final AddAdvertisementPhotoCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        log.info(
                "Adding advertisement photo: slug={}, type={}, user={}",
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
        advertisement =
                advertisement.addPhoto(
                        AdvertisementPhoto.create(new Url(command.photoUrl()), command.position()));

        advertisementRepository.save(advertisement);
        log.info("Advertisement photo added");
        return null;
    }

    private static Supplier<AdvertisementNotFoundException> throwAdvertisementNotFoundException(
            final AddAdvertisementPhotoCommand command) {

        return () -> {
            log.warn("Advertisement not found: slug={}", command.slug());
            return new AdvertisementNotFoundException(command.slug());
        };
    }

    private static Supplier<UserNotFoundException> throwUserNotFoundException(
            final AddAdvertisementPhotoCommand command) {

        return () -> {
            log.warn("User not found: email={}", command.userEmail());
            return new UserNotFoundException(command.userEmail());
        };
    }

    @Override
    public Class<AddAdvertisementPhotoCommand> getCommandType() {
        return AddAdvertisementPhotoCommand.class;
    }
}
