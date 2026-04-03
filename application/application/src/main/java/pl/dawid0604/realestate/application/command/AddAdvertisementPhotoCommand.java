package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidPhoto;
import pl.dawid0604.realestate.application.validation.ValidPhotoPosition;
import pl.dawid0604.realestate.application.validation.ValidSlug;

public record AddAdvertisementPhotoCommand(
        @ValidSlug String slug,
        @ValidPhoto String photoUrl,
        @ValidPhotoPosition int position,
        @ValidEmail String userEmail)
        implements Command {}
