package pl.dawid0604.realestate.domain.shared.event;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class UserRegisteredEvent extends DomainEvent {
    private final Identifier userId;

    public UserRegisteredEvent(final Identifier userId) {
        if (userId == null) {
            throw new InvalidArgumentValueException("AdvertisementId cannot be null");
        }

        this.userId = userId;
    }

    public Identifier getUserId() {
        return userId;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof final UserRegisteredEvent that && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
