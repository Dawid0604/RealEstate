/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidUserAvatar;

public record UpdateUserAvatarCommand(
        @ValidEmail String email, @ValidUserAvatar String newAvatarUrl) implements Command {}
