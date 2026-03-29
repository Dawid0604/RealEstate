/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.shared.projection.user.UserProfileProjection;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<UserProfileProjection> findUserProfile(UUID userId);

    Optional<User> findById(UUID userId);
}
