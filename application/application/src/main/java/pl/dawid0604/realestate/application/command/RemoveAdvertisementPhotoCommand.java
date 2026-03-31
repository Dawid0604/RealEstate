package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidPhotoId;
import pl.dawid0604.realestate.application.validation.ValidSlug;
import pl.dawid0604.realestate.application.validation.ValidUserId;

import java.util.UUID;

public record RemoveAdvertisementPhotoCommand(
        @ValidSlug String slug,
        @ValidPhotoId UUID photoId,
        @ValidUserId UUID userId)
        implements Command {}
