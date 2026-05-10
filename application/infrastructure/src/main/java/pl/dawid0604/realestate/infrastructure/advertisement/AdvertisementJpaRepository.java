/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.data.util.Predicates.negate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

        requireValidSlug(slug);
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

        requireValidSlug(slug);
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
            final UUID userId,
            final int page,
            final int pageSize) {

        Objects.requireNonNull(userId, "UserId cannot be null");
        Objects.requireNonNull(statuses, "Statuses cannot be null");

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> selectQuery = criteriaBuilder.createTupleQuery();
        final CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        final Root<UserAdvertisementViewEntity> selectRoot =
                selectQuery.from(UserAdvertisementViewEntity.class);

        final Root<UserAdvertisementViewEntity> countRoot =
                countQuery.from(UserAdvertisementViewEntity.class);

        selectQuery.select(
                criteriaBuilder.tuple(
                        selectRoot.get(AdvertisementFields.ID).alias(AdvertisementFields.ID),
                        selectRoot.get(AdvertisementFields.SLUG).alias(AdvertisementFields.SLUG),
                        selectRoot.get(AdvertisementFields.TITLE).alias(AdvertisementFields.TITLE),
                        selectRoot.get(AdvertisementFields.PRICE).alias(AdvertisementFields.PRICE),
                        selectRoot.get(AdvertisementFields.AREA).alias(AdvertisementFields.AREA),
                        selectRoot
                                .get(AdvertisementFields.PRICE_PER_SQUARE_METER)
                                .alias(AdvertisementFields.PRICE_PER_SQUARE_METER),
                        selectRoot
                                .get(AdvertisementFields.CREATED_AT)
                                .alias(AdvertisementFields.CREATED_AT),
                        selectRoot
                                .get(AdvertisementFields.STATUS)
                                .alias(AdvertisementFields.STATUS),
                        selectRoot
                                .get(AdvertisementFields.LOCALITY_ID)
                                .alias(AdvertisementFields.LOCALITY_ID),
                        selectRoot
                                .get(AdvertisementFields.VIEW_FEATURED)
                                .alias(AdvertisementFields.VIEW_FEATURED),
                        selectRoot
                                .get(AdvertisementFields.BUILDING_TYPE)
                                .alias(AdvertisementFields.BUILDING_TYPE),
                        selectRoot
                                .get(AdvertisementFields.NUMBER_OF_ROOMS)
                                .alias(AdvertisementFields.NUMBER_OF_ROOMS),
                        selectRoot.get(AdvertisementFields.FLOOR).alias(AdvertisementFields.FLOOR),
                        selectRoot
                                .get(AdvertisementFields.FLOORS)
                                .alias(AdvertisementFields.FLOORS),
                        selectRoot
                                .get(AdvertisementFields.BUILT_YEAR)
                                .alias(AdvertisementFields.BUILT_YEAR),
                        selectRoot
                                .get(AdvertisementFields.TYPE_OF_MARKET)
                                .alias(AdvertisementFields.TYPE_OF_MARKET),
                        selectRoot
                                .get(AdvertisementFields.PLOT_TYPE)
                                .alias(AdvertisementFields.PLOT_TYPE),
                        selectRoot.get(AdvertisementFields.TYPE).alias(AdvertisementFields.TYPE)));

        selectQuery.where(getUserAdvertisementsPredicates(selectRoot, statuses, userId));
        selectQuery.orderBy(criteriaBuilder.desc(selectRoot.get(AdvertisementFields.CREATED_AT)));

        countQuery.select(criteriaBuilder.count(countRoot));
        countQuery.where(getUserAdvertisementsPredicates(countRoot, statuses, userId));

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

        selectQuery.select(selectFields);
        selectQuery.where(selectPredicates);
        selectQuery.orderBy(
                criteriaBuilder.desc(selectRoot.get(AdvertisementFields.CREATED_AT)),
                criteriaBuilder.desc(selectRoot.get(AdvertisementFields.UPDATED_AT)));

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
        fields.add(root.get(AdvertisementFields.ID).alias(AdvertisementFields.ID));
        fields.add(root.get(AdvertisementFields.SLUG).alias(AdvertisementFields.SLUG));
        fields.add(root.get(AdvertisementFields.TITLE).alias(AdvertisementFields.TITLE));
        fields.add(root.get(AdvertisementFields.PRICE).alias(AdvertisementFields.PRICE));
        fields.add(root.get(AdvertisementFields.AREA).alias(AdvertisementFields.AREA));
        fields.add(
                root.get(AdvertisementFields.PRICE_PER_SQUARE_METER)
                        .alias(AdvertisementFields.PRICE_PER_SQUARE_METER));
        fields.add(root.get(AdvertisementFields.STATUS).alias(AdvertisementFields.STATUS));
        fields.add(root.get(AdvertisementFields.CREATED_AT).alias(AdvertisementFields.CREATED_AT));
        fields.add(
                root.get(AdvertisementFields.LOCALITY_ID).alias(AdvertisementFields.LOCALITY_ID));
        fields.add(root.get(AdvertisementFields.USER_ID).alias(AdvertisementFields.USER_ID));
        fields.add(root.get(AdvertisementFields.FEATURED).alias(AdvertisementFields.FEATURED));

        if (clazz == PlotAdvertisementEntity.class) {
            fields.add(
                    root.get(AdvertisementFields.PLOT_TYPE).alias(AdvertisementFields.PLOT_TYPE));

        } else {
            fields.add(
                    root.get(AdvertisementFields.BUILDING_TYPE)
                            .alias(AdvertisementFields.BUILDING_TYPE));
            fields.add(
                    root.get(AdvertisementFields.NUMBER_OF_ROOMS)
                            .alias(AdvertisementFields.NUMBER_OF_ROOMS));
            fields.add(
                    root.get(AdvertisementFields.BUILT_YEAR).alias(AdvertisementFields.BUILT_YEAR));
            fields.add(
                    root.get(AdvertisementFields.TYPE_OF_MARKET)
                            .alias(AdvertisementFields.TYPE_OF_MARKET));
            fields.add(root.get(AdvertisementFields.FLOORS).alias(AdvertisementFields.FLOORS));

            if (clazz != HouseAdvertisementEntity.class) {
                fields.add(root.get(AdvertisementFields.FLOOR).alias(AdvertisementFields.FLOOR));
            }
        }

        fields.add(criteriaBuilder.literal(getTypeLiteral(clazz)).alias(AdvertisementFields.TYPE));
        return criteriaBuilder.tuple(fields);
    }

    private static String getTypeLiteral(final Class<? extends AdvertisementEntity<?, ?>> clazz) {
        if (clazz == FlatAdvertisementEntity.class) {
            return AdvertisementType.FLAT.name();

        } else if (clazz == HouseAdvertisementEntity.class) {
            return AdvertisementType.HOUSE.name();

        } else if (clazz == CommercialAdvertisementEntity.class) {
            return AdvertisementType.COMMERCIAL.name();

        } else if (clazz == PlotAdvertisementEntity.class) {
            return AdvertisementType.PLOT.name();
        }

        throw new IllegalArgumentException(
                "Unexpected type of entity, class=" + clazz.getSimpleName());
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

        getDateFromPredicate(criteriaBuilder, root, atStartOfDay(criteria.dateFrom()))
                .ifPresent(predicates::add);

        getDateToPredicate(criteriaBuilder, root, atEndOfDay(criteria.dateTo()))
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
                        root.get(
                                criteria instanceof SearchPlotAdvertisementsCriteria
                                        ? AdvertisementFields.PLOT_TYPE
                                        : AdvertisementFields.BUILDING_TYPE),
                        criteria.types() != null ? criteria.types() : null)
                .ifPresent(predicates::add);

        return predicates;
    }

    private static LocalDateTime atStartOfDay(final LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }

    private static LocalDateTime atEndOfDay(final LocalDate date) {
        return date != null ? date.atTime(LocalTime.MAX) : null;
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

    private static Optional<Predicate> getDateFromPredicate(
            final CriteriaBuilder criteriaBuilder, final Root<?> root, final LocalDateTime value) {

        return Optional.ofNullable(value)
                .map(
                        v ->
                                criteriaBuilder.or(
                                        criteriaBuilder.greaterThanOrEqualTo(
                                                root.get(AdvertisementFields.CREATED_AT), v),
                                        criteriaBuilder.greaterThanOrEqualTo(
                                                root.get(AdvertisementFields.UPDATED_AT), v)));
    }

    private static Optional<Predicate> getDateToPredicate(
            final CriteriaBuilder criteriaBuilder, final Root<?> root, final LocalDateTime value) {

        return Optional.ofNullable(value)
                .map(
                        v ->
                                criteriaBuilder.or(
                                        criteriaBuilder.lessThanOrEqualTo(
                                                root.get(AdvertisementFields.CREATED_AT), v),
                                        criteriaBuilder.lessThanOrEqualTo(
                                                root.get(AdvertisementFields.UPDATED_AT), v)));
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
            final UUID userId) {

        return List.of(root.get("status").in(statuses), root.get("userId").equalTo(userId));
    }

    private UserAdvertisementCardProjection createUserAdvertisementCardProjection(
            final Tuple tuple) {

        final AdvertisementType advertisementType =
                tuple.get(AdvertisementFields.TYPE, AdvertisementType.class);

        final Class<? extends UserAdvertisementCardProjection> projectionClazz =
                switch (advertisementType) {
                    case FLAT -> UserFlatAdvertisementCardProjection.class;
                    case HOUSE -> UserHouseAdvertisementCardProjection.class;
                    case COMMERCIAL -> UserCommercialAdvertisementCardProjection.class;
                    case PLOT -> UserPlotAdvertisementCardProjection.class;
                };

        return projectionFactory.createProjection(projectionClazz, tupleToMap(tuple));
    }

    private AdvertisementCardProjection createAdvertisementCardProjection(final Tuple tuple) {
        final AdvertisementType advertisementType =
                AdvertisementType.of(tuple.get(AdvertisementFields.TYPE, String.class));

        final Class<? extends AdvertisementCardProjection> projectionClazz =
                switch (advertisementType) {
                    case FLAT -> FlatAdvertisementCardProjection.class;
                    case HOUSE -> HouseAdvertisementCardProjection.class;
                    case COMMERCIAL -> CommercialAdvertisementCardProjection.class;
                    case PLOT -> PlotAdvertisementCardProjection.class;
                };

        return projectionFactory.createProjection(projectionClazz, tupleToMap(tuple));
    }

    private static Map<String, Object> tupleToMap(final Tuple tuple) {
        return tuple.getElements().stream()
                .collect(
                        HashMap::new,
                        (map, element) -> map.put(element.getAlias(), tuple.get(element)),
                        HashMap::putAll);
    }

    private static void requireValidSlug(final String value) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Slug cannot be blank");
        }
    }
}
