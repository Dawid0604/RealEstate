/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.advertisement;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.CommercialAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.FlatAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.HouseAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementCardDto;
import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.application.query.SearchAdvertisementsQuery;
import pl.dawid0604.realestate.application.query.SearchCommercialAdvertisementsQuery;
import pl.dawid0604.realestate.application.query.SearchFlatAdvertisementsQuery;
import pl.dawid0604.realestate.application.query.SearchHouseAdvertisementsQuery;
import pl.dawid0604.realestate.application.query.SearchPlotAdvertisementsQuery;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.port.out.AdvertisementPhotoRepository;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.Page;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchFlatAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;

@ExtendWith(MockitoExtension.class)
class SearchAdvertisementQueryHandlerTest {
    @Mock private AdvertisementRepository advertisementRepository;
    @Mock private AdvertisementMapper advertisementMapper;
    @Mock private AdvertisementPhotoRepository advertisementPhotoRepository;
    @Mock private LocalityRepository localityRepository;
    @Mock private UserRepository userRepository;
    private SearchAdvertisementQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler =
                new SearchAdvertisementQueryHandler(
                        advertisementRepository,
                        advertisementMapper,
                        advertisementPhotoRepository,
                        localityRepository,
                        userRepository);
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
    @DisplayName("Should find by criteria and return empty page")
    void shouldFindByCriteriaAndReturnEmptyPage() {
        // Given
        final int pageNumber = 1;
        final int pageSize = 10;
        final long totalElements = 25;
        final Page<AdvertisementCardProjection> page = mock();
        final SearchFlatAdvertisementsQuery query = mock();

        given(page.getItems()).willReturn(List.of());
        given(page.getPageNumber()).willReturn(pageNumber);
        given(page.getPageSize()).willReturn(pageSize);
        given(page.getTotalElements()).willReturn(totalElements);

        given(query.criteria()).willReturn(mock(SearchFlatAdvertisementsCriteria.class));
        given(advertisementRepository.findByCriteria(query.criteria())).willReturn(page);

        // When
        final Page<AdvertisementCardDto> result = handler.handle(query);

        // Then
        Assertions.assertThat(result.getItems()).isEmpty();
        Assertions.assertThat(result.getPageNumber()).isEqualTo(pageNumber);
        Assertions.assertThat(result.getPageSize()).isEqualTo(pageSize);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(totalElements);
        verifyNoInteractions(
                advertisementMapper,
                advertisementPhotoRepository,
                localityRepository,
                userRepository);
    }

