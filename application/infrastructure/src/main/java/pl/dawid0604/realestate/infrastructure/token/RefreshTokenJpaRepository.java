/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.token;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM #{#entityName} u WHERE u.userId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);

    Optional<RefreshTokenEntity> findByUserId(UUID userId);
}
