/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidPassword;

public record UpdateUserPasswordCommand(
        @ValidEmail String email,
        @ValidPassword String currentPassword,
        @ValidPassword String newPassword)
        implements Command {}
