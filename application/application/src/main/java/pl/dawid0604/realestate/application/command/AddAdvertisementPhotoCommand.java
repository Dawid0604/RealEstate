package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidPhoto;
import pl.dawid0604.realestate.application.validation.ValidPhotoPosition;
import pl.dawid0604.realestate.application.validation.ValidSlug;
import pl.dawid0604.realestate.application.validation.ValidUserId;

import java.util.UUID;

public record AddAdvertisementPhotoCommand(
        @ValidSlug String slug,
        @ValidPhoto String photoUrl,
        @ValidPhotoPosition int position,
        @ValidUserId UUID userId)
        implements Command {}
