/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.domain.UserType;

public record RegisterUserCommand(
        String username,
        String password,
        String firstName,
        String lastName,
        UserType type,
        String notificationEmail,
        String notificationPhoneNumber)
        implements Command {}
