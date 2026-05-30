/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.advertisement;

import static lombok.AccessLevel.PACKAGE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementDetailsDto;
import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.AdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.CommercialAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.FlatAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.HouseAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.PlotAdvertisementDetailsQuery;
import pl.dawid0604.realestate.domain.port.out.AdvertisementPhotoRepository;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;
import pl.dawid0604.realestate.domain.shared.user.projection.AdvertisementUserProjection;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class AdvertisementDetailsQueryHandler
        implements QueryHandler<AdvertisementDetailsQuery, AdvertisementDetailsDto> {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementPhotoRepository advertisementPhotoRepository;
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
                    advertisementRepository.findDetails(
                            commercialQuery.slug(), AdvertisementType.COMMERCIAL);

            case FlatAdvertisementDetailsQuery flatQuery ->
                    advertisementRepository.findDetails(flatQuery.slug(), AdvertisementType.FLAT);

            case HouseAdvertisementDetailsQuery houseQuery ->
                    advertisementRepository.findDetails(houseQuery.slug(), AdvertisementType.HOUSE);

            case PlotAdvertisementDetailsQuery plotQuery ->
                    advertisementRepository.findDetails(plotQuery.slug(), AdvertisementType.PLOT);
        };
    }

    private AdvertisementDetailsDto toDetails(final AdvertisementDetailsProjection projection) {
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            final var localityFullName = getLocalityFullName(projection, executorService);
            final var photosFuture = findPhotos(projection, executorService);
            final var claimsFuture = findClaims(projection, executorService);
            final var userFuture = findOwner(projection, executorService);

            CompletableFuture.allOf(localityFullName, photosFuture, claimsFuture, userFuture)
                    .join();

            return map(
                    projection,
                    localityFullName.join(),
                    photosFuture.join(),
                    claimsFuture.join(),
                    userFuture.join());
        }
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
                                .getFullNamesInBatch(Set.of(projection.getLocalityId()))
                                .get(projection.getLocalityId()),
                executorService);
    }

    private CompletableFuture<Set<PhotoProjection>> findPhotos(
            final AdvertisementDetailsProjection projection,
            final ExecutorService executorService) {

        return CompletableFuture.supplyAsync(
                () ->
                        advertisementPhotoRepository
                                .findPhotosInBatch(
                                        List.of(projection.getId()),
                                        getAdvertisementType(projection))
                                .get(projection.getId()),
                executorService);
    }

    private CompletableFuture<AdvertisementUserProjection> findOwner(
            final AdvertisementDetailsProjection projection,
            final ExecutorService executorService) {

        return CompletableFuture.supplyAsync(
                () ->
                        userRepository
                                .findAdvertisementUser(projection.getUserId())
                                .orElseThrow(
                                        () -> new UserNotFoundException(projection.getUserId())),
                executorService);
    }

    private CompletableFuture<Set<AdvertisementClaimProjection>> findClaims(
            final AdvertisementDetailsProjection projection,
            final ExecutorService executorService) {

        return CompletableFuture.supplyAsync(
                () ->
                        advertisementRepository.findClaims(
                                projection.getId(), getAdvertisementType(projection)),
                executorService);
    }

    private static AdvertisementType getAdvertisementType(
            final AdvertisementDetailsProjection projection) {

        return switch (projection) {
            case CommercialAdvertisementDetailsProjection ignored -> AdvertisementType.COMMERCIAL;
            case FlatAdvertisementDetailsProjection ignored -> AdvertisementType.FLAT;
            case HouseAdvertisementDetailsProjection ignored -> AdvertisementType.HOUSE;
            case PlotAdvertisementDetailsProjection ignored -> AdvertisementType.PLOT;
        };
    }
}
