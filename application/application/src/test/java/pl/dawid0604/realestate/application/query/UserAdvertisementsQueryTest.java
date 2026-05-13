/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidPageNumber;
import pl.dawid0604.realestate.application.validation.ValidPageSize;
import pl.dawid0604.realestate.domain.AdvertisementStatus;

class UserAdvertisementsQueryTest {

    @Test
    @DisplayName("Should implement Query interface")
    void shouldImplementsQueryInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsQueryInterface(UserAdvertisementsQuery.class);
    }

    @Test
    @DisplayName("Should have username field with required annotations")
    void shouldHaveEmailFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("username", List.of(ValidEmail.class));
    }

    @Test
    @DisplayName("Should have page field with required annotations")
    void shouldHavePageFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("page", List.of(ValidPageNumber.class));
    }

    @Test
    @DisplayName("Should have page size field with required annotations")
    void shouldHavePageSizeFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("pageSize", List.of(ValidPageSize.class));
    }

    @ParameterizedTest
    @MethodSource("invalidStatusCollectionDataProvider")
    @DisplayName("Should set default statuses when collection is invalid")
    void shouldSetDefaultStatusesWhenCollectionIsInvalid(final Set<String> statuses) {
        // Given
        // When
        final UserAdvertisementsQuery query =
                new UserAdvertisementsQuery(UserFixture.getDummyEmail(), 0, 25, statuses);

        // Then
        Assertions.assertThat(query.statuses())
                .containsExactlyInAnyOrderElementsOf(
                        Arrays.stream(AdvertisementStatus.values())
                                .map(AdvertisementStatus::name)
                                .collect(toSet()));
    }

    @Test
    @DisplayName("Should set statuses")
    void shouldSetStatuses() {
        // Given
        final Set<String> statuses = Set.of("a", "b", "c");

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
        final Set<String> statuses = Set.of("a", "b", "c");

        // When
        final UserAdvertisementsQuery query =
                new UserAdvertisementsQuery(UserFixture.getDummyEmail(), 0, 25, statuses);

        // Then
        Assertions.assertThatThrownBy(() -> query.statuses().add("o"))
                .isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    private static Stream<Arguments> invalidStatusCollectionDataProvider() {
        return Stream.of(Arguments.of((Set<String>) null), Arguments.of(emptySet()));
    }

    private static void assertFieldAnnotations(
            final String fieldName, final List<Class<? extends Annotation>> requiredAnnotations) {

        AnnotationAssertions.assertFieldAnnotations(
                UserAdvertisementsQuery.class, fieldName, requiredAnnotations);
    }
}
