/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PACKAGE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.data.util.Predicates.negate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchCommercialAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchFlatAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchHouseAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchPlotAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserCommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserFlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserHouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserPlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.exception.InternalException;

@Repository
@RequiredArgsConstructor(access = PACKAGE)
class AdvertisementJpaRepository {
    private final FlatAdvertisementJpaRepository flatJpaRepository;
    private final HouseAdvertisementJpaRepository houseJpaRepository;
    private final CommercialAdvertisementJpaRepository commercialJpaRepository;
    private final PlotAdvertisementJpaRepository plotJpaRepository;

    private final FlatAdvertisementClaimJpaRepository flatClaimJpaRepository;
    private final HouseAdvertisementClaimJpaRepository houseClaimJpaRepository;
    private final CommercialAdvertisementClaimJpaRepository commercialClaimJpaRepository;
    private final PlotAdvertisementClaimJpaRepository plotClaimJpaRepository;

    private final EntityManager entityManager;
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    void save(final AdvertisementEntity<?, ?> entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        switch (entity) {
            case CommercialAdvertisementEntity commercialEntity ->
                    commercialJpaRepository.save(commercialEntity);

            case FlatAdvertisementEntity flatEntity -> flatJpaRepository.save(flatEntity);
            case HouseAdvertisementEntity houseEntity -> houseJpaRepository.save(houseEntity);
            case PlotAdvertisementEntity plotEntity -> plotJpaRepository.save(plotEntity);
        }
    }

    Optional<AdvertisementEntity<?, ?>> findBySlug(
            final String slug, final AdvertisementType type) {

        verifyNotBlank(slug, "Slug");
        return switch (type) {
            case FLAT -> flatJpaRepository.findBySlug(slug).map(AdvertisementEntity.class::cast);
            case HOUSE -> houseJpaRepository.findBySlug(slug).map(AdvertisementEntity.class::cast);
            case PLOT -> plotJpaRepository.findBySlug(slug).map(AdvertisementEntity.class::cast);
            case COMMERCIAL ->
                    commercialJpaRepository.findBySlug(slug).map(AdvertisementEntity.class::cast);
        };
    }

    Set<AdvertisementClaimProjection> findClaims(
            final UUID id, final AdvertisementType advertisementType) {

        Objects.requireNonNull(id, "Id cannot be null");
        return switch (advertisementType) {
            case FLAT -> flatClaimJpaRepository.findClaimsById(id);
            case HOUSE -> houseClaimJpaRepository.findClaimsById(id);
            case COMMERCIAL -> commercialClaimJpaRepository.findClaimsById(id);
            case PLOT -> plotClaimJpaRepository.findClaimsById(id);
        };
    }

    Optional<AdvertisementDetailsProjection> findDetails(
            final String slug, final AdvertisementType advertisementType) {

        verifyNotBlank(slug, "Slug");
        return switch (advertisementType) {
            case FLAT ->
                    flatJpaRepository
                            .findDetailsBySlug(slug)
                            .map(AdvertisementDetailsProjection.class::cast);

            case HOUSE ->
                    houseJpaRepository
                            .findDetailsBySlug(slug)
                            .map(AdvertisementDetailsProjection.class::cast);

            case COMMERCIAL ->
                    commercialJpaRepository
                            .findDetailsBySlug(slug)
                            .map(AdvertisementDetailsProjection.class::cast);

            case PLOT ->
                    plotJpaRepository
                            .findDetailsBySlug(slug)
                            .map(AdvertisementDetailsProjection.class::cast);
        };
    }

