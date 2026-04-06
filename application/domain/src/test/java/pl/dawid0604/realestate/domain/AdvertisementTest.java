/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.stream.Collectors.toMap;
import static pl.dawid0604.realestate.domain.AdvertisementStatus.ACTIVE;
import static pl.dawid0604.realestate.domain.AdvertisementStatus.DELETED;
import static pl.dawid0604.realestate.domain.AdvertisementStatus.INACTIVE;
import static pl.dawid0604.realestate.domain.AdvertisementStatus.SOLD;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import pl.dawid0604.realestate.domain.shared.event.AdvertisementPriceChangedEvent;
import pl.dawid0604.realestate.domain.shared.event.AdvertisementStatusChangedEvent;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;
import pl.dawid0604.realestate.domain.shared.exception.MaxPhotosExceededException;
import pl.dawid0604.realestate.domain.shared.exception.UnauthorizedAccessException;

class AdvertisementTest {

    @Nested
    final class BuilderTests {

        @Nested
        final class CreateTests {

            @Test
            @DisplayName("Should throw exception when title is null")
            void shouldThrowWhenTitleIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(() -> Advertisement.create().build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Title cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when description is null")
            void shouldThrowWhenDescriptionIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () -> Advertisement.create().title(getValidTitle()).build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Description cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when price is null")
            void shouldThrowWhenPriceIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.create()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Price cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when locality is null")
            void shouldThrowWhenLocalityIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.create()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Locality cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when details is null")
            void shouldThrowWhenDetailsIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.create()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .locality(getValidLocality())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Details cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when userId is null")
            void shouldThrowWhenUserIdIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.create()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .locality(getValidLocality())
                                                .details(getValidDetails())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("UserId cannot be null");
            }

            @Test
            @DisplayName("Should generate id")
            void shouldGenerateId() {
                // Given
                // When
                final Advertisement instance =
                        Advertisement.create()
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .userId(getValidIdentifier())
                                .build();

                // Then
                Assertions.assertThat(instance.getId()).isNotNull();
            }

            @Test
            @DisplayName("Should not substitute id")
            void shouldNotSubstituteId() {
                // Given
                final Identifier id = Identifier.generate();

                // When
                final Advertisement instance =
                        Advertisement.create()
                                .id(id)
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .userId(getValidIdentifier())
                                .build();

                // Then
                Assertions.assertThat(instance.getId()).isNotEqualTo(id);
            }

            @Test
            @DisplayName("Should set createdAt")
            void shouldSetCreatedAt() {
                // Given
                // When
                final Advertisement instance =
                        Advertisement.create()
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .userId(getValidIdentifier())
                                .build();

                // Then
                Assertions.assertThat(instance.getCreatedAt().truncatedTo(SECONDS))
                        .isEqualTo(Instant.now().truncatedTo(SECONDS));
            }

            @Test
            @DisplayName("Should set status")
            void shouldSetStatus() {
                // Given
                // When
                final Advertisement instance =
                        Advertisement.create()
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .userId(getValidIdentifier())
                                .build();

                // Then
                Assertions.assertThat(instance.isActive()).isTrue();
            }

            @Test
            @DisplayName("Should not substitute createdAt")
            void shouldNotSubstituteCreatedAt() {
                // Given
                final Instant createdAt = Instant.now().minusSeconds(3_600_000);

                // When
                final Advertisement instance =
                        Advertisement.create()
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .status(getValidStatus())
                                .userId(getValidIdentifier())
                                .createdAt(createdAt)
                                .build();

                // Then
                Assertions.assertThat(instance.getCreatedAt().truncatedTo(SECONDS))
                        .isNotEqualTo(createdAt.truncatedTo(SECONDS));
            }

            @Test
            @DisplayName("Should not substitute status")
            void shouldNotSubstituteStatus() {
                // Given
                // When
                final Advertisement instance =
                        Advertisement.create()
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .status(AdvertisementStatus.SOLD)
                                .userId(getValidIdentifier())
                                .createdAt(Instant.now())
                                .build();

                // Then
                Assertions.assertThat(instance.isActive()).isTrue();
                Assertions.assertThat(instance.isSold()).isFalse();
            }

            @Test
            @DisplayName("Should generate slug")
            void shouldGenerateSlug() {
                // Given
                // When
                final Advertisement instance =
                        Advertisement.create()
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .status(getValidStatus())
                                .userId(getValidIdentifier())
                                .build();

                // Then
                Assertions.assertThat(instance.getSlug()).isNotNull();
            }

            @Test
            @DisplayName("Should not substitute slug")
            void shouldNotSubstituteSlug() {
                // Given
                final Title title = new Title("cde cde cde");
                final Slug slug = Slug.create(new Title("abc abc adbc"));

                // When
                final Advertisement instance =
                        Advertisement.create()
                                .title(title)
                                .slug(slug)
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .status(getValidStatus())
                                .userId(getValidIdentifier())
                                .build();

                // Then
                Assertions.assertThat(instance.getSlug()).isNotEqualTo(slug);
            }
        }

