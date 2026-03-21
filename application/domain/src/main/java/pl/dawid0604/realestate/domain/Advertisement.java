/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static java.util.stream.Collectors.toCollection;

import pl.dawid0604.realestate.domain.shared.event.AdvertisementPriceChangedEvent;
import pl.dawid0604.realestate.domain.shared.event.AdvertisementStatusChangedEvent;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;
import pl.dawid0604.realestate.domain.shared.exception.MaxPhotosExceededException;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Advertisement<B extends AdvertisementDetails<?>> extends AggregateRoot {
    private final Identifier id;
    private final Slug slug;
    private final Title title;
    private final Description description;
    private final Money price;
    private final Locality locality;
    private final B details;
    private final AdvertisementStatus status;
    private final Identifier userId;
    private final Set<AdvertisementPhoto> photos;
    private final Instant createdAt;
    private static final int MAX_NUMBER_OF_PHOTOS = 20;

    public Advertisement<B> addPhoto(final AdvertisementPhoto advertisementPhoto) {
        if (advertisementPhoto == null) {
            throw new InvalidArgumentValueException("AdvertisementPhoto cannot be null");
        }

        if (this.photos.contains(advertisementPhoto)) {
            throw new InvalidArgumentValueException("Photo already exists");
        }

        if (this.photos.size() + 1 > MAX_NUMBER_OF_PHOTOS) {
            throw new MaxPhotosExceededException(MAX_NUMBER_OF_PHOTOS);
        }

        return this.copy().photos(getFixedPhotos(advertisementPhoto)).build();
    }

    public Advertisement<B> updateDetails(final B newDetails) {
        if (newDetails == null) {
            throw new InvalidArgumentValueException("Details cannot be null");
        }

        return this.copy().details(newDetails).build();
    }

    public Advertisement<B> updateTitle(final Title newTitle) {
        if (newTitle == null) {
            throw new InvalidArgumentValueException("Title cannot be null");
        }

        if (Objects.equals(this.title, newTitle)) {
            throw new InvalidArgumentValueException(
                    "Incoming title cannot be the same as old title");
        }

        return copy().title(newTitle).slug(Slug.create(newTitle)).build();
    }

    public Advertisement<B> updateDescription(final Description newDescription) {
        return copy().description(newDescription).build();
    }

    public Advertisement<B> updatePrice(final Money newPrice) {
        if (newPrice == null) {
            throw new InvalidArgumentValueException("Price cannot be null");
        }

        if (Objects.equals(this.price, newPrice)) {
            throw new InvalidArgumentValueException("Price cannot be the same as old price");
        }

        final Advertisement<B> currentObj = this.copy().price(newPrice).build();
        currentObj.addEvent(
                new AdvertisementPriceChangedEvent(currentObj.id, this.price, newPrice));

        return currentObj;
    }

    public Advertisement<B> activate() {
        if (this.status == AdvertisementStatus.SOLD) {
            throw new InvalidArgumentValueException("Advertisement is already sold");
        }

        if (this.status == AdvertisementStatus.INACTIVE) {
            final Advertisement<B> currentObj =
                    this.copy().status(AdvertisementStatus.ACTIVE).build();

            currentObj.addEvent(
                    new AdvertisementStatusChangedEvent(
                            currentObj.id, this.status, currentObj.status));

            return currentObj;
        }

        throw new InvalidArgumentValueException("Advertisement is already active");
    }

    public Advertisement<B> inactivate() {
        if (this.status == AdvertisementStatus.SOLD) {
            throw new InvalidArgumentValueException("Advertisement is already sold");
        }

        if (this.status == AdvertisementStatus.ACTIVE) {
            final Advertisement<B> currentObj =
                    this.copy().status(AdvertisementStatus.INACTIVE).build();

            currentObj.addEvent(
                    new AdvertisementStatusChangedEvent(
                            currentObj.id, this.status, currentObj.status));

            return currentObj;
        }

        throw new InvalidArgumentValueException("Advertisement is already inactive");
    }

    public Advertisement<B> setAsSold() {
        if (this.status == AdvertisementStatus.INACTIVE) {
            throw new InvalidArgumentValueException("Advertisement must be active");
        }

        if (this.status == AdvertisementStatus.ACTIVE) {
            final Advertisement<B> currentObj =
                    this.copy().status(AdvertisementStatus.SOLD).build();

            currentObj.addEvent(
                    new AdvertisementStatusChangedEvent(
                            currentObj.id, this.status, currentObj.status));

            return currentObj;
        }

        throw new InvalidArgumentValueException("Advertisement is already sold");
    }

    public Identifier getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Slug getSlug() {
        return slug;
    }

    public Set<AdvertisementPhoto> getPhotos() {
        return Set.copyOf(photos);
    }

    public B getDetails() {
        return details;
    }

    public Title getTitle() {
        return title;
    }

    public Description getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public AdvertisementStatus getStatus() {
        return status;
    }

    private Advertisement(
            final Identifier id,
            final Slug slug,
            final Title title,
            final Description description,
            final Money price,
            final Locality locality,
            final B details,
            final AdvertisementStatus status,
            final Identifier userId,
            final Set<AdvertisementPhoto> photos,
            final Instant createdAt) {

        this.id = id;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.price = price;
        this.locality = locality;
        this.details = details;
        this.status = status;
        this.userId = userId;
        this.photos = photos;
        this.createdAt = createdAt;
    }

    public static <B extends AdvertisementDetails<?>> Builder<B> create() {
        return new Builder<>(true);
    }

    public static <B extends AdvertisementDetails<?>> Builder<B> reconstitute() {
        return new Builder<>(false);
    }

    private Builder<B> copy() {
        return new Builder<B>(false)
                .id(this.id)
                .slug(this.slug)
                .title(this.title)
                .description(this.description)
                .price(this.price)
                .locality(this.locality)
                .details(this.details)
                .status(this.status)
                .userId(this.userId)
                .photos(this.photos)
                .createdAt(this.createdAt);
    }

    public static final class Builder<B extends AdvertisementDetails<?>> {
        private Identifier id;
        private Slug slug;
        private Title title;
        private Description description;
        private Money price;
        private Locality locality;
        private B details;
        private AdvertisementStatus status;
        private Identifier userId;
        private Set<AdvertisementPhoto> photos;
        private Instant createdAt;
        private final boolean createMode;

        private Builder(final boolean createMode) {
            this.createMode = createMode;
        }

        private void requireNonNull(final Object field, final String name) {
            if (field == null) {
                throw new InvalidArgumentValueException(name + " cannot be null");
            }
        }

        public Advertisement<B> build() {
            requireNonNull(this.title, "Title");
            requireNonNull(this.description, "Description");
            requireNonNull(this.price, "Price");
            requireNonNull(this.locality, "Locality");
            requireNonNull(this.details, "Details");
            requireNonNull(this.status, "Status");
            requireNonNull(this.userId, "UserId");

            if (createMode) {
                this.id = Identifier.generate();
                this.createdAt = Instant.now();
                this.slug = Slug.create(title);

            } else {
                requireNonNull(this.id, "Id");
                requireNonNull(this.createdAt, "CreatedAt");
                requireNonNull(this.slug, "Slug");
            }

            if (createdAt.isAfter(Instant.now())) {
                throw new InvalidArgumentValueException("CreatedAt cannot be from the future");
            }

            if (photos == null) {
                this.photos = new HashSet<>();
            }

            if (this.photos.size() > MAX_NUMBER_OF_PHOTOS) {
                throw new MaxPhotosExceededException(MAX_NUMBER_OF_PHOTOS);
            }

            return new Advertisement<>(
                    id,
                    slug,
                    title,
                    description,
                    price,
                    locality,
                    details,
                    status,
                    userId,
                    photos,
                    createdAt);
        }

        public Builder<B> slug(final Slug slug) {
            this.slug = slug;
            return this;
        }

        public Builder<B> title(final Title title) {
            this.title = title;
            return this;
        }

        public Builder<B> description(final Description description) {
            this.description = description;
            return this;
        }

        public Builder<B> price(final Money price) {
            this.price = price;
            return this;
        }

        public Builder<B> locality(final Locality locality) {
            this.locality = locality;
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T extends AdvertisementDetails<?>> Builder<T> details(final T details) {
            this.details = (B) details;
            return (Builder<T>) this;
        }

        public Builder<B> status(final AdvertisementStatus status) {
            this.status = status;
            return this;
        }

        public Builder<B> userId(final Identifier userId) {
            this.userId = userId;
            return this;
        }

        public Builder<B> id(final Identifier id) {
            this.id = id;
            return this;
        }

        public Builder<B> photos(final Set<AdvertisementPhoto> photos) {
            this.photos = photos == null ? new HashSet<>() : new HashSet<>(photos);
            return this;
        }

        public Builder<B> createdAt(final Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
    }

    private Set<AdvertisementPhoto> getFixedPhotos(final AdvertisementPhoto incomingPhoto) {
        final Set<AdvertisementPhoto> fixedPhotos =
                this.photos.stream()
                        .map(
                                existing ->
                                        existing.getPosition() >= incomingPhoto.getPosition()
                                                ? AdvertisementPhoto.of(
                                                        existing.getId(),
                                                        existing.getUrl(),
                                                        existing.getPosition() + 1)
                                                : existing)
                        .collect(toCollection(HashSet::new));

        fixedPhotos.add(incomingPhoto);
        return fixedPhotos;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof final Advertisement<?> that && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
