/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toCollection;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.commons.lang3.BooleanUtils;

import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.event.AdvertisementPriceChangedEvent;
import pl.dawid0604.realestate.domain.shared.event.AdvertisementStatusChangedEvent;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;
import pl.dawid0604.realestate.domain.shared.exception.MaxPhotosExceededException;
import pl.dawid0604.realestate.domain.shared.exception.UnauthorizedAccessException;

public final class Advertisement extends AggregateRoot {
    private final Identifier id;
    private final Slug slug;
    private final Title title;
    private final Description description;
    private final Price price;
    private final Area area;
    private final PricePerSquareMeter pricePerSquareMeter;
    private final Locality locality;
    private final AdvertisementDetails<?> details;
    private final AdvertisementStatus status;
    private final Identifier userId;
    private final AdvertisementType advertisementType;
    private final Set<AdvertisementPhoto> photos;
    private final Instant createdAt;
    private final boolean featured;
    private static final int MAX_NUMBER_OF_PHOTOS = 20;

    public Advertisement addPhoto(final AdvertisementPhoto advertisementPhoto) {
        requireNonNull(advertisementPhoto, "AdvertisementPhoto");

        if (this.photos.contains(advertisementPhoto)) {
            throw new InvalidArgumentValueException("Photo already exists");
        }

        if (this.photos.size() + 1 > MAX_NUMBER_OF_PHOTOS) {
            throw new MaxPhotosExceededException(MAX_NUMBER_OF_PHOTOS);
        }

        return this.copy().photos(mergePhotos(advertisementPhoto)).build();
    }

    public Advertisement updateLocality(final Locality locality) {
        return this.copy().locality(locality).build();
    }

    public Advertisement removePhoto(final Identifier photoId) {
        requireNonNull(photoId, "PhotoId");

        final AdvertisementPhoto advertisementPhoto =
                photos.stream()
                        .filter(p -> Objects.equals(p.getId(), photoId))
                        .findFirst()
                        .orElseThrow(
                                () -> new InvalidArgumentValueException("Photo does not exist"));

        return this.copy().photos(removeFromPhotos(advertisementPhoto)).build();
    }

    public Advertisement updateDetails(final AdvertisementDetails<?> newDetails) {
        if (newDetails != null && !Objects.equals(this.details.getClass(), newDetails.getClass())) {
            throw new InvalidArgumentValueException("Details must be of the same type");
        }

        return this.copy().details(newDetails).build();
    }

    public Advertisement updateTitle(final Title newTitle) {
        if (Objects.equals(this.title, newTitle)) {
            throw new InvalidArgumentValueException(
                    "Incoming title cannot be the same as old title");
        }

        return copy().title(newTitle).slug(Slug.create(newTitle)).build();
    }

    public Advertisement updateArea(final Area newArea) {
        if (Objects.equals(this.area, newArea)) {
            throw new InvalidArgumentValueException("Incoming area cannot be the same as old area");
        }

        return copy().area(newArea)
                .pricePerSquareMeter(PricePerSquareMeter.create(newArea, this.price))
                .build();
    }

    public Advertisement updateDescription(final Description newDescription) {
        return copy().description(newDescription).build();
    }

    public Advertisement updatePrice(final Price newPrice) {
        if (Objects.equals(this.price, newPrice)) {
            throw new InvalidArgumentValueException("Price cannot be the same as old price");
        }

        final Advertisement currentObj =
                this.copy()
                        .price(newPrice)
                        .pricePerSquareMeter(PricePerSquareMeter.create(this.area, newPrice))
                        .build();

        currentObj.addEvent(
                new AdvertisementPriceChangedEvent(currentObj.id, this.price, newPrice));

        return currentObj;
    }

    public Advertisement activate() {
        if (this.status == AdvertisementStatus.SOLD) {
            throw new InvalidArgumentValueException("Advertisement is already sold");
        }

        if (this.status == AdvertisementStatus.INACTIVE) {
            final Advertisement currentObj = this.copy().status(AdvertisementStatus.ACTIVE).build();

            currentObj.addEvent(
                    new AdvertisementStatusChangedEvent(
                            currentObj.id, this.status, currentObj.status));

            return currentObj;
        }

        throw new InvalidArgumentValueException("Advertisement is already active");
    }