        @Nested
        final class ReconstituteTests {

            @Test
            @DisplayName("Should throw exception when title is null")
            void shouldThrowWhenTitleIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(() -> Advertisement.reconstitute().build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Title cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when description is null")
            void shouldThrowWhenDescriptionIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () -> Advertisement.reconstitute().title(getValidTitle()).build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Description cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when price is null")
            void shouldThrowWhenPriceIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.reconstitute()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Price cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when locality is null")
            void shouldThrowWhenLocalityIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.reconstitute()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Locality cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when details is null")
            void shouldThrowWhenDetailsIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.reconstitute()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .locality(getValidLocality())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Details cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when status is null")
            void shouldThrowWhenStatusIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.reconstitute()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .locality(getValidLocality())
                                                .userId(getValidIdentifier())
                                                .id(getValidIdentifier())
                                                .createdAt(Instant.now())
                                                .slug(getValidSlug())
                                                .details(getValidDetails())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Status cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when userId is null")
            void shouldThrowWhenUserIdIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.reconstitute()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .locality(getValidLocality())
                                                .details(getValidDetails())
                                                .status(getValidStatus())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("UserId cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when id is null")
            void shouldThrowWhenIdIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.reconstitute()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .locality(getValidLocality())
                                                .details(getValidDetails())
                                                .status(getValidStatus())
                                                .userId(getValidIdentifier())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Id cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when createdAt is null")
            void shouldThrowWhenCreatedAtIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.reconstitute()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .locality(getValidLocality())
                                                .details(getValidDetails())
                                                .status(getValidStatus())
                                                .userId(getValidIdentifier())
                                                .id(getValidIdentifier())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("CreatedAt cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when createdAt is from the future")
            void shouldThrowWhenCreatedAtIsFromTheFuture() {
                // Given
                final Instant createdAt = Instant.now().plusSeconds(36_000);
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.reconstitute()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .locality(getValidLocality())
                                                .details(getValidDetails())
                                                .status(getValidStatus())
                                                .userId(getValidIdentifier())
                                                .id(getValidIdentifier())
                                                .createdAt(createdAt)
                                                .slug(getValidSlug())
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("CreatedAt cannot be from the future");
            }

            @Test
            @DisplayName("Should throw exception when slug is null")
            void shouldThrowWhenSlugIsNull() {
                // Given
                final Instant createdAt = Instant.now().minusSeconds(36_000);

                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.reconstitute()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .locality(getValidLocality())
                                                .details(getValidDetails())
                                                .status(getValidStatus())
                                                .userId(getValidIdentifier())
                                                .id(getValidIdentifier())
                                                .createdAt(createdAt)
                                                .build())
                        .isExactlyInstanceOf(InvalidArgumentValueException.class)
                        .hasMessage("Slug cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when photos limit exceeded")
            void shouldThrowExceptionWhenPhotosLimitExceeded() {
                // Given
                final Set<AdvertisementPhoto> photos = new HashSet<>();

                for (int i = 0; i < 21; i++) {
                    photos.add(AdvertisementPhoto.create(new Url("https://" + i), i));
                }

                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        Advertisement.reconstitute()
                                                .title(getValidTitle())
                                                .description(getValidDescription())
                                                .price(getValidPrice())
                                                .locality(getValidLocality())
                                                .details(getValidDetails())
                                                .status(getValidStatus())
                                                .userId(getValidIdentifier())
                                                .photos(photos)
                                                .slug(getValidSlug())
                                                .id(getValidIdentifier())
                                                .createdAt(Instant.now())
                                                .build())
                        .isExactlyInstanceOf(MaxPhotosExceededException.class);
            }

            @Test
            @DisplayName("Should set photos")
            void shouldSetPhotos() {
                // Given
                final Set<AdvertisementPhoto> photos =
                        Set.of(
                                AdvertisementPhoto.create(new Url("https://xyz"), 0),
                                AdvertisementPhoto.create(new Url("http://xyzeu"), 1));

                // When
                final Advertisement instance =
                        Advertisement.reconstitute()
                                .id(getValidIdentifier())
                                .slug(getValidSlug())
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .status(getValidStatus())
                                .userId(getValidIdentifier())
                                .createdAt(Instant.now())
                                .photos(photos)
                                .build();

                // Then
                Assertions.assertThat(instance.getPhotos())
                        .isNotNull()
                        .containsExactlyInAnyOrderElementsOf(photos);
            }

            @Test
            @DisplayName("Should set photos as empty collection when given value is null")
            void shouldSetPhotosAsEmptyCollectionWhenGivenValueIsNull() {
                // Given
                // When
                final Advertisement instance =
                        Advertisement.reconstitute()
                                .id(getValidIdentifier())
                                .slug(getValidSlug())
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .status(getValidStatus())
                                .userId(getValidIdentifier())
                                .createdAt(Instant.now())
                                .build();

                // Then
                Assertions.assertThat(instance.getPhotos()).isNotNull().isEmpty();
            }

