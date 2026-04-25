/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.shared.user.projection.AdvertisementUserProjection;
import pl.dawid0604.realestate.domain.shared.user.projection.UserProfileProjection;

public interface UserRepository {
    Optional<UserProfileProjection> findUserProfile(String email);

    Optional<AdvertisementUserProjection> findAdvertisementUser(String email);

    Optional<User> findByEmail(String email);

    Map<UUID, UserType> getUserTypesInBatch(Iterable<UUID> userIds);

    void save(User user);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    boolean hasStatus(String email, UserStatus userStatus);
}
