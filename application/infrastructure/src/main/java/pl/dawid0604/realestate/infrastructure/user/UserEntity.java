/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.user;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PACKAGE;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.UserType;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = PACKAGE)
@SuppressWarnings("PMD.ImmutableField")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class UserEntity {

    @Id @EqualsAndHashCode.Include private UUID id;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String notificationEmail;
    private String notificationPhoneNumber;
    private String avatarUrl;

    @Enumerated(STRING)
    private UserRole role;

    @Enumerated(STRING)
    private UserStatus status;

    @Enumerated(STRING)
    private UserType type;

    private Instant lastLoginAt;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate private Instant updatedAt;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    UserEntity(
            final UUID id,
            final String email,
            final String password,
            final String firstName,
            final String lastName,
            final String notificationEmail,
            final String notificationPhoneNumber,
            final String avatarUrl,
            final UserRole role,
            final UserStatus status,
            final UserType type,
            final Instant lastLoginAt) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.notificationEmail = notificationEmail;
        this.notificationPhoneNumber = notificationPhoneNumber;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.status = status;
        this.type = type;
        this.lastLoginAt = lastLoginAt;
    }
}
