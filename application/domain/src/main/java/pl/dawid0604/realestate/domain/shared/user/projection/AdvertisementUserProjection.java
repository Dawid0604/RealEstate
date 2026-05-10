/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.user.projection;

import java.util.UUID;

import pl.dawid0604.realestate.domain.UserType;

public interface AdvertisementUserProjection {
    UUID getId();

    String getFirstName();

    String getAvatarUrl();

    String getLastName();

    UserType getType();

    String getNotificationPhoneNumber();

    String getNotificationEmail();
}
