/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.advertisement;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.CommercialAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.FlatAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.HouseAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.application.query.AdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.CommercialAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.FlatAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.HouseAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.PlotAdvertisementDetailsQuery;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.port.out.PhotoRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;
import pl.dawid0604.realestate.domain.shared.user.projection.AdvertisementUserProjection;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class AdvertisementDetailsQueryHandlerTest {
    @Mock private AdvertisementRepository advertisementRepository;
    @Mock private AdvertisementMapper advertisementMapper;
    @Mock private PhotoRepository photoRepository;
    @Mock private LocalityRepository localityRepository;
    @Mock private UserRepository userRepository;
    private AdvertisementDetailsQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler =
                new AdvertisementDetailsQueryHandler(
                        advertisementRepository,
                        photoRepository,
                        localityRepository,
                        userRepository,
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
    @DisplayName("Should throw exception when advertisement not found")
    void shouldThrowExceptionWhenAdvertisementNotFound() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> handler.handle(new FlatAdvertisementDetailsQuery("abc")))
                .isExactlyInstanceOf(AdvertisementNotFoundException.class);
    }

    @Test
    @DisplayName("Should handle concurrent requests without errors")
    void shouldHandleConcurrentRequestsWithoutErrors() {
        // Given
        final FlatAdvertisementDetailsQuery query = mock();
        final AdvertisementUserProjection user = mock();
        final UUID localityId = UUID.randomUUID();
        final UUID detailsId = UUID.randomUUID();
        final String localityFullName = "xde";
        final FlatAdvertisementDetailsProjection advertisementDetails = mock();

        given(advertisementDetails.getLocalityId()).willReturn(localityId);
        given(advertisementDetails.getId()).willReturn(detailsId);
        given(advertisementDetails.getOwnerEmail()).willReturn(UserFixture.getDummyEmail());

        given(advertisementRepository.findDetails(query.slug(), AdvertisementType.FLAT))
                .willReturn(Optional.of(advertisementDetails));

        given(advertisementRepository.findClaims(detailsId, AdvertisementType.FLAT))
                .willReturn(Set.of());

        given(localityRepository.getFullNamesInBatch(Set.of(localityId)))
                .willReturn(Map.of(localityId, localityFullName));

        given(userRepository.findAdvertisementUser(advertisementDetails.getOwnerEmail()))
                .willReturn(Optional.of(user));

        given(
                        photoRepository.findAdvertisementsPhotosInBatch(
                                List.of(detailsId), AdvertisementType.FLAT))
                .willReturn(Map.of(detailsId, Set.of()));

        given(
                        advertisementMapper.toFlatDetailsDto(
                                advertisementDetails, localityFullName, Set.of(), Set.of(), user))
                .willReturn(mock(FlatAdvertisementDetailsDto.class));

        final List<CompletableFuture<AdvertisementDetailsDto>> futures =
                IntStream.range(0, 50)
                        .mapToObj(i -> CompletableFuture.supplyAsync(() -> handler.handle(query)))
                        .toList();

        // When
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .until(() -> futures.stream().allMatch(CompletableFuture::isDone));

        Assertions.assertThat(futures).allSatisfy(f -> Assertions.assertThat(f.join()).isNotNull());
    }

    @Test
    @DisplayName("Should throw exception when owner not found")
    void shouldThrowExceptionWhenOwnerNotFound() {
        // Given
        final FlatAdvertisementDetailsQuery query = mock();
        final UUID localityId = UUID.randomUUID();
        final UUID detailsId = UUID.randomUUID();
        final String localityFullName = "xde";
        final FlatAdvertisementDetailsProjection advertisementDetails = mock();

        given(advertisementDetails.getLocalityId()).willReturn(localityId);
        given(advertisementDetails.getId()).willReturn(detailsId);
        given(advertisementDetails.getOwnerEmail()).willReturn(UserFixture.getDummyEmail());

        given(advertisementRepository.findDetails(query.slug(), AdvertisementType.FLAT))
                .willReturn(Optional.of(advertisementDetails));

        given(advertisementRepository.findClaims(detailsId, AdvertisementType.FLAT))
                .willReturn(Set.of());

        given(localityRepository.getFullNamesInBatch(Set.of(localityId)))
                .willReturn(Map.of(localityId, localityFullName));

        given(
                        photoRepository.findAdvertisementsPhotosInBatch(
                                List.of(detailsId), AdvertisementType.FLAT))
                .willReturn(Map.of(detailsId, Set.of()));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(query))
                .isExactlyInstanceOf(CompletionException.class);

        verify(userRepository).findAdvertisementUser(advertisementDetails.getOwnerEmail());
    }

    @ParameterizedTest
    @DisplayName("Should find details")
    @MethodSource("shouldFindDetailsDataProvider")
    void shouldFindDetails(
            final AdvertisementDetailsQuery query,
            final AdvertisementType advertisementType,
            final AdvertisementDetailsProjection advertisementDetails) {

        // Given
        final AdvertisementUserProjection user = mock();
        final UUID localityId = UUID.randomUUID();
        final UUID detailsId = UUID.randomUUID();
        final String localityFullName = "xde";
        final Set<AdvertisementClaimProjection> claims =
                Set.of(
                        mock(AdvertisementClaimProjection.class),
                        mock(AdvertisementClaimProjection.class),
                        mock(AdvertisementClaimProjection.class),
                        mock(AdvertisementClaimProjection.class));

        final Set<PhotoProjection> photos =
                Set.of(
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class),
                        mock(PhotoProjection.class));

        given(advertisementDetails.getLocalityId()).willReturn(localityId);
        given(advertisementDetails.getId()).willReturn(detailsId);
        given(advertisementDetails.getOwnerEmail()).willReturn(UserFixture.getDummyEmail());

        given(advertisementRepository.findDetails(query.slug(), advertisementType))
                .willReturn(Optional.of(advertisementDetails));

        given(userRepository.findAdvertisementUser(advertisementDetails.getOwnerEmail()))
                .willReturn(Optional.of(user));

        given(advertisementRepository.findClaims(detailsId, advertisementType)).willReturn(claims);
        given(localityRepository.getFullNamesInBatch(Set.of(localityId)))
                .willReturn(Map.of(localityId, localityFullName));

        given(
                        photoRepository.findAdvertisementsPhotosInBatch(
                                List.of(detailsId), advertisementType))
                .willReturn(Map.of(detailsId, photos));

        switch (advertisementType) {
            case FLAT ->
                    given(
                                    advertisementMapper.toFlatDetailsDto(
                                            (FlatAdvertisementDetailsProjection)
                                                    advertisementDetails,
                                            localityFullName,
                                            photos,
                                            claims,
                                            user))
                            .willReturn(mock(FlatAdvertisementDetailsDto.class));

            case HOUSE ->
                    given(
                                    advertisementMapper.toHouseDetailsDto(
                                            (HouseAdvertisementDetailsProjection)
                                                    advertisementDetails,
                                            localityFullName,
                                            photos,
                                            claims,
                                            user))
                            .willReturn(mock(HouseAdvertisementDetailsDto.class));

            case COMMERCIAL ->
                    given(
                                    advertisementMapper.toCommercialDetailsDto(
                                            (CommercialAdvertisementDetailsProjection)
                                                    advertisementDetails,
                                            localityFullName,
                                            photos,
                                            claims,
                                            user))
                            .willReturn(mock(CommercialAdvertisementDetailsDto.class));

            case PLOT ->
                    given(
                                    advertisementMapper.toPlotDetailsDto(
                                            (PlotAdvertisementDetailsProjection)
                                                    advertisementDetails,
                                            localityFullName,
                                            photos,
                                            claims,
                                            user))
                            .willReturn(mock(PlotAdvertisementDetailsDto.class));
        }

        // When
        final AdvertisementDetailsDto result = handler.handle(query);

        // Then
        Assertions.assertThat(result).isNotNull();
    }

    private static Stream<Arguments> shouldFindDetailsDataProvider() {
        return Arrays.stream(AdvertisementType.values())
                .map(
                        type ->
                                switch (type) {
                                    case FLAT ->
                                            Arguments.of(
                                                    mock(FlatAdvertisementDetailsQuery.class),
                                                    AdvertisementType.FLAT,
                                                    mock(FlatAdvertisementDetailsProjection.class));

                                    case HOUSE ->
                                            Arguments.of(
                                                    mock(HouseAdvertisementDetailsQuery.class),
                                                    AdvertisementType.HOUSE,
                                                    mock(
                                                            HouseAdvertisementDetailsProjection
                                                                    .class));

                                    case COMMERCIAL ->
                                            Arguments.of(
                                                    mock(CommercialAdvertisementDetailsQuery.class),
                                                    AdvertisementType.COMMERCIAL,
                                                    mock(
                                                            CommercialAdvertisementDetailsProjection
                                                                    .class));

                                    case PLOT ->
                                            Arguments.of(
                                                    mock(PlotAdvertisementDetailsQuery.class),
                                                    AdvertisementType.PLOT,
                                                    mock(PlotAdvertisementDetailsProjection.class));
                                });
    }
}
