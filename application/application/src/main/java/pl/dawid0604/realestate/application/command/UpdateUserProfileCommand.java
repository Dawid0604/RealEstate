/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.domain.UserType;

public record UpdateUserProfileCommand(
        String email,
        String avatarUrl,
        String notificationEmail,
        String notificationPhoneNumber,
        String firstName,
        String lastName,
        UserType type)
        implements Command {}
