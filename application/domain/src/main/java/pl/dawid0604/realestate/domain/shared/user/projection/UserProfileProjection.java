/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.user.projection;

import java.util.UUID;

public interface UserProfileProjection {
    UUID getUserId();

    String getEmail();

    String getFirstName();

    String getLastName();

    String getContactPhoneNumber();

    String getContactEmail();

    String getAvatarUrl();

    String getRole();

    String getType();

    String getStatus();
}
