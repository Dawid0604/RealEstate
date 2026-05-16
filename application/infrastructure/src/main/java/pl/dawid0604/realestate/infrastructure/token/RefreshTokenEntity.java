package pl.dawid0604.realestate.infrastructure.token;

import static lombok.AccessLevel.PACKAGE;

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
@Table(name = "refresh_tokens")
@NoArgsConstructor(access = PACKAGE)
@SuppressWarnings("PMD.ImmutableField")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class RefreshTokenEntity {
    @Id @EqualsAndHashCode.Include private UUID id;

    private UUID userId;
    private String hashedToken;
    private Instant expiresAt;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate private Instant updatedAt;

    RefreshTokenEntity(
            final UUID id,
            final UUID userId,
            final String hashedToken,
            final Instant expiresAt) {

        this.id = id;
        this.userId = userId;
        this.hashedToken = hashedToken;
        this.expiresAt = expiresAt;
    }
}
