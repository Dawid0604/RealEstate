/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.photo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PhotoJpaRepository extends JpaRepository<PhotoEntity, UUID> {}