            @Test
            @DisplayName("Should set photos when number of photos as limit boundary")
            void shouldSetPhotosWhenNumberOfPhotosAsLimitBoundary() {
                // Given
                final Set<AdvertisementPhoto> photos = new HashSet<>();

                for (int i = 0; i < 20; i++) {
                    photos.add(AdvertisementPhoto.create(new Url("https://" + i), i));
                }

                // When
                final Advertisement instance =
                        Advertisement.reconstitute()
                                .id(getValidIdentifier())
                                .slug(getValidSlug())
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .status(getValidStatus())
                                .userId(getValidIdentifier())
                                .createdAt(Instant.now())
                                .photos(photos)
                                .build();

                // Then
                Assertions.assertThat(instance.getPhotos())
                        .isNotNull()
                        .containsExactlyInAnyOrderElementsOf(photos);
            }

            @Test
            @DisplayName("Should return copy of photos at getter")
            void shouldReturnCopyOfPhotosAtGetter() {
                // Given
                final Set<AdvertisementPhoto> photos =
                        Set.of(
                                AdvertisementPhoto.create(new Url("https://xyz"), 0),
                                AdvertisementPhoto.create(new Url("http://xyzeu"), 1));

                // When
                final Advertisement instance =
                        Advertisement.reconstitute()
                                .id(getValidIdentifier())
                                .slug(getValidSlug())
                                .title(getValidTitle())
                                .description(getValidDescription())
                                .price(getValidPrice())
                                .locality(getValidLocality())
                                .details(getValidDetails())
                                .status(getValidStatus())
                                .userId(getValidIdentifier())
                                .createdAt(Instant.now())
                                .photos(photos)
                                .build();

                // Then
                Assertions.assertThat(instance.getPhotos() == photos).isFalse();
            }
        }
    }

    @Nested
    final class UpdateDetailsTests {

        @Test
        @DisplayName("Should throw exception when details is null")
        void shouldThrowExceptionWhenDetailsIsNull() {
            // Given
            final PlotDetails plotDetails =
                    new PlotDetails(new Area(null), PlotBuildingType.CONSTRUCTION, null);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(plotDetails)
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.updateDetails(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Details cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when details has different type")
        void shouldThrowExceptionWhenDetailsHasDifferentType() {
            // Given
            final PlotDetails plotDetails =
                    new PlotDetails(new Area(null), PlotBuildingType.CONSTRUCTION, null);

            final HouseDetails houseDetails =
                    new HouseDetails(
                            new Area(null),
                            HouseBuildingType.DETACHED,
                            null,
                            new NumberOfRooms(null),
                            new Floor(null),
                            new BuiltYear(null),
                            TypeOfMarket.PRIMARY);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(plotDetails)
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.updateDetails(houseDetails))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Details must be of the same type");
        }

        @Test
        @DisplayName("Should update details successfully")
        void shouldUpdateDetailsSuccessfully() {
            // Given
            final PlotDetails plotDetails =
                    new PlotDetails(new Area(null), PlotBuildingType.CONSTRUCTION, null);

            final PlotDetails incomingPlotDetails =
                    new PlotDetails(
                            new Area(BigDecimal.valueOf(2_500_000)),
                            PlotBuildingType.CONSTRUCTION,
                            null);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(plotDetails)
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.updateDetails(incomingPlotDetails);

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getDetails()).isEqualTo(incomingPlotDetails);
        }
    }

    @Nested
    final class UpdateTitleTests {

        @Test
        @DisplayName("Should throw exception when title is null")
        void shouldThrowExceptionWhenTitleIsNull() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.updateTitle(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Title cannot be null");
        }

        @Test
        @DisplayName("Should update title successfully")
        void shouldUpdateTitleSuccessfully() {
            // Given
            final Title title = new Title("abc abc abc");
            final Title incomingTitle = new Title("cde cde cde");

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(title)
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.updateTitle(incomingTitle);

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getTitle()).isEqualTo(incomingTitle);
        }

