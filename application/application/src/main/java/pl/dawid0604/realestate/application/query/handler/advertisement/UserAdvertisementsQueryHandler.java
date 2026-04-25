/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toSet;

import com.google.common.collect.ImmutableMap;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.dto.advertisement.UserAdvertisementCardDto;
import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UserAdvertisementsQueryHandler
        implements QueryHandler<UserAdvertisementsQuery, Page<UserAdvertisementCardDto>> {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementPhotoRepository advertisementPhotoRepository;
    private final LocalityRepository localityRepository;
    private final AdvertisementMapper advertisementMapper;

    @Override
    public Page<UserAdvertisementCardDto> handle(final UserAdvertisementsQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            final var advertisementsPage =
                    advertisementRepository.findAdvertisementsByUser(
                            mapStatuses(query.statuses()),
                            query.email(),
                            query.page(),
                            query.pageSize());

            return Page.of(
                    mapPage(advertisementsPage, executorService),
                    advertisementsPage.getPageNumber(),
                    advertisementsPage.getPageSize(),
                    advertisementsPage.getTotalElements());
        }
    }

    @Override
    public Class<UserAdvertisementsQuery> getQueryType() {
        return UserAdvertisementsQuery.class;
    }

    private static Set<AdvertisementStatus> mapStatuses(final Set<String> statuses) {
        return statuses.stream().map(AdvertisementStatus::of).collect(toSet());
    }

    private List<UserAdvertisementCardDto> mapPage(
            final Page<UserAdvertisementCardProjection> page,
            final ExecutorService executorService) {

        final var localityFullNames = getLocalityFullNames(page.getItems(), executorService);
        final var photos = findPhotos(page.getItems(), executorService);

        CompletableFuture.allOf(localityFullNames, photos).join();

        return page.getItems().stream()
                .map(
                        projection ->
                                mapAdvertisement(
                                        projection, localityFullNames.join(), photos.join()))
                .toList();
    }

    private UserAdvertisementCardDto mapAdvertisement(
            final UserAdvertisementCardProjection projection,
            final Map<UUID, String> localityIds,
            final Map<UUID, Set<PhotoProjection>> photos) {

        return switch (projection) {
            case UserCommercialAdvertisementCardProjection commercialAdvertisement ->
                    advertisementMapper.toUserCommercialCardDto(
                            commercialAdvertisement,
                            localityIds.get(commercialAdvertisement.getLocalityId()),
                            photos.get(commercialAdvertisement.getId()));

            case UserFlatAdvertisementCardProjection flatAdvertisement ->
                    advertisementMapper.toUserFlatCardDto(
                            flatAdvertisement,
                            localityIds.get(flatAdvertisement.getLocalityId()),
                            photos.get(flatAdvertisement.getId()));

            case UserHouseAdvertisementCardProjection houseAdvertisement ->
                    advertisementMapper.toUserHouseCardDto(
                            houseAdvertisement,
                            localityIds.get(houseAdvertisement.getLocalityId()),
                            photos.get(houseAdvertisement.getId()));

            case UserPlotAdvertisementCardProjection plotAdvertisement ->
                    advertisementMapper.toUserPlotCardDto(
                            plotAdvertisement,
                            localityIds.get(plotAdvertisement.getLocalityId()),
                            photos.get(plotAdvertisement.getId()));
        };
    }

    private CompletableFuture<Map<UUID, String>> getLocalityFullNames(
            final List<UserAdvertisementCardProjection> items,
            final ExecutorService executorService) {

        if (items.isEmpty()) {
            return CompletableFuture.completedFuture(emptyMap());
        }

        return CompletableFuture.supplyAsync(
                () -> {
                    final Set<UUID> localityIds =
                            items.stream()
                                    .map(UserAdvertisementCardProjection::getLocalityId)
                                    .collect(toSet());

                    return localityRepository.getFullNamesInBatch(localityIds);
                },
                executorService);
    }

    private CompletableFuture<Map<UUID, Set<PhotoProjection>>> findPhotos(
            final List<UserAdvertisementCardProjection> items,
            final ExecutorService executorService) {

        if (items.isEmpty()) {
            return CompletableFuture.completedFuture(emptyMap());
        }

        final var flatsIds =
                items.stream()
                        .filter(UserFlatAdvertisementCardProjection.class::isInstance)
                        .map(UserAdvertisementCardProjection::getId)
                        .collect(toSet());

        final var housesIds =
                items.stream()
                        .filter(UserHouseAdvertisementCardProjection.class::isInstance)
                        .map(UserAdvertisementCardProjection::getId)
                        .collect(toSet());

        final var commercialsIds =
                items.stream()
                        .filter(UserCommercialAdvertisementCardProjection.class::isInstance)
                        .map(UserAdvertisementCardProjection::getId)
                        .collect(toSet());

        final var plotsIds =
                items.stream()
                        .filter(UserPlotAdvertisementCardProjection.class::isInstance)
                        .map(UserAdvertisementCardProjection::getId)
                        .collect(toSet());

        final var flatPhotosFuture =
                flatsIds.isEmpty()
                        ? CompletableFuture.completedFuture(
                                Collections.<UUID, Set<PhotoProjection>>emptyMap())
                        : CompletableFuture.supplyAsync(
                                () ->
                                        advertisementPhotoRepository.findPhotosInBatch(
                                                flatsIds, AdvertisementType.FLAT),
                                executorService);

        final var housePhotosFuture =
                housesIds.isEmpty()
                        ? CompletableFuture.completedFuture(
                                Collections.<UUID, Set<PhotoProjection>>emptyMap())
                        : CompletableFuture.supplyAsync(
                                () ->
                                        advertisementPhotoRepository.findPhotosInBatch(
                                                housesIds, AdvertisementType.HOUSE),
                                executorService);

        final var commercialsPhotosFuture =
                commercialsIds.isEmpty()
                        ? CompletableFuture.completedFuture(
                                Collections.<UUID, Set<PhotoProjection>>emptyMap())
                        : CompletableFuture.supplyAsync(
                                () ->
                                        advertisementPhotoRepository.findPhotosInBatch(
                                                commercialsIds, AdvertisementType.COMMERCIAL),
                                executorService);

        final var plotsPhotosFuture =
                plotsIds.isEmpty()
                        ? CompletableFuture.completedFuture(
                                Collections.<UUID, Set<PhotoProjection>>emptyMap())
                        : CompletableFuture.supplyAsync(
                                () ->
                                        advertisementPhotoRepository.findPhotosInBatch(
                                                plotsIds, AdvertisementType.PLOT),
                                executorService);

        return CompletableFuture.allOf(
                        flatPhotosFuture,
                        housePhotosFuture,
                        commercialsPhotosFuture,
                        plotsPhotosFuture)
                .thenApply(
                        ignored ->
                                ImmutableMap.<UUID, Set<PhotoProjection>>builder()
                                        .putAll(flatPhotosFuture.join())
                                        .putAll(housePhotosFuture.join())
                                        .putAll(commercialsPhotosFuture.join())
                                        .putAll(plotsPhotosFuture.join())
                                        .build());
    }
}
