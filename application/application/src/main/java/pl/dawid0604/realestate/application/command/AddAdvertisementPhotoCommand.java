/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidPhotoPosition;
import pl.dawid0604.realestate.application.validation.ValidSlug;
import pl.dawid0604.realestate.application.validation.ValidUrl;

public record AddAdvertisementPhotoCommand(
        @ValidSlug String slug,
        @ValidUrl String photoUrl,
        @ValidPhotoPosition int position,
        @ValidEmail String userEmail)
        implements Command {}
