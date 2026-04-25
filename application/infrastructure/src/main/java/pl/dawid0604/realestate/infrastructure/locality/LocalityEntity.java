/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "localities")
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@SuppressWarnings("PMD.ImmutableField")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
class LocalityEntity {

    @Id @EqualsAndHashCode.Include private UUID id;

    private String name;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(updatable = false)
    private Instant updatedAt;
}
