/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementDetailsDto;
import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.AdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.CommercialAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.FlatAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.HouseAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.PlotAdvertisementDetailsQuery;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.port.out.PhotoRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.LocalityNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.AdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.CommercialAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.FlatAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.HouseAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.PlotAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.photo.PhotoProjection;
import pl.dawid0604.realestate.domain.shared.projection.user.AdvertisementUserProjection;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class AdvertisementDetailsQueryHandler
        implements QueryHandler<AdvertisementDetailsQuery, AdvertisementDetailsDto> {

    private final AdvertisementRepository advertisementRepository;
    private final PhotoRepository photoRepository;
    private final LocalityRepository localityRepository;
    private final UserRepository userRepository;
    private final AdvertisementMapper advertisementMapper;

    @Override
    public AdvertisementDetailsDto handle(final AdvertisementDetailsQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        return find(query)
                .map(this::toDetails)
                .orElseThrow(() -> new AdvertisementNotFoundException(query.slug()));
    }

    @Override
    public Class<AdvertisementDetailsQuery> getQueryType() {
        return AdvertisementDetailsQuery.class;
    }

    private Optional<? extends AdvertisementDetailsProjection> find(
            final AdvertisementDetailsQuery query) {

        return switch (query) {
            case CommercialAdvertisementDetailsQuery commercialQuery ->
                    advertisementRepository.findCommercialDetails(commercialQuery.slug());

            case FlatAdvertisementDetailsQuery flatQuery ->
                    advertisementRepository.findFlatDetails(flatQuery.slug());

            case HouseAdvertisementDetailsQuery houseQuery ->
                    advertisementRepository.findHouseDetails(houseQuery.slug());

            case PlotAdvertisementDetailsQuery plotQuery ->
                    advertisementRepository.findPlotDetails(plotQuery.slug());
        };
    }

    private AdvertisementDetailsDto toDetails(final AdvertisementDetailsProjection projection) {
        final Exception exception;

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            final var localityFullName = getLocalityFullName(projection, executorService);
            final var photosFuture = findPhotos(projection, executorService);
            final var claimsFuture = findClaims(projection, executorService);
            final var userFuture = findOwner(projection, executorService);

            CompletableFuture.allOf(localityFullName, photosFuture, claimsFuture, userFuture)
                    .join();

            return map(
                    projection,
                    localityFullName.get(),
                    photosFuture.get(),
                    claimsFuture.get(),
                    userFuture.get());

        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            exception = interruptedException;

        } catch (ExecutionException executionException) {
            exception = executionException;
        }

        throw new AdvertisementNotFoundException(projection.getSlug(), exception);
    }

    private AdvertisementDetailsDto map(
            final AdvertisementDetailsProjection projection,
            final String localityFullName,
            final Set<PhotoProjection> photoProjections,
            final Set<AdvertisementClaimProjection> advertisementClaimProjections,
            final AdvertisementUserProjection advertisementUserProjection) {

        return switch (projection) {
            case CommercialAdvertisementDetailsProjection detailsProjection ->
                    advertisementMapper.toCommercialDetailsDto(
                            detailsProjection,
                            localityFullName,
                            photoProjections,
                            advertisementClaimProjections,
                            advertisementUserProjection);

            case FlatAdvertisementDetailsProjection detailsProjection ->
                    advertisementMapper.toFlatDetailsDto(
                            detailsProjection,
                            localityFullName,
                            photoProjections,
                            advertisementClaimProjections,
                            advertisementUserProjection);

            case HouseAdvertisementDetailsProjection detailsProjection ->
                    advertisementMapper.toHouseDetailsDto(
                            detailsProjection,
                            localityFullName,
                            photoProjections,
                            advertisementClaimProjections,
                            advertisementUserProjection);

            case PlotAdvertisementDetailsProjection detailsProjection ->
                    advertisementMapper.toPlotDetailsDto(
                            detailsProjection,
                            localityFullName,
                            photoProjections,
                            advertisementClaimProjections,
                            advertisementUserProjection);
        };
    }

    private CompletableFuture<String> getLocalityFullName(
            final AdvertisementDetailsProjection projection,
            final ExecutorService executorService) {

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
            final AdvertisementDetailsProjection projection,
            final ExecutorService executorService) {

        return CompletableFuture.supplyAsync(
                () ->
                        switch (projection) {
                            case CommercialAdvertisementDetailsProjection detailsProjection ->
                                    photoRepository.findCommercialAdvertisementPhotos(
                                            detailsProjection.getSlug());

                            case FlatAdvertisementDetailsProjection detailsProjection ->
                                    photoRepository.findFlatAdvertisementPhotos(
                                            detailsProjection.getSlug());

                            case HouseAdvertisementDetailsProjection detailsProjection ->
                                    photoRepository.findHouseAdvertisementPhotos(
                                            detailsProjection.getSlug());

                            case PlotAdvertisementDetailsProjection detailsProjection ->
                                    photoRepository.findPlotAdvertisementPhotos(
                                            detailsProjection.getSlug());
                        },
                executorService);
    }

    private CompletableFuture<AdvertisementUserProjection> findOwner(
            final AdvertisementDetailsProjection projection,
            final ExecutorService executorService) {

        return CompletableFuture.supplyAsync(
                () ->
                        userRepository
                                .findAdvertisementUser(projection.getOwnerEmail())
                                .orElseThrow(
                                        () ->
                                                new UserNotFoundException(
                                                        projection.getOwnerEmail())),
                executorService);
    }

    private CompletableFuture<Set<AdvertisementClaimProjection>> findClaims(
            final AdvertisementDetailsProjection projection,
            final ExecutorService executorService) {

        return CompletableFuture.supplyAsync(
                () ->
                        switch (projection) {
                            case CommercialAdvertisementDetailsProjection detailsProjection ->
                                    advertisementRepository.findCommercialClaims(
                                            detailsProjection.getSlug());

                            case FlatAdvertisementDetailsProjection detailsProjection ->
                                    advertisementRepository.findFlatClaims(
                                            detailsProjection.getSlug());

                            case HouseAdvertisementDetailsProjection detailsProjection ->
                                    advertisementRepository.findHouseClaims(
                                            detailsProjection.getSlug());

                            case PlotAdvertisementDetailsProjection detailsProjection ->
                                    advertisementRepository.findPlotClaims(
                                            detailsProjection.getSlug());
                        },
                executorService);
    }
}
