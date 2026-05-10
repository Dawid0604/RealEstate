/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import static java.util.Collections.emptySet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchFlatAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserFlatAdvertisementCardProjection;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

class AdvertisementAdapterTest {

    @Nested
    final class SaveTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private AdvertisementMapper advertisementMapper;
            @Mock private AdvertisementJpaRepository advertisementJpaRepository;
            @Captor private ArgumentCaptor<AdvertisementEntity<?, ?>> argumentCaptor;
            private AdvertisementAdapter advertisementAdapter;

            @BeforeEach
            void setUp() {
                advertisementAdapter =
                        new AdvertisementAdapter(advertisementMapper, advertisementJpaRepository);
            }

            @Test
            @DisplayName("Should save flat advertisement")
            void shouldSaveFlatAdvertisement() {
                // Given
                final Advertisement advertisement = mock(Advertisement.class);
                final FlatAdvertisementEntity expectedEntity = mock();

                given(advertisement.getAdvertisementType()).willReturn(AdvertisementType.FLAT);
                given(advertisementMapper.toFlatEntity(advertisement)).willReturn(expectedEntity);

                // When
                advertisementAdapter.save(advertisement);

                // Then
                verify(advertisementJpaRepository).save(argumentCaptor.capture());
                Assertions.assertThat(argumentCaptor.getValue()).isEqualTo(expectedEntity);
            }

            @Test
            @DisplayName("Should save house advertisement")
            void shouldSaveHouseAdvertisement() {
                // Given
                final Advertisement advertisement = mock(Advertisement.class);
                final HouseAdvertisementEntity expectedEntity = mock();

                given(advertisement.getAdvertisementType()).willReturn(AdvertisementType.HOUSE);
                given(advertisementMapper.toHouseEntity(advertisement)).willReturn(expectedEntity);

                // When
                advertisementAdapter.save(advertisement);

                // Then
                verify(advertisementJpaRepository).save(argumentCaptor.capture());
                Assertions.assertThat(argumentCaptor.getValue()).isEqualTo(expectedEntity);
            }

            @Test
            @DisplayName("Should save commercial advertisement")
            void shouldSaveCommercialAdvertisement() {
                // Given
                final Advertisement advertisement = mock(Advertisement.class);
                final CommercialAdvertisementEntity expectedEntity = mock();

                given(advertisement.getAdvertisementType())
                        .willReturn(AdvertisementType.COMMERCIAL);

                given(advertisementMapper.toCommercialEntity(advertisement))
                        .willReturn(expectedEntity);

                // When
                advertisementAdapter.save(advertisement);

                // Then
                verify(advertisementJpaRepository).save(argumentCaptor.capture());
                Assertions.assertThat(argumentCaptor.getValue()).isEqualTo(expectedEntity);
            }

