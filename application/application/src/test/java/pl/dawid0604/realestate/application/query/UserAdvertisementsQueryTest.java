/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.domain.AdvertisementStatus;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

class UserAdvertisementsQueryTest {

    @Test
    @DisplayName("Should implement Query interface")
    void shouldImplementsQueryInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsQueryInterface(UserAdvertisementsQuery.class);
    }

    @ParameterizedTest
    @MethodSource("invalidStatusCollectionDataProvider")
    @DisplayName("Should set default statuses when collection is invalid")
    void shouldSetDefaultStatusesWhenCollectionIsInvalid(final Set<AdvertisementStatus> statuses) {
        // Given
        // When
        final UserAdvertisementsQuery query =
                new UserAdvertisementsQuery(UserFixture.getDummyEmail(), 0, 25, statuses);

        // Then
        Assertions.assertThat(query.statuses())
                .containsExactlyInAnyOrderElementsOf(
                        Arrays.stream(AdvertisementStatus.values()).collect(toSet()));
    }

    @Test
    @DisplayName("Should set statuses")
    void shouldSetStatuses() {
        // Given
        final Set<AdvertisementStatus> statuses =
                Set.of(AdvertisementStatus.DELETED, AdvertisementStatus.ACTIVE);

        // When
        final UserAdvertisementsQuery query =
                new UserAdvertisementsQuery(UserFixture.getDummyEmail(), 0, 25, statuses);

        // Then
        Assertions.assertThat(query.statuses()).containsExactlyInAnyOrderElementsOf(statuses);
    }

    @Test
    @DisplayName("Statuses should be immutable")
    void statusesShouldBeImmutable() {
        // Given
        final Set<AdvertisementStatus> statuses =
                Set.of(AdvertisementStatus.DELETED, AdvertisementStatus.ACTIVE);

        // When
        final UserAdvertisementsQuery query =
                new UserAdvertisementsQuery(UserFixture.getDummyEmail(), 0, 25, statuses);

        // Then
        Assertions.assertThatThrownBy(() -> query.statuses().add(AdvertisementStatus.INACTIVE))
                .isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    private static Stream<Arguments> invalidStatusCollectionDataProvider() {
        return Stream.of(Arguments.of((Set<AdvertisementStatus>) null), Arguments.of(emptySet()));
    }
}