    @Test
    @DisplayName("Should handle concurrent requests without errors")
    void shouldHandleConcurrentRequestsWithoutErrors() {
        // Given
        final int pageNumber = 1;
        final int pageSize = 10;
        final long totalElements = 25;
        final Page<AdvertisementCardProjection> page = mock();
        final SearchFlatAdvertisementsQuery query = mock();

        given(page.getItems()).willReturn(List.of());
        given(page.getPageNumber()).willReturn(pageNumber);
        given(page.getPageSize()).willReturn(pageSize);
        given(page.getTotalElements()).willReturn(totalElements);

        given(query.criteria()).willReturn(mock(SearchFlatAdvertisementsCriteria.class));
        given(advertisementRepository.findByCriteria(query.criteria())).willReturn(page);

        List<CompletableFuture<Page<AdvertisementCardDto>>> futures =
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
    @DisplayName("Should find advertisements by criteria and return filled page")
    @MethodSource("shouldFindAdvertisementsByCriteriaAndReturnFilledPageDataProvider")
    void shouldFindAdvertisementsByCriteriaAndReturnFilledPage(
            final AdvertisementType advertisementType,
            final SearchAdvertisementsQuery query,
            final AdvertisementCardProjection firstCard,
            final AdvertisementCardProjection secondCard) {

        // Given
        final Page<AdvertisementCardProjection> page = mock();
        final List<AdvertisementCardProjection> items = List.of(firstCard, secondCard);

        final UUID firstCardId = UUID.randomUUID();
        final UUID firstCardLocalityId = UUID.randomUUID();
        final String firstCardLocalityFullName = "abc";
        final Set<PhotoProjection> firstCardPhotos = Set.of();

        final UUID firstCardUserId = UUID.randomUUID();
        final UserType firstCardUserType = UserType.AGENCY;

        final UUID secondCardUserId = UUID.randomUUID();
        final UserType secondCardUserType = UserType.DEVELOPER;

        final UUID secondCardId = UUID.randomUUID();
        final UUID secondCardLocalityId = UUID.randomUUID();
        final String secondCardLocalityFullName = "cde";
        final Set<PhotoProjection> secondCardPhotos =
                Set.of(
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class));

        given(firstCard.getLocalityId()).willReturn(firstCardLocalityId);
        given(firstCard.getId()).willReturn(firstCardId);
        given(firstCard.getUserId()).willReturn(firstCardUserId);

        given(secondCard.getLocalityId()).willReturn(secondCardLocalityId);
        given(secondCard.getId()).willReturn(secondCardId);
        given(secondCard.getUserId()).willReturn(secondCardUserId);

        given(query.criteria()).willReturn(mock(SearchFlatAdvertisementsCriteria.class));
        given(advertisementRepository.findByCriteria(query.criteria())).willReturn(page);

        given(page.getItems()).willReturn(items);
        given(page.getPageNumber()).willReturn(1);
        given(page.getPageSize()).willReturn(25);
        given(page.getTotalElements()).willReturn((long) items.size());

        given(
                        localityRepository.getFullNamesInBatch(
                                Set.of(firstCardLocalityId, secondCardLocalityId)))
                .willReturn(
                        Map.of(
                                firstCardLocalityId, firstCardLocalityFullName,
                                secondCardLocalityId, secondCardLocalityFullName));

        given(
                        advertisementPhotoRepository.findPhotosInBatch(
                                Set.of(firstCardId, secondCardId), advertisementType))
                .willReturn(
                        Map.of(
                                firstCardId, firstCardPhotos,
                                secondCardId, secondCardPhotos));

        given(userRepository.getUserTypesInBatch(Set.of(firstCardUserId, secondCardUserId)))
                .willReturn(
                        Map.of(
                                firstCardUserId, firstCardUserType,
                                secondCardUserId, secondCardUserType));

        switch (advertisementType) {
            case FLAT -> {
                given(
                                advertisementMapper.toFlatCardDto(
                                        (FlatAdvertisementCardProjection) firstCard,
                                        firstCardLocalityFullName,
                                        firstCardPhotos,
                                        firstCardUserType))
                        .willReturn(mock(FlatAdvertisementCardDto.class));

                given(
                                advertisementMapper.toFlatCardDto(
                                        (FlatAdvertisementCardProjection) secondCard,
                                        secondCardLocalityFullName,
                                        secondCardPhotos,
                                        secondCardUserType))
                        .willReturn(mock(FlatAdvertisementCardDto.class));
            }

            case HOUSE -> {
                given(
                                advertisementMapper.toHouseCardDto(
                                        (HouseAdvertisementCardProjection) firstCard,
                                        firstCardLocalityFullName,
                                        firstCardPhotos,
                                        firstCardUserType))
                        .willReturn(mock(HouseAdvertisementCardDto.class));

                given(
                                advertisementMapper.toHouseCardDto(
                                        (HouseAdvertisementCardProjection) secondCard,
                                        secondCardLocalityFullName,
                                        secondCardPhotos,
                                        secondCardUserType))
                        .willReturn(mock(HouseAdvertisementCardDto.class));
            }

            case COMMERCIAL -> {
                given(
                                advertisementMapper.toCommercialCardDto(
                                        (CommercialAdvertisementCardProjection) firstCard,
                                        firstCardLocalityFullName,
                                        firstCardPhotos,
                                        firstCardUserType))
                        .willReturn(mock(CommercialAdvertisementCardDto.class));

                given(
                                advertisementMapper.toCommercialCardDto(
                                        (CommercialAdvertisementCardProjection) secondCard,
                                        secondCardLocalityFullName,
                                        secondCardPhotos,
                                        secondCardUserType))
                        .willReturn(mock(CommercialAdvertisementCardDto.class));
            }

            case PLOT -> {
                given(
                                advertisementMapper.toPlotCardDto(
                                        (PlotAdvertisementCardProjection) firstCard,
                                        firstCardLocalityFullName,
                                        firstCardPhotos,
                                        firstCardUserType))
                        .willReturn(mock(PlotAdvertisementCardDto.class));

                given(
                                advertisementMapper.toPlotCardDto(
                                        (PlotAdvertisementCardProjection) secondCard,
                                        secondCardLocalityFullName,
                                        secondCardPhotos,
                                        secondCardUserType))
                        .willReturn(mock(PlotAdvertisementCardDto.class));
            }
        }

        // When
        final Page<AdvertisementCardDto> result = handler.handle(query);

        // Then
        Assertions.assertThat(result.getItems()).hasSize(items.size());
    }

    private static Stream<Arguments>
            shouldFindAdvertisementsByCriteriaAndReturnFilledPageDataProvider() {

        return Arrays.stream(AdvertisementType.values())
                .map(
                        type ->
                                switch (type) {
                                    case FLAT ->
                                            Arguments.of(
                                                    AdvertisementType.FLAT,
                                                    mock(SearchFlatAdvertisementsQuery.class),
                                                    mock(FlatAdvertisementCardProjection.class),
                                                    mock(FlatAdvertisementCardProjection.class));

                                    case HOUSE ->
                                            Arguments.of(
                                                    AdvertisementType.HOUSE,
                                                    mock(SearchHouseAdvertisementsQuery.class),
                                                    mock(HouseAdvertisementCardProjection.class),
                                                    mock(HouseAdvertisementCardProjection.class));

                                    case COMMERCIAL ->
                                            Arguments.of(
                                                    AdvertisementType.COMMERCIAL,
                                                    mock(SearchCommercialAdvertisementsQuery.class),
                                                    mock(
                                                            CommercialAdvertisementCardProjection
                                                                    .class),
                                                    mock(
                                                            CommercialAdvertisementCardProjection
                                                                    .class));

                                    case PLOT ->
                                            Arguments.of(
                                                    AdvertisementType.PLOT,
                                                    mock(SearchPlotAdvertisementsQuery.class),
                                                    mock(PlotAdvertisementCardProjection.class),
                                                    mock(PlotAdvertisementCardProjection.class));
                                });
    }
}
