/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.shared.user.projection.AdvertisementUserProjection;
import pl.dawid0604.realestate.domain.shared.user.projection.UserProfileProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    interface UserTypeProjection {
        UUID getId();

        UserType getType();
    }

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

    @Query("SELECT u.id, u.type FROM #{#entityName} u WHERE u.id IN :userIds")
    List<UserTypeProjection> findUserTypesByIdIn(Iterable<UUID> userIds);
}
