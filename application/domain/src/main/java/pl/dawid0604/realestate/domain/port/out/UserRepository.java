/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import java.util.Optional;

import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.shared.projection.user.UserProfileProjection;

public interface UserRepository {
    Optional<UserProfileProjection> findUserProfile(String email);

    Optional<User> findByEmail(String email);

    void save(User user);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
