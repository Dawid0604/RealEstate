/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toSet;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementCardDto;
import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.SearchAdvertisementsQuery;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.port.out.PhotoRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.Page;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;

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
class SearchAdvertisementQueryHandler
        implements QueryHandler<SearchAdvertisementsQuery, Page<AdvertisementCardDto>> {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper advertisementMapper;
    private final PhotoRepository photoRepository;
    private final LocalityRepository localityRepository;

    @Override
    public Page<AdvertisementCardDto> handle(final SearchAdvertisementsQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            final var advertisementsPage = advertisementRepository.findByCriteria(query.criteria());

            return Page.of(
                    mapPage(advertisementsPage, executorService),
                    advertisementsPage.getPageNumber(),
                    advertisementsPage.getPageSize(),
                    advertisementsPage.getTotalElements());
        }
    }

    @Override
    public Class<SearchAdvertisementsQuery> getQueryType() {
        return SearchAdvertisementsQuery.class;
    }

    private List<AdvertisementCardDto> mapPage(
            final Page<AdvertisementCardProjection> page, final ExecutorService executorService) {

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

    private AdvertisementCardDto mapAdvertisement(
            final AdvertisementCardProjection projection,
            final Map<UUID, String> localityIds,
            final Map<UUID, Set<PhotoProjection>> photos) {

        return switch (projection) {
            case CommercialAdvertisementCardProjection commercialAdvertisement ->
                    advertisementMapper.toCommercialCardDto(
                            commercialAdvertisement,
                            localityIds.get(commercialAdvertisement.getLocalityId()),
                            photos.get(commercialAdvertisement.getId()));

            case FlatAdvertisementCardProjection flatAdvertisement ->
                    advertisementMapper.toFlatCardDto(
                            flatAdvertisement,
                            localityIds.get(flatAdvertisement.getLocalityId()),
                            photos.get(flatAdvertisement.getId()));

            case HouseAdvertisementCardProjection houseAdvertisement ->
                    advertisementMapper.toHouseCardDto(
                            houseAdvertisement,
                            localityIds.get(houseAdvertisement.getLocalityId()),
                            photos.get(houseAdvertisement.getId()));

            case PlotAdvertisementCardProjection plotAdvertisement ->
                    advertisementMapper.toPlotCardDto(
                            plotAdvertisement,
                            localityIds.get(plotAdvertisement.getLocalityId()),
                            photos.get(plotAdvertisement.getId()));
        };
    }

    private CompletableFuture<Map<UUID, String>> getLocalityFullNames(
            final List<AdvertisementCardProjection> items, final ExecutorService executorService) {

        return CompletableFuture.supplyAsync(
                () -> {
                    final Set<UUID> localityIds =
                            items.stream()
                                    .map(AdvertisementCardProjection::getLocalityId)
                                    .collect(toSet());

                    return localityRepository.getFullNamesInBatch(localityIds);
                },
                executorService);
    }

    private CompletableFuture<Map<UUID, Set<PhotoProjection>>> findPhotos(
            final List<AdvertisementCardProjection> items, final ExecutorService executorService) {

        if (items.isEmpty()) {
            return CompletableFuture.completedFuture(emptyMap());
        }

        final List<UUID> ids = items.stream().map(AdvertisementCardProjection::getId).toList();
        final AdvertisementType advertisementType =
                switch (items.getFirst()) {
                    case CommercialAdvertisementCardProjection ignored ->
                            AdvertisementType.COMMERCIAL;

                    case FlatAdvertisementCardProjection ignored -> AdvertisementType.FLAT;
                    case HouseAdvertisementCardProjection ignored -> AdvertisementType.HOUSE;
                    case PlotAdvertisementCardProjection ignored -> AdvertisementType.PLOT;
                };

        return CompletableFuture.supplyAsync(
                        () ->
                                photoRepository.findAdvertisementsPhotosInBatch(
                                        ids, advertisementType),
                        executorService)
                .thenApply(v -> v);
    }
}
