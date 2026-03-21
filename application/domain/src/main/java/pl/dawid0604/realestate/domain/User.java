/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.ForbiddenException;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public final class User extends AggregateRoot {
    private final Identifier id;
    private final Email email;
    private final Password password;
    private final FullName fullName;
    private final ContactDetails contactDetails;
    private final Url avatar;
    private final UserRole role;
    private final UserStatus status;
    private final Instant createdAt;
    private final Instant lastLoginAt;

    private User(
            final Identifier id,
            final Email email,
            final Password password,
            final FullName fullName,
            final ContactDetails contactDetails,
            final Url avatar,
            final UserRole role,
            final UserStatus status,
            final Instant createdAt,
            final Instant lastLoginAt) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.contactDetails = contactDetails;
        this.avatar = avatar;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }

    public boolean canLogin() {
        return this.status == UserStatus.ACTIVE;
    }

    public boolean isBanned() {
        return this.status == UserStatus.BANNED;
    }

    public boolean isInactive() {
        return this.status == UserStatus.INACTIVE;
    }

    public User ban() {
        if (this.role != UserRole.ADMIN_ROLE) {
            throw new ForbiddenException("Only admins can ban users");
        }

        if (this.status == UserStatus.BANNED) {
            throw new InvalidArgumentValueException("User is already banned");
        }

        return copy().status(UserStatus.BANNED).build();
    }

    public User activate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new InvalidArgumentValueException("User is already active");
        }

        return copy().status(UserStatus.ACTIVE).build();
    }

    public User updatePassword(final Password password) {
        requireNonNull(password, "Password");
        return copy().password(password).build();
    }

    public User updateEmail(final Email email) {
        requireNonNull(email, "Email");
        return copy().email(email).build();
    }

    public User updateContactDetails(final ContactDetails contactDetails) {
        requireNonNull(contactDetails, "ContactDetails");
        return copy().contactDetails(contactDetails).build();
    }

    public User updateFullName(final FullName fullName) {
        requireNonNull(fullName, "FullName");
        return copy().fullName(fullName).build();
    }

    public User updateAvatar(final Url avatar) {
        return copy().avatar(avatar).build();
    }

    public User handleLogin() {
        return copy().lastLoginAt(Instant.now()).build();
    }

    public Optional<Instant> getLastLoginAt() {
        return Optional.ofNullable(lastLoginAt);
    }

    public Optional<Url> getAvatar() {
        return Optional.ofNullable(avatar);
    }

    public static Builder create() {
        return new Builder(true);
    }

    public static Builder reconstitute() {
        return new Builder(false);
    }

    private Builder copy() {
        return reconstitute()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .fullName(this.fullName)
                .contactDetails(this.contactDetails)
                .avatar(this.avatar)
                .role(this.role)
                .status(this.status)
                .createdAt(this.createdAt)
                .lastLoginAt(this.lastLoginAt);
    }

    public static final class Builder {
        private Identifier id;
        private Email email;
        private Password password;
        private FullName fullName;
        private ContactDetails contactDetails;
        private Url avatar;
        private UserRole role;
        private UserStatus status;
        private Instant createdAt;
        private Instant lastLoginAt;
        private final boolean createMode;

        private Builder(final boolean createMode) {
            this.createMode = createMode;
        }

        public User build() {
            requireNonNull(this.email, "Email");
            requireNonNull(this.password, "Password");
            requireNonNull(this.fullName, "FullName");
            requireNonNull(this.role, "Role");
            requireNonNull(this.contactDetails, "ContactDetails");
            requireNonNull(this.status, "Status");

            if (createMode) {
                this.id = Identifier.generate();
                this.createdAt = Instant.now();

            } else {
                requireNonNull(this.id, "Id");
                requireNonNull(this.createdAt, "CreatedAt");
            }

            return new User(
                    id,
                    email,
                    password,
                    fullName,
                    contactDetails,
                    avatar,
                    role,
                    status,
                    createdAt,
                    lastLoginAt);
        }

        public Builder id(final Identifier id) {
            this.id = id;
            return this;
        }

        public Builder email(final Email email) {
            this.email = email;
            return this;
        }

        public Builder password(final Password password) {
            this.password = password;
            return this;
        }

        public Builder fullName(final FullName fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder contactDetails(final ContactDetails contactDetails) {
            this.contactDetails = contactDetails;
            return this;
        }

        public Builder avatar(final Url avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder role(final UserRole role) {
            this.role = role;
            return this;
        }

        public Builder status(final UserStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(final Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastLoginAt(final Instant lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof final User other && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
