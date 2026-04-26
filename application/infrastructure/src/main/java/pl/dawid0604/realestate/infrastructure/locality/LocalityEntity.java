/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "localities")
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("PMD.ImmutableField")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class LocalityEntity {

    @Id @EqualsAndHashCode.Include private UUID id;

    private String name;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate private Instant updatedAt;

    LocalityEntity(final UUID id, final String name) {
        this.id = id;
        this.name = name;
    }
}
