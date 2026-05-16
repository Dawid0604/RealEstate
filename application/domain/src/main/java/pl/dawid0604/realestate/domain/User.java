/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.event.UserRegisteredEvent;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;
import pl.dawid0604.realestate.domain.shared.exception.UnauthorizedAccessException;
import pl.dawid0604.realestate.domain.shared.exception.UserAlreadyActiveException;
import pl.dawid0604.realestate.domain.shared.exception.UserBannedException;
import pl.dawid0604.realestate.domain.shared.exception.UserCannotBeActivatedException;
import pl.dawid0604.realestate.domain.shared.exception.UserCannotBeUnbannedException;

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
    private final UserType type;
    private final UserStatus status;
    private final Instant createdAt;
    private final Instant lastLoginAt;
    private final boolean createMode;

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
            final Instant lastLoginAt,
            final boolean createMode,
            final UserType type) {

        this.id = id;
        this.type = type;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.contactDetails = contactDetails;
        this.avatar = avatar;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.createMode = createMode;
    }

    public void verifyUser() {
        if (this.status != UserStatus.ACTIVE) {
            throw new UnauthorizedAccessException(
                    "User account has no permissions to perform this action");
        }
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

    public Identifier getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public User ban() {
        if (this.status == UserStatus.BANNED) {
            throw new UserBannedException();
        }

        return copy().status(UserStatus.BANNED).build();
    }

    public User unban() {
        if (this.status != UserStatus.BANNED) {
            throw new UserCannotBeUnbannedException();
        }

        return copy().status(UserStatus.ACTIVE).build();
    }

    public User activate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new UserAlreadyActiveException();
        }

        if (this.status == UserStatus.INACTIVE) {
            return copy().status(UserStatus.ACTIVE).build();
        }

        throw new UserCannotBeActivatedException();
    }

    public User updatePassword(final Password password) {
        return copy().password(password).build();
    }

    public User updateContactDetails(final ContactDetails contactDetails) {
        return copy().contactDetails(contactDetails).build();
    }

    public User updateType(final UserType newType) {
        return copy().type(newType).build();
    }

    public User register() {
        if (!createMode) {
            throw new UnauthorizedAccessException("User is already registered");
        }

        final User currentObj = copy().build();
        currentObj.addEvent(new UserRegisteredEvent(id));

        return currentObj;
    }

    public User updateFullName(final FullName fullName) {
        return copy().fullName(fullName).build();
    }

    public boolean isAdmin() {
        return this.role == UserRole.ROLE_ADMIN;
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

    public Url getAvatar() {
        return avatar;
    }

    public Password getPassword() {
        return password;
    }

    public FullName getFullName() {
        return fullName;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public Email getEmail() {
        return email;
    }

    public UserType getType() {
        return type;
    }

    public UserRole getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
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
                .type(this.type)
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
        private UserType type;
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
            requireNonNull(this.type, "Type");

            if (createMode) {
                this.id = Identifier.generate();
                this.createdAt = Instant.now();
                this.status = UserStatus.INACTIVE;

            } else {
                requireNonNull(this.id, "Id");
                requireNonNull(this.createdAt, "CreatedAt");
                requireNonNull(this.status, "Status");
            }

            if (createdAt.isAfter(Instant.now())) {
                throwDateFromTheFutureException("CreatedAt");
            }

            if (lastLoginAt != null && lastLoginAt.isAfter(Instant.now())) {
                throwDateFromTheFutureException("LastLoginAt");
            }

            return new User(
                    this.id,
                    this.email,
                    this.password,
                    this.fullName,
                    this.contactDetails,
                    this.avatar,
                    this.role,
                    this.status,
                    this.createdAt,
                    this.lastLoginAt,
                    this.createMode,
                    this.type);
        }

        private static void throwDateFromTheFutureException(final String fieldName) {
            throw new InvalidArgumentValueException(fieldName + " cannot be from the future");
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

        public Builder type(final UserType type) {
            this.type = type;
            return this;
        }

        public Builder createdAt(final Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastLoginAt(final Instant lastLoginAt) {
            if (!createMode) {
                this.lastLoginAt = lastLoginAt;
            }

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
