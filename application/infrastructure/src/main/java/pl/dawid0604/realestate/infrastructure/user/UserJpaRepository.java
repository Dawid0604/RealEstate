/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.shared.user.projection.AdvertisementUserProjection;
import pl.dawid0604.realestate.domain.shared.user.projection.UserProfileProjection;

@Repository
interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);

    @Query(
            """
                    SELECT
                        CASE WHEN COUNT(u) > 0 THEN true ELSE false END
                    FROM #{#entityName} u
                    WHERE u.email = :email AND u.status = :status
                    """)
    boolean hasStatus(@Param("email") String email, @Param("status") UserStatus userStatus);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserProfileProjection> findUserProfileByEmail(String email);

    Optional<AdvertisementUserProjection> findAdvertisementUserByEmail(String email);

    int deleteByEmail(String email);
}
