/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.advertisement;

import static java.util.stream.Collectors.toSet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.dto.advertisement.UserAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserCommercialAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserFlatAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserHouseAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserPlotAdvertisementCardDto;
import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.application.query.UserAdvertisementsQuery;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.port.out.AdvertisementPhotoRepository;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.Page;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserCommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserFlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserHouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserPlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;

@ExtendWith(MockitoExtension.class)
class UserAdvertisementsQueryHandlerTest {
    @Mock private AdvertisementRepository advertisementRepository;
    @Mock private AdvertisementMapper advertisementMapper;
    @Mock private AdvertisementPhotoRepository advertisementPhotoRepository;
    @Mock private LocalityRepository localityRepository;
    @Captor private ArgumentCaptor<Set<AdvertisementStatus>> advertisementStatusArgumentCaptor;
    private UserAdvertisementsQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler =
                new UserAdvertisementsQueryHandler(
                        advertisementRepository,
                        advertisementPhotoRepository,
                        localityRepository,
                        advertisementMapper);
    }

    @Test
    @DisplayName("Should throw exception when query is null")
    void shouldThrowExceptionWhenQueryIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("Query cannot be null");
    }

    @Test
    @DisplayName("Should find by and return empty page")
    void shouldFindAndReturnEmptyPage() {
        // Given
        final int pageNumber = 1;
        final int pageSize = 10;
        final long totalElements = 25;
        final Page<UserAdvertisementCardProjection> page = mock();
        final UserAdvertisementsQuery query = mock();

        given(page.getItems()).willReturn(List.of());
        given(page.getPageNumber()).willReturn(pageNumber);
        given(page.getPageSize()).willReturn(pageSize);
        given(page.getTotalElements()).willReturn(totalElements);
        given(advertisementRepository.findAdvertisementsByUser(anySet(), any(), anyInt(), anyInt()))
                .willReturn(page);

        // When
        final Page<UserAdvertisementCardDto> result = handler.handle(query);

        // Then
        Assertions.assertThat(result.getItems()).isEmpty();
        Assertions.assertThat(result.getPageNumber()).isEqualTo(pageNumber);
        Assertions.assertThat(result.getPageSize()).isEqualTo(pageSize);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(totalElements);
        verifyNoInteractions(advertisementMapper, advertisementPhotoRepository, localityRepository);
    }

    @Test
    @DisplayName("Should handle concurrent requests without errors")
    void shouldHandleConcurrentRequestsWithoutErrors() {
        // Given
        final int pageNumber = 1;
        final int pageSize = 10;
        final long totalElements = 25;
        final Page<UserAdvertisementCardProjection> page = mock();
        final UserAdvertisementsQuery query = mock();

        given(page.getItems()).willReturn(List.of());
        given(page.getPageNumber()).willReturn(pageNumber);
        given(page.getPageSize()).willReturn(pageSize);
        given(page.getTotalElements()).willReturn(totalElements);
        given(advertisementRepository.findAdvertisementsByUser(anySet(), any(), anyInt(), anyInt()))
                .willReturn(page);

        List<CompletableFuture<Page<UserAdvertisementCardDto>>> futures =
                IntStream.range(0, 50)
                        .mapToObj(i -> CompletableFuture.supplyAsync(() -> handler.handle(query)))
                        .toList();

        // When
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .until(() -> futures.stream().allMatch(CompletableFuture::isDone));

        Assertions.assertThat(futures).allSatisfy(f -> Assertions.assertThat(f.join()).isNotNull());
    }

    @ParameterizedTest
    @DisplayName("Should map statuses properly")
    @MethodSource("shouldMapStatusesProperlyDataProvider")
    void shouldMapStatusesProperly(
            final Set<String> statuses, final Set<AdvertisementStatus> expectedStatuses) {

        // Given
        final int pageNumber = 1;
        final int pageSize = 10;
        final long totalElements = 25;
        final Page<UserAdvertisementCardProjection> page = mock();
        final UserAdvertisementsQuery query = mock();

        given(query.statuses()).willReturn(statuses);
        given(page.getItems()).willReturn(List.of());
        given(page.getPageNumber()).willReturn(pageNumber);
        given(page.getPageSize()).willReturn(pageSize);
        given(page.getTotalElements()).willReturn(totalElements);
        given(advertisementRepository.findAdvertisementsByUser(anySet(), any(), anyInt(), anyInt()))
                .willReturn(page);

        // When
        handler.handle(query);

        // Then
        verify(advertisementRepository)
                .findAdvertisementsByUser(
                        advertisementStatusArgumentCaptor.capture(), any(), anyInt(), anyInt());

        Assertions.assertThat(advertisementStatusArgumentCaptor.getValue())
                .isEqualTo(expectedStatuses);
    }

    private static Stream<Arguments> shouldMapStatusesProperlyDataProvider() {
        return Stream.of(
                Arguments.of(
                        Set.of(
                                AdvertisementStatus.ACTIVE.name(),
                                AdvertisementStatus.DELETED.name()),
                        Set.of(AdvertisementStatus.ACTIVE, AdvertisementStatus.DELETED)),
                Arguments.of(
                        Arrays.stream(AdvertisementStatus.values())
                                .map(AdvertisementStatus::name)
                                .collect(toSet()),
                        Arrays.stream(AdvertisementStatus.values()).collect(toSet())));
    }

    @Test
    @DisplayName("Should find advertisements with mixed types")
    void shouldFindAdvertisementsWithMixedTypes() {
        // Given
        final int pageNumber = 1;
        final int pageSize = 10;
        final Page<UserAdvertisementCardProjection> page = mock();
        final UserAdvertisementsQuery query = mock();

        final UserFlatAdvertisementCardProjection firstCard =
                mock(UserFlatAdvertisementCardProjection.class);

        final UserFlatAdvertisementCardProjection secondCard =
                mock(UserFlatAdvertisementCardProjection.class);

        final UserHouseAdvertisementCardProjection thirdCard =
                mock(UserHouseAdvertisementCardProjection.class);

        final UserCommercialAdvertisementCardProjection forthCard =
                mock(UserCommercialAdvertisementCardProjection.class);

        final UserPlotAdvertisementCardProjection fifthCard =
                mock(UserPlotAdvertisementCardProjection.class);

        final UUID firstLocalityId = UUID.randomUUID();
        final UUID secondLocalityId = UUID.randomUUID();

        final UUID firstCardId = UUID.randomUUID();
        final UUID secondCardId = UUID.randomUUID();
        final UUID thirdCardId = UUID.randomUUID();
        final UUID forthCardId = UUID.randomUUID();
        final UUID fifthCardId = UUID.randomUUID();

        final String firstLocalityFullName = "abc";
        final String secondLocalityFullName = "cde";

        final Set<PhotoProjection> firstCardPhotos =
                Set.of(
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class));

        final Set<PhotoProjection> secondCardPhotos =
                Set.of(mock(PhotoProjection.class), mock(PhotoProjection.class));

        final Set<PhotoProjection> thirdCardPhotos =
                Set.of(mock(PhotoProjection.class), mock(PhotoProjection.class));

        final Set<PhotoProjection> forthCardPhotos = Set.of(mock(PhotoProjection.class));
        final Set<PhotoProjection> fifthCardPhotos = Set.of(mock(PhotoProjection.class));

        given(firstCard.getId()).willReturn(firstCardId);
        given(secondCard.getId()).willReturn(secondCardId);
        given(thirdCard.getId()).willReturn(thirdCardId);
        given(forthCard.getId()).willReturn(forthCardId);
        given(fifthCard.getId()).willReturn(fifthCardId);

        given(firstCard.getLocalityId()).willReturn(firstLocalityId);
        given(secondCard.getLocalityId()).willReturn(firstLocalityId);
        given(thirdCard.getLocalityId()).willReturn(firstLocalityId);

        given(forthCard.getLocalityId()).willReturn(secondLocalityId);
        given(fifthCard.getLocalityId()).willReturn(secondLocalityId);

        final List<UserAdvertisementCardProjection> items =
                List.of(firstCard, secondCard, thirdCard, forthCard, fifthCard);

        given(localityRepository.getFullNamesInBatch(Set.of(firstLocalityId, secondLocalityId)))
                .willReturn(
                        Map.of(
                                firstLocalityId, firstLocalityFullName,
                                secondLocalityId, secondLocalityFullName));

        given(
                        advertisementPhotoRepository.findPhotosInBatch(
                                Set.of(firstCardId, secondCardId), AdvertisementType.FLAT))
                .willReturn(
                        Map.of(
                                firstCardId, firstCardPhotos,
                                secondCardId, secondCardPhotos));

        given(
                        advertisementPhotoRepository.findPhotosInBatch(
                                Set.of(thirdCardId), AdvertisementType.HOUSE))
                .willReturn(Map.of(thirdCardId, thirdCardPhotos));

        given(
                        advertisementPhotoRepository.findPhotosInBatch(
                                Set.of(forthCardId), AdvertisementType.COMMERCIAL))
                .willReturn(Map.of(forthCardId, forthCardPhotos));

        given(
                        advertisementPhotoRepository.findPhotosInBatch(
                                Set.of(fifthCardId), AdvertisementType.PLOT))
                .willReturn(Map.of(fifthCardId, fifthCardPhotos));

        given(
                        advertisementMapper.toUserFlatCardDto(
                                firstCard, firstLocalityFullName, firstCardPhotos))
                .willReturn(mock(UserFlatAdvertisementCardDto.class));

        given(
                        advertisementMapper.toUserFlatCardDto(
                                secondCard, firstLocalityFullName, secondCardPhotos))
                .willReturn(mock(UserFlatAdvertisementCardDto.class));

        given(
                        advertisementMapper.toUserHouseCardDto(
                                thirdCard, firstLocalityFullName, thirdCardPhotos))
                .willReturn(mock(UserHouseAdvertisementCardDto.class));

        given(
                        advertisementMapper.toUserCommercialCardDto(
                                forthCard, secondLocalityFullName, forthCardPhotos))
                .willReturn(mock(UserCommercialAdvertisementCardDto.class));

        given(
                        advertisementMapper.toUserPlotCardDto(
                                fifthCard, secondLocalityFullName, fifthCardPhotos))
                .willReturn(mock(UserPlotAdvertisementCardDto.class));

        given(page.getItems()).willReturn(items);
        given(page.getPageNumber()).willReturn(pageNumber);
        given(page.getPageSize()).willReturn(pageSize);
        given(page.getTotalElements()).willReturn((long) items.size());
        given(advertisementRepository.findAdvertisementsByUser(anySet(), any(), anyInt(), anyInt()))
                .willReturn(page);

        // When
        final Page<UserAdvertisementCardDto> result = handler.handle(query);

        // Then
        Assertions.assertThat(result.getItems()).hasSize(items.size());
    }

    @Test
    @DisplayName("Should find advertisements with same types")
    void shouldFindAdvertisementsWithSameTypes() {
        // Given
        final int pageNumber = 1;
        final int pageSize = 10;
        final Page<UserAdvertisementCardProjection> page = mock();
        final UserAdvertisementsQuery query = mock();

        final UserFlatAdvertisementCardProjection firstCard =
                mock(UserFlatAdvertisementCardProjection.class);

        final UserFlatAdvertisementCardProjection secondCard =
                mock(UserFlatAdvertisementCardProjection.class);

        final UUID firstLocalityId = UUID.randomUUID();
        final UUID secondLocalityId = UUID.randomUUID();

        final UUID firstCardId = UUID.randomUUID();
        final UUID secondCardId = UUID.randomUUID();

        final String firstLocalityFullName = "abc";
        final String secondLocalityFullName = "cde";

        final Set<PhotoProjection> firstCardPhotos =
                Set.of(
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class));

        final Set<PhotoProjection> secondCardPhotos =
                Set.of(mock(PhotoProjection.class), mock(PhotoProjection.class));

        given(firstCard.getId()).willReturn(firstCardId);
        given(secondCard.getId()).willReturn(secondCardId);

        given(firstCard.getLocalityId()).willReturn(firstLocalityId);
        given(secondCard.getLocalityId()).willReturn(secondLocalityId);
        final List<UserAdvertisementCardProjection> items = List.of(firstCard, secondCard);

        given(localityRepository.getFullNamesInBatch(Set.of(firstLocalityId, secondLocalityId)))
                .willReturn(
                        Map.of(
                                firstLocalityId, firstLocalityFullName,
                                secondLocalityId, secondLocalityFullName));

        given(
                        advertisementPhotoRepository.findPhotosInBatch(
                                Set.of(firstCardId, secondCardId), AdvertisementType.FLAT))
                .willReturn(
                        Map.of(
                                firstCardId, firstCardPhotos,
                                secondCardId, secondCardPhotos));

        given(
                        advertisementMapper.toUserFlatCardDto(
                                firstCard, firstLocalityFullName, firstCardPhotos))
                .willReturn(mock(UserFlatAdvertisementCardDto.class));

        given(
                        advertisementMapper.toUserFlatCardDto(
                                secondCard, secondLocalityFullName, secondCardPhotos))
                .willReturn(mock(UserFlatAdvertisementCardDto.class));

        given(page.getItems()).willReturn(items);
        given(page.getPageNumber()).willReturn(pageNumber);
        given(page.getPageSize()).willReturn(pageSize);
        given(page.getTotalElements()).willReturn((long) items.size());
        given(advertisementRepository.findAdvertisementsByUser(anySet(), any(), anyInt(), anyInt()))
                .willReturn(page);

        // When
        final Page<UserAdvertisementCardDto> result = handler.handle(query);

        // Then
        Assertions.assertThat(result.getItems()).hasSize(items.size());
    }
}
