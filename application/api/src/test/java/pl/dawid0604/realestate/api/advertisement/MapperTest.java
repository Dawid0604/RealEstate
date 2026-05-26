/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.api.advertisement.request.AdvertisementPhotoRequest;
import pl.dawid0604.realestate.application.command.CreateAdvertisementCommand.AdvertisementPhoto;
import pl.dawid0604.realestate.domain.AdvertisementStatus;

import java.util.Set;

class MapperTest {

    @Nested
    final class MapEnumCollectionToSetTests {

        @Test
        @DisplayName("Should return empty collection when input is null")
        void shouldReturnEmptyCollectionWhenInputIsNull() {
            // Given
            // When
            final Set<String> result = Mapper.mapEnumCollectionToSet(null);

            // Then
            Assertions.assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should map properly")
        void shouldMapProperly() {
            // Given
            final Set<AdvertisementStatus> statuses =
                    Set.of(AdvertisementStatus.DELETED, AdvertisementStatus.ACTIVE);

            // When
            final Set<String> result = Mapper.mapEnumCollectionToSet(statuses);

            // Then
            Assertions.assertThat(result)
                    .hasSize(statuses.size())
                    .containsExactlyInAnyOrder(
                            AdvertisementStatus.DELETED.name(), AdvertisementStatus.ACTIVE.name());
        }
    }

    @Nested
    final class MapPhotosTests {

        @Test
        @DisplayName("Should return empty collection when input is null")
        void shouldReturnEmptyCollectionWhenInputIsNull() {
            // Given
            // When
            final var result = Mapper.mapPhotos(null);

            // Then
            Assertions.assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should map properly")
        void shouldMapProperly() {
            // Given
            final Set<AdvertisementPhotoRequest> photos =
                    Set.of(
                            new AdvertisementPhotoRequest("anyUrl", 1),
                            new AdvertisementPhotoRequest("anyUrl2", 2));

            // When
            final var result = Mapper.mapPhotos(photos);

            // Then
            final Tuple[] tuples =
                    photos.stream()
                            .map(p -> Tuple.tuple(p.url(), p.position()))
                            .toArray(Tuple[]::new);

            Assertions.assertThat(result)
                    .hasSize(photos.size())
                    .extracting(AdvertisementPhoto::url, AdvertisementPhoto::position)
                    .containsExactlyInAnyOrder(tuples);
        }
    }
}
