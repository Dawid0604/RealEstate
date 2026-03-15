/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.event.AdvertisementPriceChangedEvent;
import pl.dawid0604.realestate.domain.shared.event.AdvertisementStatusChangedEvent;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Advertisement extends AggregateRoot {
    private final Identifier id;
    private final Slug slug;
    private final Title title;
    private final Description description;
    private final Money price;
    private final Locality locality;
    private final AdvertisementDetails details;
    private final AdvertisementStatus status;
    private final Identifier userId;
    private final Set<AdvertisementPhoto> photos;
    private final Instant createdAt;

    public Advertisement addPhoto(final AdvertisementPhoto advertisementPhoto) {
        if (advertisementPhoto == null) {
            throw new InvalidArgumentValueException("AdvertisementPhoto cannot be null");
        }

        if (photoExists(advertisementPhoto)) {
            throw new InvalidArgumentValueException("Photo already exist");
        }

        final Set<AdvertisementPhoto> updatedPhotos = new HashSet<>(this.photos);
        updatedPhotos.add(advertisementPhoto);

        return this.copy().photos(updatedPhotos).build();
    }

    public Advertisement updateDetails(final AdvertisementDetails newDetails) {
        if (newDetails == null) {
            throw new InvalidArgumentValueException("New details cannot be null");
        }

        return this.copy().details(newDetails).build();
    }

    public Advertisement updateTitle(final Title newTitle) {
        if (newTitle == null) {
            throw new InvalidArgumentValueException("New title cannot be null");
        }

        if (Objects.equals(this.title, newTitle)) {
            throw new InvalidArgumentValueException("New title cannot be the same as old title");
        }

        return copy().title(newTitle).slug(Slug.create(newTitle.value())).build();
    }

    public Advertisement updateDescription(final Description newDescription) {
        return copy().description(newDescription).build();
    }

    public Advertisement updatePrice(final Money newPrice) {
        if (Objects.equals(this.price, newPrice)) {
            throw new InvalidArgumentValueException("New price cannot be the same as old price");
        }

        final Advertisement currentObj = this.copy().price(newPrice).build();
        currentObj.addEvent(
                new AdvertisementPriceChangedEvent(currentObj.id, this.price, newPrice));

        return currentObj;
    }

    public Advertisement activate() {
        if (this.status == AdvertisementStatus.INACTIVE) {
            final Advertisement currentObj = this.copy().status(AdvertisementStatus.ACTIVE).build();

            currentObj.addEvent(
                    new AdvertisementStatusChangedEvent(
                            currentObj.id, this.status, currentObj.status));

            return currentObj;
        }

        if (this.status == AdvertisementStatus.SOLD) {
            throw new InvalidArgumentValueException("Advertisement is already sold");
        }

        throw new InvalidArgumentValueException("Advertisement is already active");
    }

    public Advertisement inactivate() {
        if (this.status == AdvertisementStatus.ACTIVE) {
            final Advertisement currentObj =
                    this.copy().status(AdvertisementStatus.INACTIVE).build();

            currentObj.addEvent(
                    new AdvertisementStatusChangedEvent(
                            currentObj.id, this.status, currentObj.status));

            return currentObj;
        }

        if (this.status == AdvertisementStatus.SOLD) {
            throw new InvalidArgumentValueException("Advertisement is already sold");
        }

        throw new InvalidArgumentValueException("Advertisement is already inactive");
    }

    public Advertisement setAsSold() {
        if (this.status != AdvertisementStatus.SOLD) {
            final Advertisement currentObj = this.copy().status(AdvertisementStatus.SOLD).build();

            currentObj.addEvent(
                    new AdvertisementStatusChangedEvent(
                            currentObj.id, this.status, currentObj.status));

            return currentObj;
        }

        throw new InvalidArgumentValueException("Advertisement is already sold");
    }

    private Advertisement(
            final Identifier id,
            final Slug slug,
            final Title title,
            final Description description,
            final Money price,
            final Locality locality,
            final AdvertisementDetails details,
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

    public static Builder create() {
        return new Builder(true).id(Identifier.generate()).createdAt(Instant.now());
    }

    public static Builder reconstitute() {
        return new Builder(false);
    }

    private Builder copy() {
        return new Builder(false)
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

    public static final class Builder {
        private Identifier id;
        private Slug slug;
        private Title title;
        private Description description;
        private Money price;
        private Locality locality;
        private AdvertisementDetails details;
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

        private void validateRequiredFields() {
            requireNonNull(this.id, "id");
            requireNonNull(this.title, "title");
            requireNonNull(this.description, "description");
            requireNonNull(this.price, "price");
            requireNonNull(this.locality, "locality");
            requireNonNull(this.details, "details");
            requireNonNull(this.status, "status");
            requireNonNull(this.userId, "userId");
            requireNonNull(this.createdAt, "createdAt");

            if (!createMode) {
                requireNonNull(this.slug, "slug");
            }

            if (createdAt.isAfter(Instant.now())) {
                throw new InvalidArgumentValueException("CreatedAt cannot be in the future");
            }
        }

        public Advertisement build() {
            validateRequiredFields();

            if (this.createMode) {
                this.slug = Slug.create(title.value());
            }

            return new Advertisement(
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

        public Builder slug(final Slug slug) {
            if (!createMode) {
                this.slug = slug;
            }

            return this;
        }

        public Builder title(final Title title) {
            this.title = title;
            return this;
        }

        public Builder description(final Description description) {
            this.description = description;
            return this;
        }

        public Builder price(final Money price) {
            this.price = price;
            return this;
        }

        public Builder locality(final Locality locality) {
            this.locality = locality;
            return this;
        }

        public Builder details(final AdvertisementDetails details) {
            this.details = details;
            return this;
        }

        public Builder status(final AdvertisementStatus status) {
            this.status = status;
            return this;
        }

        public Builder userId(final Identifier userId) {
            this.userId = userId;
            return this;
        }

        public Builder id(final Identifier id) {
            if (!createMode) {
                this.id = id;
            }

            return this;
        }

        public Builder photos(final Set<AdvertisementPhoto> photos) {
            this.photos = photos == null ? new HashSet<>() : new HashSet<>(photos);
            return this;
        }

        public Builder createdAt(final Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
    }

    private boolean photoExists(final AdvertisementPhoto photo) {
        return photos.stream()
                .map(AdvertisementPhoto::getUrl)
                .anyMatch(f -> f.equals(photo.getUrl()));
    }
}