    public Advertisement deactivate() {
        if (this.status == AdvertisementStatus.SOLD) {
            throw new InvalidArgumentValueException("Advertisement is already sold");
        }

        if (this.status == AdvertisementStatus.ACTIVE) {
            final Advertisement currentObj =
                    this.copy().status(AdvertisementStatus.INACTIVE).build();

            currentObj.addEvent(
                    new AdvertisementStatusChangedEvent(
                            currentObj.id, this.status, currentObj.status));

            return currentObj;
        }

        throw new InvalidArgumentValueException("Advertisement is already inactive");
    }

    public Advertisement setAsSold() {
        if (this.status == AdvertisementStatus.INACTIVE) {
            throw new InvalidArgumentValueException("Advertisement must be active");
        }

        if (this.status == AdvertisementStatus.ACTIVE) {
            final Advertisement currentObj = this.copy().status(AdvertisementStatus.SOLD).build();

            currentObj.addEvent(
                    new AdvertisementStatusChangedEvent(
                            currentObj.id, this.status, currentObj.status));

            return currentObj;
        }

        throw new InvalidArgumentValueException("Advertisement is already sold");
    }

    public Advertisement delete() {
        if (this.status == AdvertisementStatus.DELETED) {
            throw new InvalidArgumentValueException("Advertisement is already deleted");
        }

        return this.copy().status(AdvertisementStatus.DELETED).build();
    }

    public Advertisement setAsFeatured() {
        if (this.status != AdvertisementStatus.ACTIVE) {
            throw new InvalidArgumentValueException("Advertisement must be active");
        }

        if (this.isFeatured()) {
            throw new InvalidArgumentValueException("Advertisement is already featured");
        }

        return this.copy().featured(true).build();
    }

    public Advertisement disableFeaturedState() {
        if (this.status != AdvertisementStatus.ACTIVE) {
            throw new InvalidArgumentValueException("Advertisement must be active");
        }

        if (!this.isFeatured()) {
            throw new InvalidArgumentValueException("Advertisement is not featured");
        }

        return this.copy().featured(false).build();
    }

