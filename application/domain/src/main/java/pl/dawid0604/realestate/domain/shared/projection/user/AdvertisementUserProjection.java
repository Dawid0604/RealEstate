/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.projection.user;

import java.util.UUID;

public interface AdvertisementUserProjection {
    UUID getId();

    String getFirstName();

    String getUserAvatarUrl();

    String getLastName();

    String getType();

    String getContactPhoneNumber();

    String getContactEmail();
}
