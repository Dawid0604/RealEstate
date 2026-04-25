/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.user.projection;

import pl.dawid0604.realestate.domain.UserType;

import java.util.UUID;

public interface AdvertisementUserProjection {
    UUID getId();

    String getFirstName();

    String getAvatarUrl();

    String getLastName();

    UserType getType();

    String getNotificationPhoneNumber();

    String getNotificationEmail();
}
