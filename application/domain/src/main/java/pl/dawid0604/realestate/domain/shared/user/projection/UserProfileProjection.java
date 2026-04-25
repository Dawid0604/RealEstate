/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.user.projection;

import java.util.UUID;

import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.UserType;

public interface UserProfileProjection {
    UUID getId();

    String getEmail();

    String getFirstName();

    String getLastName();

    String getNotificationPhoneNumber();

    String getNotificationEmail();

    String getAvatarUrl();

    UserRole getRole();

    UserType getType();

    UserStatus getStatus();
}