            @Test
            @DisplayName("Should save plot advertisement")
            void shouldSavePlotAdvertisement() {
                // Given
                final Advertisement advertisement = mock(Advertisement.class);
                final PlotAdvertisementEntity expectedEntity = mock();

                given(advertisement.getAdvertisementType()).willReturn(AdvertisementType.PLOT);
                given(advertisementMapper.toPlotEntity(advertisement)).willReturn(expectedEntity);

                // When
                advertisementAdapter.save(advertisement);

                // Then
                verify(advertisementJpaRepository).save(argumentCaptor.capture());
                Assertions.assertThat(argumentCaptor.getValue()).isEqualTo(expectedEntity);
            }
        }
    }

    @Nested
    final class FindBySlugTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private AdvertisementMapper advertisementMapper;
            @Mock private AdvertisementJpaRepository advertisementJpaRepository;
            private AdvertisementAdapter advertisementAdapter;

            @BeforeEach
            void setUp() {
                advertisementAdapter =
                        new AdvertisementAdapter(advertisementMapper, advertisementJpaRepository);
            }

            @ParameterizedTest
            @NullAndEmptySource
            @DisplayName("Should throw exception when slug is blank")
            void shouldThrowExceptionWhenSlugIsBlank(final String slug) {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () -> advertisementAdapter.findBySlug(slug, AdvertisementType.FLAT))
                        .isExactlyInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Slug cannot be blank");
            }

            @Test
            @DisplayName("Should throw exception when advertisementType is null")
            void shouldThrowExceptionWhenAdvertisementTypeIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () -> advertisementAdapter.findBySlug("any-slug", null))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("AdvertisementType cannot be null");
            }

            @ParameterizedTest
            @EnumSource(AdvertisementType.class)
            @DisplayName("Should return advertisement by slug and type")
            void shouldReturnAdvertisementBySlugAndType(final AdvertisementType advertisementType) {
                // Given
                final String slug = "any-slug";
                final AdvertisementEntity<?, ?> entity = mock(FlatAdvertisementEntity.class);
                final Advertisement domain = mock(Advertisement.class);

                given(advertisementJpaRepository.findBySlug(slug, advertisementType))
                        .willReturn(Optional.of(entity));

                given(advertisementMapper.toDomain(entity)).willReturn(domain);

                // When
                final var result = advertisementAdapter.findBySlug(slug, advertisementType);

                // Then
                Assertions.assertThat(result).isPresent().contains(domain);
            }
        }
    }

    @Nested
    final class FindDetailsTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private AdvertisementMapper advertisementMapper;
            @Mock private AdvertisementJpaRepository advertisementJpaRepository;
            private AdvertisementAdapter advertisementAdapter;

            @BeforeEach
            void setUpBeforeEach() {
                advertisementAdapter =
                        new AdvertisementAdapter(advertisementMapper, advertisementJpaRepository);
            }

            @ParameterizedTest
            @NullAndEmptySource
            @DisplayName("Should throw exception when slug is blank")
            void shouldThrowExceptionWhenSlugIsBlank(final String slug) {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        advertisementAdapter.findDetails(
                                                slug, AdvertisementType.FLAT))
                        .isExactlyInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Slug cannot be blank");
            }

            @Test
            @DisplayName("Should throw exception when advertisementType is null")
            void shouldThrowExceptionWhenAdvertisementTypeIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () -> advertisementAdapter.findDetails("any-slug", null))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("AdvertisementType cannot be null");
            }

            @ParameterizedTest
            @EnumSource(AdvertisementType.class)
            @DisplayName("Should find details by slug and type")
            void shouldFindDetailsBySlugAndType(final AdvertisementType advertisementType) {
                // Given
                final String slug = "any-slug";
                final AdvertisementDetailsProjection projection =
                        mock(FlatAdvertisementDetailsProjection.class);

                given(advertisementJpaRepository.findDetails(slug, advertisementType))
                        .willReturn(Optional.of(projection));

                // When
                final var result = advertisementAdapter.findDetails(slug, advertisementType);

                // Then
                Assertions.assertThat(result).isPresent().contains(projection);
                verifyNoInteractions(advertisementMapper);
            }
        }
    }

    @Nested
    final class FindClaimsTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private AdvertisementMapper advertisementMapper;
            @Mock private AdvertisementJpaRepository advertisementJpaRepository;
            private AdvertisementAdapter advertisementAdapter;

            @BeforeEach
            void setUpBeforeEach() {
                advertisementAdapter =
                        new AdvertisementAdapter(advertisementMapper, advertisementJpaRepository);
            }

            @Test
            @DisplayName("Should throw exception when id is null")
            void shouldThrowExceptionWhenIdIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () -> advertisementAdapter.findClaims(null, AdvertisementType.FLAT))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("Id cannot be null");
            }

            @Test
            @DisplayName("Should throw exception when advertisementType is null")
            void shouldThrowExceptionWhenAdvertisementTypeIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () -> advertisementAdapter.findClaims(UUID.randomUUID(), null))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("AdvertisementType cannot be null");
            }

            @ParameterizedTest
            @EnumSource(AdvertisementType.class)
            @DisplayName("Should find details by id and type")
            void shouldFindClaimsByIdAndType(final AdvertisementType advertisementType) {
                // Given
                final UUID id = UUID.randomUUID();
                final AdvertisementClaimProjection projection =
                        mock(AdvertisementClaimProjection.class);

                given(advertisementJpaRepository.findClaims(id, advertisementType))
                        .willReturn(Set.of(projection));

                // When
                final var result = advertisementAdapter.findClaims(id, advertisementType);

                // Then
                Assertions.assertThat(result).containsExactly(projection);
                verifyNoInteractions(advertisementMapper);
            }
        }
    }

    @Nested
    final class FindAdvertisementsByUserTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private AdvertisementMapper advertisementMapper;
            @Mock private AdvertisementJpaRepository advertisementJpaRepository;
            private AdvertisementAdapter advertisementAdapter;

            @BeforeEach
            void setUpBeforeEach() {
                advertisementAdapter =
                        new AdvertisementAdapter(advertisementMapper, advertisementJpaRepository);
            }

            @Test
            @DisplayName("Should throw exception when userId is null")
            void shouldThrowExceptionWhenUserIdIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(
                                () ->
                                        advertisementAdapter.findAdvertisementsByUser(
                                                emptySet(), null, 0, 1))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("UserId cannot be null");
            }

            @ParameterizedTest
            @MethodSource("shouldFindAdvertisementsDataProvider")
            @DisplayName("Should find advertisements with statuses")
            void shouldFindAdvertisements(final Set<AdvertisementStatus> statuses) {
                // Given
                final int page = 1;
                final int pageSize = 25;
                final UUID userId = UUID.randomUUID();
                final PageImpl<UserAdvertisementCardProjection> pageResult = mock();
                final UserFlatAdvertisementCardProjection projection = mock();

                given(
                                advertisementJpaRepository.findAdvertisementsByUser(
                                        statuses, userId, page, pageSize))
                        .willReturn(pageResult);

                given(pageResult.getContent()).willReturn(List.of(projection));
                given(pageResult.getNumber()).willReturn(page);
                given(pageResult.getSize()).willReturn(pageSize);
                given(pageResult.getTotalElements()).willReturn(1L);

                // When
                final var result =
                        advertisementAdapter.findAdvertisementsByUser(
                                statuses, userId, page, pageSize);

                // Then
                Assertions.assertThat(result.getItems()).containsExactly(projection);
                Assertions.assertThat(result.getPageNumber()).isEqualTo(page);
                Assertions.assertThat(result.getPageSize()).isEqualTo(pageSize);
                Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
                verifyNoInteractions(advertisementMapper);
            }

            private static Stream<Arguments> shouldFindAdvertisementsDataProvider() {
                return Stream.of(
                        Arguments.of((Set<AdvertisementStatus>) null),
                        Arguments.of(emptySet()),
                        Arguments.of(Set.of(AdvertisementStatus.ACTIVE)),
                        Arguments.of(
                                Set.of(AdvertisementStatus.DELETED, AdvertisementStatus.SOLD)));
            }
        }
    }

    @Nested
    final class FindByCriteriaTests {

        @Nested
        @ExtendWith(MockitoExtension.class)
        final class UnitTests {
            @Mock private AdvertisementMapper advertisementMapper;
            @Mock private AdvertisementJpaRepository advertisementJpaRepository;
            private AdvertisementAdapter advertisementAdapter;

            @BeforeEach
            void setUpBeforeEach() {
                advertisementAdapter =
                        new AdvertisementAdapter(advertisementMapper, advertisementJpaRepository);
            }

            @Test
            @DisplayName("Should throw exception when criteria is null")
            void shouldThrowExceptionWhenCriteriaIsNull() {
                // Given
                // When
                // Then
                Assertions.assertThatThrownBy(() -> advertisementAdapter.findByCriteria(null))
                        .isExactlyInstanceOf(NullPointerException.class)
                        .hasMessage("Criteria cannot be null");
            }

            @Test
            @DisplayName("Should find advertisements by criteria")
            void shouldFindAdvertisementsByCriteria() {
                // Given
                final int page = 1;
                final int pageSize = 25;
                final SearchFlatAdvertisementsCriteria criteria = mock();
                final PageImpl<AdvertisementCardProjection> pageResult = mock();
                final FlatAdvertisementCardProjection projection = mock();

                given(advertisementJpaRepository.findByCriteria(criteria)).willReturn(pageResult);
                given(pageResult.getContent()).willReturn(List.of(projection));
                given(pageResult.getNumber()).willReturn(page);
                given(pageResult.getSize()).willReturn(pageSize);
                given(pageResult.getTotalElements()).willReturn(1L);

                // When
                final var result = advertisementAdapter.findByCriteria(criteria);

                // Then
                Assertions.assertThat(result.getItems()).containsExactly(projection);
                Assertions.assertThat(result.getPageNumber()).isEqualTo(page);
                Assertions.assertThat(result.getPageSize()).isEqualTo(pageSize);
                Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
                verifyNoInteractions(advertisementMapper);
            }
        }
    }
}
