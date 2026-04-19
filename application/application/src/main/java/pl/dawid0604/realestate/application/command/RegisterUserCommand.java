/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidFirstName;
import pl.dawid0604.realestate.application.validation.ValidLastName;
import pl.dawid0604.realestate.application.validation.ValidPassword;
import pl.dawid0604.realestate.application.validation.ValidUserType;

public record RegisterUserCommand(
        @ValidEmail String email,
        @ValidPassword String password,
        @ValidFirstName String firstName,
        @ValidLastName String lastName,
        @ValidUserType String type,
        String notificationEmail,
        String phoneNumber)
        implements Command {}
