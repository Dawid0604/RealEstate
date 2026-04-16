/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementCardDto;
import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.SearchAdvertisementsQuery;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.port.out.PhotoRepository;
import pl.dawid0604.realestate.domain.shared.Page;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.exception.LocalityNotFoundException;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
        final Exception exception;

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            final var advertisementsPage = advertisementRepository.findByCriteria(query.criteria());

            return Page.of(
                    mapPage(advertisementsPage, executorService),
                    advertisementsPage.getPageNumber(),
                    advertisementsPage.getPageSize(),
                    advertisementsPage.getTotalElements());

        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            exception = interruptedException;

        } catch (ExecutionException executionException) {
            exception = executionException;
            // log error
        }

        throw new IllegalStateException("Failed to fetch advertisements", exception);
    }

    @Override
    public Class<SearchAdvertisementsQuery> getQueryType() {
        return SearchAdvertisementsQuery.class;
    }

    private List<AdvertisementCardDto> mapPage(
            final Page<AdvertisementCardProjection> page, final ExecutorService executorService)
            throws ExecutionException, InterruptedException {

        final List<AdvertisementCardDto> cards = new ArrayList<>();

        for (final var projection : page.getItems()) {
            cards.add(mapAdvertisement(projection, executorService));
        }

        return cards;
    }

    private AdvertisementCardDto mapAdvertisement(
            final AdvertisementCardProjection projection, final ExecutorService executorService)
            throws InterruptedException, ExecutionException {

        final var localityFullName = getLocalityFullName(projection, executorService);
        final var photosFuture = findPhotos(projection, executorService);

        CompletableFuture.allOf(localityFullName, photosFuture).join();

        return switch (projection) {
            case CommercialAdvertisementCardProjection commercialAdvertisement ->
                    advertisementMapper.toCommercialCardDto(
                            commercialAdvertisement, localityFullName.get(), photosFuture.get());

            case FlatAdvertisementCardProjection flatAdvertisement ->
                    advertisementMapper.toFlatCardDto(
                            flatAdvertisement, localityFullName.get(), photosFuture.get());

            case HouseAdvertisementCardProjection houseAdvertisement ->
                    advertisementMapper.toHouseCardDto(
                            houseAdvertisement, localityFullName.get(), photosFuture.get());

            case PlotAdvertisementCardProjection plotAdvertisement ->
                    advertisementMapper.toPlotCardDto(
                            plotAdvertisement, localityFullName.get(), photosFuture.get());
        };
    }

    private CompletableFuture<String> getLocalityFullName(
            final AdvertisementCardProjection projection, final ExecutorService executorService) {

        return CompletableFuture.supplyAsync(
                () ->
                        localityRepository
                                .getFullName(projection.getLocalityId())
                                .orElseThrow(
                                        () ->
                                                new LocalityNotFoundException(
                                                        projection.getLocalityId())),
                executorService);
    }

    private CompletableFuture<Set<PhotoProjection>> findPhotos(
            final AdvertisementCardProjection projection, final ExecutorService executorService) {

        return CompletableFuture.supplyAsync(
                () ->
                        switch (projection) {
                            case CommercialAdvertisementCardProjection detailsProjection ->
                                    photoRepository.findCommercialAdvertisementPhotos(
                                            detailsProjection.getSlug());

                            case FlatAdvertisementCardProjection detailsProjection ->
                                    photoRepository.findFlatAdvertisementPhotos(
                                            detailsProjection.getSlug());

                            case HouseAdvertisementCardProjection detailsProjection ->
                                    photoRepository.findHouseAdvertisementPhotos(
                                            detailsProjection.getSlug());

                            case PlotAdvertisementCardProjection detailsProjection ->
                                    photoRepository.findPlotAdvertisementPhotos(
                                            detailsProjection.getSlug());
                        },
                executorService);
    }
}