    Page<UserAdvertisementCardProjection> findAdvertisementsByUser(
            final Set<AdvertisementStatus> statuses,
            final String email,
            final int page,
            final int pageSize) {

        verifyNotBlank(email, "Email");
        Objects.requireNonNull(statuses, "Statuses");

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> selectQuery = criteriaBuilder.createTupleQuery();
        final CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        final Root<UserAdvertisementViewEntity> selectRoot =
                selectQuery.from(UserAdvertisementViewEntity.class);

        final Root<UserAdvertisementViewEntity> countRoot =
                countQuery.from(UserAdvertisementViewEntity.class);

        selectQuery.select(
                criteriaBuilder.tuple(
                        selectRoot.get(AdvertisementFields.ID),
                        selectRoot.get(AdvertisementFields.SLUG),
                        selectRoot.get(AdvertisementFields.TITLE),
                        selectRoot.get(AdvertisementFields.PRICE),
                        selectRoot.get(AdvertisementFields.AREA),
                        selectRoot.get(AdvertisementFields.PRICE_PER_SQUARE_METER),
                        selectRoot.get(AdvertisementFields.CREATED_AT),
                        selectRoot.get(AdvertisementFields.LOCALITY_ID),
                        selectRoot.get(AdvertisementFields.FEATURED),
                        selectRoot.get(AdvertisementFields.BUILDING_TYPE),
                        selectRoot.get(AdvertisementFields.NUMBER_OF_ROOMS),
                        selectRoot.get(AdvertisementFields.FLOOR),
                        selectRoot.get(AdvertisementFields.FLOORS),
                        selectRoot.get(AdvertisementFields.BUILT_YEAR),
                        selectRoot.get(AdvertisementFields.TYPE_OF_MARKET),
                        selectRoot.get(AdvertisementFields.PLOT_TYPE),
                        selectRoot.get(AdvertisementFields.TYPE)));

        selectQuery.where(getUserAdvertisementsPredicates(selectRoot, statuses, email));
        selectQuery.orderBy(criteriaBuilder.desc(selectRoot.get(AdvertisementFields.CREATED_AT)));

        countQuery.select(criteriaBuilder.count(countRoot));
        countQuery.where(getUserAdvertisementsPredicates(countRoot, statuses, email));

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final Future<Long> countFuture =
                    executor.submit(() -> entityManager.createQuery(countQuery).getSingleResult());

            final Future<List<Tuple>> selectFuture =
                    executor.submit(
                            () ->
                                    entityManager
                                            .createQuery(selectQuery)
                                            .setFirstResult(page * pageSize)
                                            .setMaxResults(pageSize)
                                            .getResultList());

            final List<UserAdvertisementCardProjection> content =
                    selectFuture.get().stream()
                            .map(this::createUserAdvertisementCardProjection)
                            .toList();

            return new PageImpl<>(content, PageRequest.of(page, pageSize), countFuture.get());

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new InternalException(exception);

        } catch (ExecutionException exception) {
            throw new InternalException(exception);
        }
    }

    Page<AdvertisementCardProjection> findByCriteria(final SearchAdvertisementsCriteria criteria) {
        Objects.requireNonNull(criteria, "Criteria cannot be null");

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> selectQuery = criteriaBuilder.createTupleQuery();
        final CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        final Class<? extends AdvertisementEntity<?, ?>> entityClazz =
                getAdvertisementEntityClazz(criteria);

        final Root<? extends AdvertisementEntity<?, ?>> selectRoot = selectQuery.from(entityClazz);
        final Root<? extends AdvertisementEntity<?, ?>> countRoot = countQuery.from(entityClazz);

        final List<Predicate> selectPredicates =
                getAdvertisementByCriteriaPredicates(selectRoot, criteriaBuilder, criteria);

        final List<Predicate> countPredicates =
                getAdvertisementByCriteriaPredicates(countRoot, criteriaBuilder, criteria);

        final Selection<Tuple> selectFields =
                getAdvertisementByCriteriaSelectFields(selectRoot, criteriaBuilder, entityClazz);

        selectQuery.select(criteriaBuilder.tuple(selectFields));
        selectQuery.where(selectPredicates);
        selectQuery.orderBy(criteriaBuilder.desc(selectRoot.get(AdvertisementFields.CREATED_AT)));

        countQuery.select(criteriaBuilder.count(countRoot));
        countQuery.where(countPredicates);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final Future<Long> countFuture =
                    executor.submit(() -> entityManager.createQuery(countQuery).getSingleResult());

            final Future<List<Tuple>> selectFuture =
                    executor.submit(
                            () ->
                                    entityManager
                                            .createQuery(selectQuery)
                                            .setFirstResult(criteria.page() * criteria.pageSize())
                                            .setMaxResults(criteria.pageSize())
                                            .getResultList());

            final List<AdvertisementCardProjection> content =
                    selectFuture.get().stream()
                            .map(this::createAdvertisementCardProjection)
                            .toList();

            return new PageImpl<>(
                    content,
                    PageRequest.of(criteria.page(), criteria.pageSize()),
                    countFuture.get());

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new InternalException(exception);

        } catch (ExecutionException exception) {
            throw new InternalException(exception);
        }
    }

    private static Selection<Tuple> getAdvertisementByCriteriaSelectFields(
            final Root<? extends AdvertisementEntity<?, ?>> root,
            final CriteriaBuilder criteriaBuilder,
            final Class<? extends AdvertisementEntity<?, ?>> clazz) {

        final List<Selection<?>> fields = new ArrayList<>();
        fields.add(root.get(AdvertisementFields.ID));
        fields.add(root.get(AdvertisementFields.SLUG));
        fields.add(root.get(AdvertisementFields.TITLE));
        fields.add(root.get(AdvertisementFields.PRICE));
        fields.add(root.get(AdvertisementFields.AREA));
        fields.add(root.get(AdvertisementFields.PRICE_PER_SQUARE_METER));
        fields.add(root.get(AdvertisementFields.STATUS));
        fields.add(root.get(AdvertisementFields.CREATED_AT));
        fields.add(root.get(AdvertisementFields.LOCALITY_ID));
        fields.add(root.get(AdvertisementFields.USER_ID));
        fields.add(root.get(AdvertisementFields.FEATURED));

        if (clazz == PlotAdvertisementEntity.class) {
            fields.add(root.get(AdvertisementFields.PLOT_TYPE));

        } else {
            fields.add(root.get(AdvertisementFields.BUILDING_TYPE));
            fields.add(root.get(AdvertisementFields.NUMBER_OF_ROOMS));
            fields.add(root.get(AdvertisementFields.BUILT_YEAR));
            fields.add(root.get(AdvertisementFields.TYPE_OF_MARKET));
            fields.add(root.get(AdvertisementFields.FLOORS));

            if (clazz != HouseAdvertisementEntity.class) {
                fields.add(root.get(AdvertisementFields.FLOOR));
            }
        }

        return criteriaBuilder.tuple(fields);
    }

    private static List<Predicate> getAdvertisementByCriteriaPredicates(
            final Root<? extends AdvertisementEntity<?, ?>> root,
            final CriteriaBuilder criteriaBuilder,
            final SearchAdvertisementsCriteria criteria) {

        final List<Predicate> predicates =
                getBaseAdvertisementByCriteriaPredicates(criteriaBuilder, root, criteria);

        final List<Predicate> extraPredicates =
                switch (criteria) {
                    case SearchCommercialAdvertisementsCriteria commercialCriteria ->
                            getCommercialAdvertisementByCriteriaPredicates(
                                    criteriaBuilder, root, commercialCriteria);

                    case SearchFlatAdvertisementsCriteria flatCriteria ->
                            getFlatAdvertisementByCriteriaPredicates(
                                    criteriaBuilder, root, flatCriteria);

                    case SearchHouseAdvertisementsCriteria houseCriteria ->
                            getHouseAdvertisementByCriteriaPredicates(
                                    criteriaBuilder, root, houseCriteria);

                    default -> List.of();
                };

        predicates.addAll(extraPredicates);
        return predicates;
    }

    private static List<Predicate> getBaseAdvertisementByCriteriaPredicates(
            final CriteriaBuilder criteriaBuilder,
            final Root<? extends AdvertisementEntity<?, ?>> root,
            final SearchAdvertisementsCriteria criteria) {

        final List<Predicate> predicates = new ArrayList<>();

        predicates.add(
                criteriaBuilder.equal(
                        root.get(AdvertisementFields.LOCALITY_ID), criteria.localityId()));

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.PRICE), criteria.priceFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.PRICE), criteria.priceTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.DATE), criteria.dateFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.DATE), criteria.dateTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.AREA), criteria.areaFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.AREA), criteria.areaTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.PRICE_PER_SQUARE_METER),
                        criteria.pricePerSquareMeterFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.PRICE_PER_SQUARE_METER),
                        criteria.pricePerSquareMeterTo())
                .ifPresent(predicates::add);

        getInPredicate(
                        root.get(AdvertisementFields.OFFER_FROM),
                        criteria.offerFrom() != null ? criteria.offerFrom() : null)
                .ifPresent(predicates::add);

        getInPredicate(
                        root.get(
                                criteria instanceof SearchPlotAdvertisementsCriteria
                                        ? AdvertisementFields.PLOT_TYPE
                                        : AdvertisementFields.BUILDING_TYPE),
                        criteria.types() != null ? criteria.types() : null)
                .ifPresent(predicates::add);

        return predicates;
    }

    @SuppressWarnings("CPD-START")
    private static List<Predicate> getFlatAdvertisementByCriteriaPredicates(
            final CriteriaBuilder criteriaBuilder,
            final Root<? extends AdvertisementEntity<?, ?>> root,
            final SearchFlatAdvertisementsCriteria criteria) {

        final List<Predicate> predicates = new ArrayList<>();

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.FLOOR), criteria.floorFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.FLOOR), criteria.floorTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.FLOORS),
                        criteria.floorsFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.FLOORS), criteria.floorsTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.NUMBER_OF_ROOMS),
                        criteria.numberOfRoomsFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.NUMBER_OF_ROOMS),
                        criteria.numberOfRoomsTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.BUILT_YEAR),
                        criteria.builtYearFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.BUILT_YEAR),
                        criteria.builtYearTo())
                .ifPresent(predicates::add);

        getInPredicate(root.get(AdvertisementFields.TYPE_OF_MARKET), criteria.typeOfMarkets())
                .ifPresent(predicates::add);

        return predicates;
    }

    private static List<Predicate> getCommercialAdvertisementByCriteriaPredicates(
            final CriteriaBuilder criteriaBuilder,
            final Root<? extends AdvertisementEntity<?, ?>> root,
            final SearchCommercialAdvertisementsCriteria criteria) {

        final List<Predicate> predicates = new ArrayList<>();

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.FLOOR), criteria.floorFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.FLOOR), criteria.floorTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.FLOORS),
                        criteria.floorsFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.FLOORS), criteria.floorsTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.NUMBER_OF_ROOMS),
                        criteria.numberOfRoomsFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.NUMBER_OF_ROOMS),
                        criteria.numberOfRoomsTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.BUILT_YEAR),
                        criteria.builtYearFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.BUILT_YEAR),
                        criteria.builtYearTo())
                .ifPresent(predicates::add);

        getInPredicate(root.get(AdvertisementFields.TYPE_OF_MARKET), criteria.typeOfMarkets())
                .ifPresent(predicates::add);

        return predicates;
    }

    @SuppressWarnings("CPD-END")
    private static List<Predicate> getHouseAdvertisementByCriteriaPredicates(
            final CriteriaBuilder criteriaBuilder,
            final Root<? extends AdvertisementEntity<?, ?>> root,
            final SearchHouseAdvertisementsCriteria criteria) {

        final List<Predicate> predicates = new ArrayList<>();

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.FLOORS),
                        criteria.floorsFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder, root.get(AdvertisementFields.FLOORS), criteria.floorsTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.NUMBER_OF_ROOMS),
                        criteria.numberOfRoomsFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.NUMBER_OF_ROOMS),
                        criteria.numberOfRoomsTo())
                .ifPresent(predicates::add);

        getGreaterThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.BUILT_YEAR),
                        criteria.builtYearFrom())
                .ifPresent(predicates::add);

        getLessThanOrEqualToPredicate(
                        criteriaBuilder,
                        root.get(AdvertisementFields.BUILT_YEAR),
                        criteria.builtYearTo())
                .ifPresent(predicates::add);

        getInPredicate(root.get(AdvertisementFields.TYPE_OF_MARKET), criteria.typeOfMarkets())
                .ifPresent(predicates::add);

        return predicates;
    }

    private static <T extends Comparable<? super T>>
            Optional<Predicate> getGreaterThanOrEqualToPredicate(
                    final CriteriaBuilder criteriaBuilder, final Path<T> path, final T value) {

        return Optional.ofNullable(value).map(v -> criteriaBuilder.greaterThanOrEqualTo(path, v));
    }

    private static <T extends Comparable<? super T>>
            Optional<Predicate> getLessThanOrEqualToPredicate(
                    final CriteriaBuilder criteriaBuilder, final Path<T> path, final T value) {

        return Optional.ofNullable(value).map(v -> criteriaBuilder.lessThanOrEqualTo(path, v));
    }

    private static <T extends Comparable<? super T>> Optional<Predicate> getInPredicate(
            final Path<T> path, final Collection<T> value) {

        return Optional.ofNullable(value).filter(negate(CollectionUtils::isEmpty)).map(path::in);
    }

    private static Class<? extends AdvertisementEntity<?, ?>> getAdvertisementEntityClazz(
            final SearchAdvertisementsCriteria criteria) {

        return switch (criteria) {
            case SearchCommercialAdvertisementsCriteria ignored ->
                    CommercialAdvertisementEntity.class;

            case SearchFlatAdvertisementsCriteria ignored -> FlatAdvertisementEntity.class;
            case SearchHouseAdvertisementsCriteria ignored -> HouseAdvertisementEntity.class;
            case SearchPlotAdvertisementsCriteria ignored -> PlotAdvertisementEntity.class;
        };
    }

    private static List<Predicate> getUserAdvertisementsPredicates(
            final Root<UserAdvertisementViewEntity> root,
            final Set<AdvertisementStatus> statuses,
            final String email) {

        return List.of(root.get("status").in(statuses), root.get("email").equalTo(email));
    }

    private UserAdvertisementCardProjection createUserAdvertisementCardProjection(
            final Tuple tuple) {

        final Class<? extends UserAdvertisementCardProjection> projectionClazz =
                switch (tuple.get("type", AdvertisementType.class)) {
                    case FLAT -> UserFlatAdvertisementCardProjection.class;
                    case HOUSE -> UserHouseAdvertisementCardProjection.class;
                    case COMMERCIAL -> UserCommercialAdvertisementCardProjection.class;
                    case PLOT -> UserPlotAdvertisementCardProjection.class;
                };

        return projectionFactory.createProjection(projectionClazz, tupleToMap(tuple));
    }

    private AdvertisementCardProjection createAdvertisementCardProjection(final Tuple tuple) {

        final Class<? extends AdvertisementCardProjection> projectionClazz =
                switch (tuple.get("type", AdvertisementType.class)) {
                    case FLAT -> FlatAdvertisementCardProjection.class;
                    case HOUSE -> HouseAdvertisementCardProjection.class;
                    case COMMERCIAL -> CommercialAdvertisementCardProjection.class;
                    case PLOT -> PlotAdvertisementCardProjection.class;
                };

        return projectionFactory.createProjection(projectionClazz, tupleToMap(tuple));
    }

    private static Map<String, Object> tupleToMap(final Tuple tuple) {
        return tuple.getElements().stream()
                .collect(toMap(TupleElement::getAlias, e -> tuple.get(e.getAlias())));
    }

    private static void verifyNotBlank(final String value, final String label) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(label + " cannot be blank");
        }
    }
}