    public void verifyOwner(final User user) {
        requireNonNull(user, "User");

        if (!Objects.equals(userId, user.getId()) && !user.isAdmin()) {
            throw new UnauthorizedAccessException("No permissions to modify this advertisement");
        }
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

    public AdvertisementDetails<?> getDetails() {
        return details;
    }

    public Title getTitle() {
        return title;
    }

    public Description getDescription() {
        return description;
    }

    public Price getPrice() {
        return price;
    }

    public PricePerSquareMeter getPricePerSquareMeter() {
        return pricePerSquareMeter;
    }

    public Area getArea() {
        return area;
    }

    public AdvertisementStatus getStatus() {
        return status;
    }

    public Locality getLocality() {
        return locality;
    }

    public boolean isFeatured() {
        return featured;
    }

    public boolean isActive() {
        return status == AdvertisementStatus.ACTIVE;
    }

    public boolean isInactive() {
        return status == AdvertisementStatus.INACTIVE;
    }

    public boolean isSold() {
        return status == AdvertisementStatus.SOLD;
    }

    public boolean isDeleted() {
        return status == AdvertisementStatus.DELETED;
    }

    public Identifier getOwner() {
        return userId;
    }

    public AdvertisementType getAdvertisementType() {
        return advertisementType;
    }

    private Advertisement(
            final Identifier id,
            final Slug slug,
            final Title title,
            final Description description,
            final Price price,
            final Area area,
            final PricePerSquareMeter pricePerSquareMeter,
            final Locality locality,
            final AdvertisementDetails<?> details,
            final AdvertisementStatus status,
            final Identifier userId,
            final Set<AdvertisementPhoto> photos,
            final Boolean featured,
            final Instant createdAt,
            final AdvertisementType advertisementType) {

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
        this.featured = featured;
        this.createdAt = createdAt;
        this.area = area;
        this.pricePerSquareMeter = pricePerSquareMeter;
        this.advertisementType = advertisementType;
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
                .slug(this.slug)
                .title(this.title)
                .description(this.description)
                .price(this.price)
                .locality(this.locality)
                .details(this.details)
                .status(this.status)
                .userId(this.userId)
                .area(this.area)
                .pricePerSquareMeter(this.pricePerSquareMeter)
                .photos(this.photos)
                .createdAt(this.createdAt);
    }

    public static final class Builder {
        private Identifier id;
        private Slug slug;
        private Title title;
        private Description description;
        private Price price;
        private Area area;
        private PricePerSquareMeter pricePerSquareMeter;
        private Locality locality;
        private AdvertisementDetails<?> details;
        private AdvertisementStatus status;
        private Identifier userId;
        private Set<AdvertisementPhoto> photos;
        private Instant createdAt;
        private Boolean featured;
        private final boolean createMode;

        private Builder(final boolean createMode) {
            this.createMode = createMode;
        }

        public Advertisement build() {
            requireNonNull(this.title, "Title");
            requireNonNull(this.description, "Description");
            requireNonNull(this.price, "Price");
            requireNonNull(this.locality, "Locality");
            requireNonNull(this.details, "Details");
            requireNonNull(this.userId, "UserId");
            requireNonNull(this.area, "Area");

            if (createMode) {
                this.id = Identifier.generate();
                this.createdAt = Instant.now();
                this.slug = Slug.create(title);
                this.status = AdvertisementStatus.ACTIVE;
                this.pricePerSquareMeter = PricePerSquareMeter.create(area, price);

            } else {
                requireNonNull(this.id, "Id");
                requireNonNull(this.createdAt, "CreatedAt");
                requireNonNull(this.slug, "Slug");
                requireNonNull(this.status, "Status");
                requireNonNull(this.pricePerSquareMeter, "PricePerSquareMeter");
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

            return new Advertisement(
                    this.id,
                    this.slug,
                    this.title,
                    this.description,
                    this.price,
                    this.area,
                    this.pricePerSquareMeter,
                    this.locality,
                    this.details,
                    this.status,
                    this.userId,
                    this.photos,
                    BooleanUtils.toBoolean(this.featured),
                    this.createdAt,
                    determineAdvertisementType(this.details));
        }

        private static AdvertisementType determineAdvertisementType(
                final AdvertisementDetails<?> details) {

            return switch (details) {
                case FlatDetails ignored -> AdvertisementType.FLAT;
                case CommercialDetails ignored -> AdvertisementType.COMMERCIAL;
                case HouseDetails ignored -> AdvertisementType.HOUSE;
                case PlotDetails ignored -> AdvertisementType.PLOT;
            };
        }

        public Builder slug(final Slug slug) {
            this.slug = slug;
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

        public Builder price(final Price price) {
            this.price = price;
            return this;
        }

        public Builder area(final Area area) {
            this.area = area;
            return this;
        }

        public Builder pricePerSquareMeter(final PricePerSquareMeter pricePerSquareMeter) {
            this.pricePerSquareMeter = pricePerSquareMeter;
            return this;
        }

        public Builder locality(final Locality locality) {
            this.locality = locality;
            return this;
        }

        public Builder details(final AdvertisementDetails<?> details) {
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
            this.id = id;
            return this;
        }

        public Builder featured(final boolean featured) {
            this.featured = featured;
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

    private Set<AdvertisementPhoto> removeFromPhotos(final AdvertisementPhoto advertisementPhoto) {
        final List<AdvertisementPhoto> sortedPhotos =
                this.photos.stream()
                        .filter(p -> !Objects.equals(p, advertisementPhoto))
                        .sorted(comparingInt(AdvertisementPhoto::getPosition))
                        .toList();

        return reindexPhotos(sortedPhotos);
    }

    private Set<AdvertisementPhoto> mergePhotos(final AdvertisementPhoto advertisementPhoto) {
        final List<AdvertisementPhoto> sortedPhotos = new ArrayList<>(photos);
        sortedPhotos.sort(comparingInt(AdvertisementPhoto::getPosition));

        final int targetIndex = Math.min(advertisementPhoto.getPosition(), sortedPhotos.size());
        sortedPhotos.add(targetIndex, advertisementPhoto);

        return reindexPhotos(sortedPhotos);
    }

    private static Set<AdvertisementPhoto> reindexPhotos(
            final List<AdvertisementPhoto> sortedPhotos) {

        return IntStream.range(0, sortedPhotos.size())
                .mapToObj(
                        pos -> {
                            final AdvertisementPhoto photo = sortedPhotos.get(pos);
                            return AdvertisementPhoto.of(photo.getId(), photo.getUrl(), pos);
                        })
                .collect(toCollection(HashSet::new));
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof final Advertisement that && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
