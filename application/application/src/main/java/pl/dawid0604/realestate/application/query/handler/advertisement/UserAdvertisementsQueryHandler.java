/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static java.util.stream.Collectors.toSet;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import pl.dawid0604.realestate.application.dto.PagedResult;
import pl.dawid0604.realestate.application.dto.advertisement.UserAdvertisementCardDto;
import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.UserAdvertisementsQuery;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.port.out.PhotoRepository;
import pl.dawid0604.realestate.domain.shared.Page;
import pl.dawid0604.realestate.domain.shared.exception.LocalityNotFoundException;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.CommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.FlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.HouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.PlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.projection.photo.PhotoProjection;

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
class UserAdvertisementsQueryHandler
        implements QueryHandler<UserAdvertisementsQuery, PagedResult<UserAdvertisementCardDto>> {

    private final AdvertisementRepository advertisementRepository;
    private final PhotoRepository photoRepository;
    private final LocalityRepository localityRepository;
    private final AdvertisementMapper advertisementMapper;
    private static final Set<AdvertisementStatus> DEFAULT_STATUSES =
            Set.of(AdvertisementStatus.values());

    @Override
    public PagedResult<UserAdvertisementCardDto> handle(final UserAdvertisementsQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");
        final Exception exception;

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            final Set<AdvertisementStatus> statuses = getRequiredStatuses(query.statuses());

            final var advertisementsPage =
                    advertisementRepository.findAdvertisementsByUser(
                            statuses, query.email(), query.page(), query.pageSize());

            return PagedResult.of(
                    mapPage(advertisementsPage, executorService),
                    query.page(),
                    query.pageSize(),
                    advertisementsPage.totalElements());

        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            exception = interruptedException;

        } catch (ExecutionException executionException) {
            exception = executionException;
            // log error
        }

        throw new IllegalStateException("Failed to fetch advertisement data", exception);
    }

    @Override
    public Class<UserAdvertisementsQuery> getQueryType() {
        return UserAdvertisementsQuery.class;
    }

    private static Set<AdvertisementStatus> getRequiredStatuses(final Set<String> statuses) {
        if (CollectionUtils.isEmpty(statuses)) {
            return DEFAULT_STATUSES;
        }

        return statuses.stream().map(AdvertisementStatus::of).collect(toSet());
    }

    private List<UserAdvertisementCardDto> mapPage(
            final Page<AdvertisementCardProjection> page, final ExecutorService executorService)
            throws ExecutionException, InterruptedException {

        final List<UserAdvertisementCardDto> cards = new ArrayList<>();

        for (final var projection : page.items()) {
            cards.add(mapAdvertisement(projection, executorService));
        }

        return cards;
    }

    private UserAdvertisementCardDto mapAdvertisement(
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