        @Test
        @DisplayName("Should throw exception when incoming title is the same as old title")
        void shouldThrowExceptionWhenIncomingTitleIsTheSameAsOldTitle() {
            // Given
            final Title title = new Title("abc abc abc");
            final Title incomingTitle = new Title("abc abc abc");

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(title)
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.updateTitle(incomingTitle))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Incoming title cannot be the same as old title");
        }

        @Test
        @DisplayName("Should update slug")
        void shouldUpdateSlug() {
            // Given
            final Title title = new Title("abc abc abc");
            final Title incomingTitle = new Title("cde cde cde");
            final Slug slug = Slug.create(title);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(slug)
                            .title(title)
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.updateTitle(incomingTitle);

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getSlug()).isNotNull().isNotEqualTo(slug);
        }
    }

    @Nested
    final class UpdateDescriptionTests {

        @Test
        @DisplayName("Should throw exception when description is null")
        void shouldThrowExceptionWhenDescriptionIsNull() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.updateDescription(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Description cannot be null");
        }

        @Test
        @DisplayName("Should update description successfully")
        void shouldUpdateDescriptionSuccessfully() {
            // Given
            final Description description = new Description("abc abc abc");
            final Description incomingDescription = new Description("cde cde cde");

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(description)
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.updateDescription(incomingDescription);

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getDescription()).isEqualTo(incomingDescription);
        }
    }

    @Nested
    final class UpdatePriceTests {

        @Test
        @DisplayName("Should throw exception when price is null")
        void shouldThrowExceptionWhenPriceIsNull() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.updatePrice(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Price cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when incoming price is the same as old price")
        void shouldThrowExceptionWhenIncomingPriceIsTheSameAsOldPrice() {
            // Given
            final Money price = new Money(BigDecimal.valueOf(2_500_00), MoneyCurrency.PLN);
            final Money incomingPrice = new Money(BigDecimal.valueOf(2_500_00), MoneyCurrency.PLN);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(price)
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.updatePrice(incomingPrice))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Price cannot be the same as old price");
        }

        @Test
        @DisplayName("Should update price successfully")
        void shouldUpdatePriceSuccessfully() {
            // Given
            final Money price = new Money(BigDecimal.valueOf(2_500_00), MoneyCurrency.PLN);
            final Money incomingPrice = new Money(BigDecimal.valueOf(1_500_00), MoneyCurrency.PLN);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(price)
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.updatePrice(incomingPrice);

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getPrice()).isEqualTo(incomingPrice);
        }

        @Test
        @DisplayName("Should add event after successful update")
        void shouldAddEventAfterSuccessfulUpdate() {
            // Given
            final Money price = new Money(BigDecimal.valueOf(2_500_00), MoneyCurrency.PLN);
            final Money incomingPrice = new Money(BigDecimal.valueOf(1_500_00), MoneyCurrency.PLN);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(price)
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.updatePrice(incomingPrice);

            // Then
            final AdvertisementPriceChangedEvent expectedEvent =
                    new AdvertisementPriceChangedEvent(instance.getId(), price, incomingPrice);

            Assertions.assertThat(instance.getEvents()).isEmpty();
            Assertions.assertThat(updatedInstance.getEvents()).containsExactly(expectedEvent);
        }
    }

    @Nested
    final class UpdateLocalityTests {

        @Test
        @DisplayName("Should throw exception when locality is null")
        void shouldThrowExceptionWhenLocalityIsNull() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.updateLocality(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Locality cannot be null");
        }

        @Test
        @DisplayName("Should update locality successfully")
        void shouldUpdateLocalitySuccessfully() {
            // Given
            final Locality locality = new Locality(Identifier.generate());
            final Locality incomingLocality = new Locality(Identifier.generate());

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(locality)
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.updateLocality(incomingLocality);

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getLocality()).isEqualTo(incomingLocality);
        }
    }

    @Nested
    final class ActivateStatusTests {

        @Test
        @DisplayName("Should throw exception when advertisement is sold")
        void shouldThrowExceptionWhenAdvertisementIsSold() {
            // Given
            final AdvertisementStatus status = AdvertisementStatus.SOLD;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::activate)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement is already sold");
        }

        @Test
        @DisplayName("Should throw exception when advertisement is active")
        void shouldThrowExceptionWhenAdvertisementIsActive() {
            // Given
            final AdvertisementStatus status = ACTIVE;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::activate)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement is already active");
        }

        @Test
        @DisplayName("Should update status successfully")
        void shouldUpdateStatusSuccessfully() {
            // Given
            final AdvertisementStatus status = AdvertisementStatus.INACTIVE;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.activate();

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.isActive()).isTrue();
        }

        @Test
        @DisplayName("Should add event after successful update")
        void shouldAddEventAfterSuccessfulUpdate() {
            // Given
            final AdvertisementStatus status = AdvertisementStatus.INACTIVE;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.activate();

            // Then
            final AdvertisementStatusChangedEvent expectedEvent =
                    new AdvertisementStatusChangedEvent(instance.getId(), status, ACTIVE);

            Assertions.assertThat(instance.getEvents()).isEmpty();
            Assertions.assertThat(updatedInstance.getEvents()).containsExactly(expectedEvent);
        }
    }

    @Nested
    final class DeactivateStatusTests {

        @Test
        @DisplayName("Should throw exception when advertisement is sold")
        void shouldThrowExceptionWhenAdvertisementIsSold() {
            // Given
            final AdvertisementStatus status = AdvertisementStatus.SOLD;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::deactivate)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement is already sold");
        }

        @Test
        @DisplayName("Should throw exception when advertisement is inactive")
        void shouldThrowExceptionWhenAdvertisementIsActive() {
            // Given
            final AdvertisementStatus status = AdvertisementStatus.INACTIVE;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::deactivate)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement is already inactive");
        }

        @Test
        @DisplayName("Should update status successfully")
        void shouldUpdateStatusSuccessfully() {
            // Given
            final AdvertisementStatus status = ACTIVE;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.deactivate();

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.isInactive()).isTrue();
        }

        @Test
        @DisplayName("Should add event after successful update")
        void shouldAddEventAfterSuccessfulUpdate() {
            // Given
            final AdvertisementStatus status = ACTIVE;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.deactivate();

            // Then
            final AdvertisementStatusChangedEvent expectedEvent =
                    new AdvertisementStatusChangedEvent(
                            instance.getId(), status, AdvertisementStatus.INACTIVE);

            Assertions.assertThat(instance.getEvents()).isEmpty();
            Assertions.assertThat(updatedInstance.getEvents()).containsExactly(expectedEvent);
        }
    }

    @Nested
    final class SetAsSoldStatusTests {

        @Test
        @DisplayName("Should throw exception when advertisement is sold")
        void shouldThrowExceptionWhenAdvertisementIsSold() {
            // Given
            final AdvertisementStatus status = AdvertisementStatus.SOLD;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::setAsSold)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement is already sold");
        }

        @Test
        @DisplayName("Should throw exception when advertisement is inactive")
        void shouldThrowExceptionWhenAdvertisementIsInactive() {
            // Given
            final AdvertisementStatus status = AdvertisementStatus.INACTIVE;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::setAsSold)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement must be active");
        }

        @Test
        @DisplayName("Should update status successfully")
        void shouldUpdateStatusSuccessfully() {
            // Given
            final AdvertisementStatus status = ACTIVE;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.setAsSold();

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.isSold()).isTrue();
        }

        @Test
        @DisplayName("Should add event after successful update")
        void shouldAddEventAfterSuccessfulUpdate() {
            // Given
            final AdvertisementStatus status = ACTIVE;

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.setAsSold();

            // Then
            final AdvertisementStatusChangedEvent expectedEvent =
                    new AdvertisementStatusChangedEvent(
                            instance.getId(), status, AdvertisementStatus.SOLD);

            Assertions.assertThat(instance.getEvents()).isEmpty();
            Assertions.assertThat(updatedInstance.getEvents()).containsExactly(expectedEvent);
        }
    }

    @Nested
    final class AddPhotoTests {

        @Test
        @DisplayName("Should throw exception when photo is null")
        void shouldThrowExceptionWhenPhotoIsNull() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.addPhoto(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("AdvertisementPhoto cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when photo exists")
        void shouldThrowExceptionWhenPhotoExists() {
            // Given
            final AdvertisementPhoto advertisementPhoto =
                    AdvertisementPhoto.create(new Url("https://xyz"), 0);

            final Set<AdvertisementPhoto> photos = Set.of(advertisementPhoto);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(photos)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.addPhoto(advertisementPhoto))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Photo already exists");
        }

        @Test
        @DisplayName("Should add photo when advertisement does not have any photos")
        void shouldAddPhotoWhenAdvertisementDoesNotHaveAnyPhotos() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            final AdvertisementPhoto advertisementPhoto =
                    AdvertisementPhoto.create(new Url("https://xyz"), 0);

            // When
            final Advertisement updatedInstance = instance.addPhoto(advertisementPhoto);

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getPhotos()).containsExactly(advertisementPhoto);
        }

        @Test
        @DisplayName("Should add photo when advertisement have other photos")
        void shouldAddPhotoWhenAdvertisementHaveOtherPhotos() {
            // Given
            final AdvertisementPhoto existingPhoto =
                    AdvertisementPhoto.create(new Url("https://abc"), 0);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(Set.of(existingPhoto))
                            .build();

            final AdvertisementPhoto advertisementPhoto =
                    AdvertisementPhoto.create(new Url("https://xyz"), 0);

            // When
            final Advertisement updatedInstance = instance.addPhoto(advertisementPhoto);

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getPhotos())
                    .contains(existingPhoto, advertisementPhoto);
        }

        @Test
        @DisplayName("Should reindex photos after successful update")
        void shouldReindexPhotosAfterSuccessfulUpdate() {
            // Given
            final AdvertisementPhoto existingPhoto =
                    AdvertisementPhoto.create(new Url("https://abc"), 0);

            final AdvertisementPhoto existingPhoto2 =
                    AdvertisementPhoto.create(new Url("https://cde"), 1);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(Set.of(existingPhoto, existingPhoto2))
                            .build();

            final AdvertisementPhoto advertisementPhoto =
                    AdvertisementPhoto.create(new Url("https://xyz"), 0);

            // When
            final Advertisement updatedInstance = instance.addPhoto(advertisementPhoto);

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getPhotos())
                    .contains(existingPhoto, advertisementPhoto, existingPhoto2);

            final Map<AdvertisementPhoto, Integer> groupedPhotos =
                    updatedInstance.getPhotos().stream()
                            .collect(toMap(p -> p, AdvertisementPhoto::getPosition));

            Assertions.assertThat(groupedPhotos.get(advertisementPhoto)).isEqualTo(0);
            Assertions.assertThat(groupedPhotos.get(existingPhoto)).isEqualTo(1);
            Assertions.assertThat(groupedPhotos.get(existingPhoto2)).isEqualTo(2);
        }

        @Test
        @DisplayName("Should throw exception when photos limit exceeded")
        void shouldThrowExceptionWhenPhotosLimitExceeded() {
            // Given
            final Set<AdvertisementPhoto> photos = new HashSet<>();

            for (int i = 0; i < 20; i++) {
                photos.add(AdvertisementPhoto.create(new Url("https://" + i), i));
            }

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .photos(photos)
                            .slug(getValidSlug())
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(
                            () ->
                                    instance.addPhoto(
                                            AdvertisementPhoto.create(new Url("https://11"), 11)))
                    .isExactlyInstanceOf(MaxPhotosExceededException.class);
        }

        @Test
        @DisplayName("Should add photo when number of photos as limit boundary")
        void shouldAddPhotoWhenNumberOfPhotosAsLimitBoundary() {
            // Given
            final Set<AdvertisementPhoto> photos = new HashSet<>();

            for (int i = 0; i < 19; i++) {
                photos.add(AdvertisementPhoto.create(new Url("https://" + i), i));
            }

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(photos)
                            .build();

            final AdvertisementPhoto advertisementPhoto =
                    AdvertisementPhoto.create(new Url("https://11"), 11);

            // When
            final Advertisement updatedInstance = instance.addPhoto(advertisementPhoto);

            // Then
            Assertions.assertThat(updatedInstance.getPhotos())
                    .isNotNull()
                    .containsAll(photos)
                    .contains(advertisementPhoto);
        }
    }

    @Nested
    final class RemovePhotoTests {

        @Test
        @DisplayName("Should throw exception when photo is null")
        void shouldThrowExceptionWhenPhotoIsNull() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.removePhoto(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("PhotoId cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when photo not exist")
        void shouldThrowExceptionWhenPhotoNotExist() {
            // Given
            final AdvertisementPhoto advertisementPhoto =
                    AdvertisementPhoto.create(new Url("https://xyz"), 0);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.removePhoto(advertisementPhoto.getId()))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Photo does not exist");
        }

        @Test
        @DisplayName("Should remove photo successfully when there is only one")
        void shouldRemovePhotoSuccessfullyWhenThereIsOnlyOne() {
            // Given
            final AdvertisementPhoto advertisementPhoto =
                    AdvertisementPhoto.create(new Url("https://xyz"), 0);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(Set.of(advertisementPhoto))
                            .build();

            // When
            final Advertisement updatedInstance = instance.removePhoto(advertisementPhoto.getId());

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getPhotos()).isEmpty();
        }

        @Test
        @DisplayName("Should remove first photo successfully with more photos")
        void shouldRemoveFirstPhotoSuccessfullyWithMorePhotos() {
            // Given
            final AdvertisementPhoto advertisementPhoto =
                    AdvertisementPhoto.create(new Url("https://xyz"), 0);

            final AdvertisementPhoto advertisementPhoto2 =
                    AdvertisementPhoto.create(new Url("https://abc"), 1);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(Set.of(advertisementPhoto, advertisementPhoto2))
                            .build();

            // When
            final Advertisement updatedInstance = instance.removePhoto(advertisementPhoto.getId());

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);

            final AdvertisementPhoto[] photosAsArray =
                    updatedInstance.getPhotos().toArray(AdvertisementPhoto[]::new);

            Assertions.assertThat(photosAsArray).doesNotContain(advertisementPhoto);
            Assertions.assertThat(photosAsArray).hasSize(1);
            Assertions.assertThat(photosAsArray[0])
                    .isEqualTo(
                            AdvertisementPhoto.of(
                                    advertisementPhoto2.getId(), advertisementPhoto2.getUrl(), 0));
        }

        @Test
        @DisplayName("Should remove last photo successfully with more photos")
        void shouldRemoveLastPhotoSuccessfullyWithMorePhotos() {
            // Given
            final AdvertisementPhoto advertisementPhoto =
                    AdvertisementPhoto.create(new Url("https://xyz"), 0);

            final AdvertisementPhoto advertisementPhoto2 =
                    AdvertisementPhoto.create(new Url("https://abc"), 1);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(Set.of(advertisementPhoto, advertisementPhoto2))
                            .build();

            // When
            final Advertisement updatedInstance = instance.removePhoto(advertisementPhoto2.getId());

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);

            final AdvertisementPhoto[] photosAsArray =
                    updatedInstance.getPhotos().toArray(AdvertisementPhoto[]::new);

            Assertions.assertThat(photosAsArray).doesNotContain(advertisementPhoto2);
            Assertions.assertThat(photosAsArray).hasSize(1);
            Assertions.assertThat(photosAsArray[0])
                    .isEqualTo(
                            AdvertisementPhoto.of(
                                    advertisementPhoto.getId(), advertisementPhoto.getUrl(), 0));
        }

        @Test
        @DisplayName("Should remove mid photo successfully with more photos")
        void shouldRemoveMidPhotoSuccessfullyWithMorePhotos() {
            // Given
            final AdvertisementPhoto advertisementPhoto =
                    AdvertisementPhoto.create(new Url("https://xyz"), 0);

            final AdvertisementPhoto advertisementPhoto2 =
                    AdvertisementPhoto.create(new Url("https://abc"), 1);

            final AdvertisementPhoto advertisementPhoto3 =
                    AdvertisementPhoto.create(new Url("https://cde"), 2);

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(getValidStatus())
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(
                                    Set.of(
                                            advertisementPhoto,
                                            advertisementPhoto2,
                                            advertisementPhoto3))
                            .build();

            // When
            final Advertisement updatedInstance = instance.removePhoto(advertisementPhoto2.getId());

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.getPhotos()).doesNotContain(advertisementPhoto2);
            Assertions.assertThat(updatedInstance.getPhotos()).hasSize(2);
            final List<AdvertisementPhoto> photos = updatedInstance.getPhotos().stream().toList();

            Assertions.assertThat(photos.get(photos.indexOf(advertisementPhoto)))
                    .isEqualTo(advertisementPhoto)
                    .matches(p -> p.getPosition() == 0);

            Assertions.assertThat(photos.get(photos.indexOf(advertisementPhoto3)))
                    .isEqualTo(advertisementPhoto3)
                    .matches(p -> p.getPosition() == 1);
        }
    }

    @Nested
    final class SetAsFeaturedTests {

        @ParameterizedTest
        @EnumSource(AdvertisementStatus.class)
        @DisplayName("Should throw exception when advertisement is not active")
        void shouldThrowExceptionWhenAdvertisementIsNotActive(final AdvertisementStatus status) {
            // Given

            if (status == ACTIVE) {
                return;
            }

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::setAsFeatured)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement must be active");
        }

        @Test
        @DisplayName("Should throw exception when advertisement is already featured")
        void shouldThrowExceptionWhenAdvertisementIsAlreadyFeatured() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(ACTIVE)
                            .featured(true)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::setAsFeatured)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement is already featured");
        }

        @Test
        @DisplayName("Should set advertisement as featured successfully")
        void shouldSetAdvertisementAsFeaturedSuccessfully() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(ACTIVE)
                            .featured(false)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.setAsFeatured();

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.isFeatured()).isTrue();
        }
    }

    @Nested
    final class DisableFeaturedStateTests {

        @ParameterizedTest
        @EnumSource(AdvertisementStatus.class)
        @DisplayName("Should throw exception when advertisement is not active")
        void shouldThrowExceptionWhenAdvertisementIsNotActive(final AdvertisementStatus status) {
            // Given
            if (status == ACTIVE) {
                return;
            }

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .featured(true)
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::disableFeaturedState)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement must be active");
        }

        @Test
        @DisplayName("Should throw exception when advertisement is not featured")
        void shouldThrowExceptionWhenAdvertisementIsNotFeatured() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(ACTIVE)
                            .featured(false)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::disableFeaturedState)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement is not featured");
        }

        @Test
        @DisplayName("Should set advertisement as featured successfully")
        void shouldDisableFeaturedStateSuccessfully() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(ACTIVE)
                            .featured(true)
                            .userId(getValidIdentifier())
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.disableFeaturedState();

            // Then
            Assertions.assertThat(instance).isEqualTo(updatedInstance);
            Assertions.assertThat(updatedInstance.isFeatured()).isFalse();
        }
    }

    @Nested
    final class DeleteTests {

        @Test
        @DisplayName("Should throw exception when advertisement is already deleted")
        void shouldThrowExceptionWhenAdvertisementIsAlreadyDeleted() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(AdvertisementStatus.DELETED)
                            .userId(getValidIdentifier())
                            .featured(true)
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(instance::delete)
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Advertisement is already deleted");
        }

        @ParameterizedTest
        @EnumSource(AdvertisementStatus.class)
        @DisplayName("Should delete successfully")
        void shouldDeleteSuccessfully(final AdvertisementStatus status) {
            // Given
            if (status == DELETED) {
                return;
            }

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(status)
                            .userId(getValidIdentifier())
                            .featured(true)
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            final Advertisement updatedInstance = instance.delete();

            // Then
            Assertions.assertThat(updatedInstance).isEqualTo(instance);
            Assertions.assertThat(updatedInstance.isDeleted()).isTrue();
        }
    }

    @Nested
    final class VerifyOwnerTests {

        @Test
        @DisplayName("Should throw exception when user is null")
        void shouldThrowExceptionWhenUserIsNull() {
            // Given
            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(AdvertisementStatus.DELETED)
                            .userId(getValidIdentifier())
                            .featured(true)
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.verifyOwner(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("User cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when user is different")
        void shouldThrowExceptionWhenUserIsDifferent() {
            // Given
            final User user =
                    User.reconstitute()
                            .email(new Email("abc@mail.com"))
                            .password(Password.ofHashed("$abc"))
                            .fullName(new FullName("abc", "cde"))
                            .role(UserRole.USER_ROLE)
                            .contactDetails(
                                    new ContactDetails(
                                            new Email("abc@mail.com"),
                                            new PhoneNumber("123456789")))
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .build();

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(AdvertisementStatus.DELETED)
                            .userId(getValidIdentifier())
                            .featured(true)
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatThrownBy(() -> instance.verifyOwner(user))
                    .isExactlyInstanceOf(UnauthorizedAccessException.class)
                    .hasMessage("No permissions to modify this advertisement");
        }

        @Test
        @DisplayName("Should verify when user is same")
        void shouldVerifyWhenUserIsSame() {
            // Given
            final User user =
                    User.reconstitute()
                            .email(new Email("abc@mail.com"))
                            .password(Password.ofHashed("$abc"))
                            .fullName(new FullName("abc", "cde"))
                            .role(UserRole.USER_ROLE)
                            .contactDetails(
                                    new ContactDetails(
                                            new Email("abc@mail.com"),
                                            new PhoneNumber("123456789")))
                            .status(UserStatus.ACTIVE)
                            .id(getValidIdentifier())
                            .createdAt(Instant.now())
                            .build();

            final Advertisement instance =
                    Advertisement.reconstitute()
                            .id(getValidIdentifier())
                            .slug(getValidSlug())
                            .title(getValidTitle())
                            .description(getValidDescription())
                            .price(getValidPrice())
                            .locality(getValidLocality())
                            .details(getValidDetails())
                            .status(AdvertisementStatus.DELETED)
                            .userId(user.getId())
                            .featured(true)
                            .createdAt(Instant.now())
                            .photos(null)
                            .build();

            // When
            // Then
            Assertions.assertThatCode(() -> instance.verifyOwner(user)).doesNotThrowAnyException();
        }
    }

    @ParameterizedTest
    @EnumSource(AdvertisementStatus.class)
    @DisplayName("Should return proper status at isActive")
    void shouldReturnProperStatusAtIsActive(final AdvertisementStatus status) {
        // Given
        final boolean expectedValue = status == ACTIVE;

        final Advertisement instance =
                Advertisement.reconstitute()
                        .id(getValidIdentifier())
                        .slug(getValidSlug())
                        .title(getValidTitle())
                        .description(getValidDescription())
                        .price(getValidPrice())
                        .locality(getValidLocality())
                        .details(getValidDetails())
                        .status(status)
                        .featured(true)
                        .userId(getValidIdentifier())
                        .createdAt(Instant.now())
                        .photos(null)
                        .build();

        // When
        // Then
        Assertions.assertThat(instance.isActive()).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @EnumSource(AdvertisementStatus.class)
    @DisplayName("Should return proper status at isInactive")
    void shouldReturnProperStatusAtIsInactive(final AdvertisementStatus status) {
        // Given
        final boolean expectedValue = status == INACTIVE;

        final Advertisement instance =
                Advertisement.reconstitute()
                        .id(getValidIdentifier())
                        .slug(getValidSlug())
                        .title(getValidTitle())
                        .description(getValidDescription())
                        .price(getValidPrice())
                        .locality(getValidLocality())
                        .details(getValidDetails())
                        .status(status)
                        .featured(true)
                        .userId(getValidIdentifier())
                        .createdAt(Instant.now())
                        .photos(null)
                        .build();

        // When
        // Then
        Assertions.assertThat(instance.isInactive()).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @EnumSource(AdvertisementStatus.class)
    @DisplayName("Should return proper status at isSold")
    void shouldReturnProperStatusAtIsSold(final AdvertisementStatus status) {
        // Given
        final boolean expectedValue = status == SOLD;

        final Advertisement instance =
                Advertisement.reconstitute()
                        .id(getValidIdentifier())
                        .slug(getValidSlug())
                        .title(getValidTitle())
                        .description(getValidDescription())
                        .price(getValidPrice())
                        .locality(getValidLocality())
                        .details(getValidDetails())
                        .status(status)
                        .featured(true)
                        .userId(getValidIdentifier())
                        .createdAt(Instant.now())
                        .photos(null)
                        .build();

        // When
        // Then
        Assertions.assertThat(instance.isSold()).isEqualTo(expectedValue);
    }

    private static Identifier getValidIdentifier() {
        return Identifier.generate();
    }

    private static Title getValidTitle() {
        return new Title("xyz xyz xyz");
    }

    private static Description getValidDescription() {
        return new Description("xyz xyz xyz");
    }

    private static Money getValidPrice() {
        return new Money(null, MoneyCurrency.PLN);
    }

    private static Locality getValidLocality() {
        return new Locality(Identifier.generate());
    }

    private static AdvertisementStatus getValidStatus() {
        return ACTIVE;
    }

    private static Slug getValidSlug() {
        return Slug.create(getValidTitle());
    }

    private static AdvertisementDetails<?> getValidDetails() {
        return new PlotDetails(new Area(null), PlotBuildingType.CONSTRUCTION, null);
    }
}
