/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static org.springframework.util.CollectionUtils.isEmpty;

import static java.util.stream.Collectors.toMap;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    void save(@Nonnull final AdvertisementEntity entity) {
        switch (entity) {
            case CommercialAdvertisementEntity commercialEntity ->
                    commercialJpaRepository.save(commercialEntity);

            case FlatAdvertisementEntity flatEntity -> flatJpaRepository.save(flatEntity);
            case HouseAdvertisementEntity houseEntity -> houseJpaRepository.save(houseEntity);
            case PlotAdvertisementEntity plotEntity -> plotJpaRepository.save(plotEntity);
        }
    }

    @Nonnull
    Optional<? extends AdvertisementEntity> findBySlug(
            @Nonnull final String slug, @Nonnull final AdvertisementType type) {

        return switch (type) {
            case FLAT -> flatJpaRepository.findBySlug(slug);
            case HOUSE -> houseJpaRepository.findBySlug(slug);
            case COMMERCIAL -> commercialJpaRepository.findBySlug(slug);
            case PLOT -> plotJpaRepository.findBySlug(slug);
        };
    }

    @Nonnull
    Set<AdvertisementClaimProjection> findClaims(
            @Nonnull final UUID id, @Nonnull final AdvertisementType advertisementType) {

        return switch (advertisementType) {
            case FLAT -> flatClaimJpaRepository.findClaimsById(id);
            case HOUSE -> houseClaimJpaRepository.findClaimsById(id);
            case COMMERCIAL -> commercialClaimJpaRepository.findClaimsById(id);
            case PLOT -> plotClaimJpaRepository.findClaimsById(id);
        };
    }

    @Nonnull
    Optional<AdvertisementDetailsProjection> findDetails(
            @Nonnull final String slug, @Nonnull final AdvertisementType advertisementType) {

        return switch (advertisementType) {
            case FLAT -> flatJpaRepository.findDetailsBySlug(slug);
            case HOUSE -> houseJpaRepository.findDetailsBySlug(slug);
            case COMMERCIAL -> commercialJpaRepository.findDetailsBySlug(slug);
            case PLOT -> plotJpaRepository.findDetailsBySlug(slug);
        };
    }

    @Nonnull
    Page<UserAdvertisementCardProjection> findAdvertisementsByUser(
            @Nonnull final Set<AdvertisementStatus> statuses,
            @Nonnull final String email,
            final int page,
            final int pageSize) {

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> selectQuery = criteriaBuilder.createTupleQuery();
        final CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        final Root<UserAdvertisementViewEntity> selectRoot =
                selectQuery.from(UserAdvertisementViewEntity.class);

        final Root<UserAdvertisementViewEntity> countRoot =
                countQuery.from(UserAdvertisementViewEntity.class);

        selectQuery.select(
                criteriaBuilder.tuple(
                        selectRoot.get("id"),
                        selectRoot.get("slug"),
                        selectRoot.get("title"),
                        selectRoot.get("price"),
                        selectRoot.get("area"),
                        selectRoot.get("pricePerSquareMeter"),
                        selectRoot.get("createdAt"),
                        selectRoot.get("localityId"),
                        selectRoot.get("featured"),
                        selectRoot.get("buildingType"),
                        selectRoot.get("numberOfRooms"),
                        selectRoot.get("floor"),
                        selectRoot.get("floors"),
                        selectRoot.get("builtYear"),
                        selectRoot.get("typeOfMarket"),
                        selectRoot.get("plotType"),
                        selectRoot.get("type")));

        selectQuery.where(getUserAdvertisementsPredicates(selectRoot, statuses, email));
        selectQuery.orderBy(criteriaBuilder.desc(selectRoot.get("createdAt")));

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

        } catch (InterruptedException | ExecutionException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            throw new RuntimeException(exception);
        }
    }

    @Nonnull
    Page<AdvertisementCardProjection> findByCriteria(
            @Nonnull final SearchAdvertisementsCriteria criteria) {

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tuple> selectQuery = criteriaBuilder.createTupleQuery();
        final CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        final Class<? extends AdvertisementEntity> entityClazz =
                getAdvertisementEntityClazz(criteria);

        final Root<? extends AdvertisementEntity> selectRoot = selectQuery.from(entityClazz);
        final Root<? extends AdvertisementEntity> countRoot = countQuery.from(entityClazz);

        final List<Predicate> selectPredicates =
                getAdvertisementByCriteriaPredicates(selectRoot, criteriaBuilder, criteria);

        final List<Predicate> countPredicates =
                getAdvertisementByCriteriaPredicates(countRoot, criteriaBuilder, criteria);

        final Selection<Tuple> selectFields =
                getAdvertisementByCriteriaSelectFields(selectRoot, criteriaBuilder, entityClazz);

        selectQuery.select(criteriaBuilder.tuple(selectFields));
        selectQuery.where(selectPredicates);
        selectQuery.orderBy(criteriaBuilder.desc(selectRoot.get("createdAt")));

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

        } catch (InterruptedException | ExecutionException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            throw new RuntimeException(exception);
        }
    }

    @Nonnull
    private static Selection<Tuple> getAdvertisementByCriteriaSelectFields(
            @Nonnull final Root<? extends AdvertisementEntity> root,
            @Nonnull final CriteriaBuilder criteriaBuilder,
            @Nonnull final Class<? extends AdvertisementEntity> clazz) {

        final List<Selection<?>> fields = new ArrayList<>();
        fields.add(root.get("id"));
        fields.add(root.get("slug"));
        fields.add(root.get("title"));
        fields.add(root.get("price"));
        fields.add(root.get("area"));
        fields.add(root.get("pricePerSquareMeter"));
        fields.add(root.get("status"));
        fields.add(root.get("createdAt"));
        fields.add(root.get("localityId"));
        fields.add(root.get("userId"));
        fields.add(root.get("featured"));

        if (clazz != PlotAdvertisementEntity.class) {
            fields.add(root.get("buildingType"));
            fields.add(root.get("numberOfRooms"));
            fields.add(root.get("builtYear"));
            fields.add(root.get("typeOfMarket"));
            fields.add(root.get("floors"));

            if (clazz != HouseAdvertisementEntity.class) {
                fields.add(root.get("floor"));
            }

        } else {
            fields.add(root.get("plotType"));
        }

        return criteriaBuilder.tuple(fields);
    }

    @Nonnull
    private static List<Predicate> getAdvertisementByCriteriaPredicates(
            @Nonnull final Root<? extends AdvertisementEntity> root,
            @Nonnull final CriteriaBuilder criteriaBuilder,
            @Nonnull final SearchAdvertisementsCriteria criteria) {

        final List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("localityId"), criteria.localityId()));

        if (criteria.priceFrom() != null)
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), criteria.priceFrom()));

        if (criteria.priceTo() != null)
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), criteria.priceTo()));

        if (criteria.dateFrom() != null)
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("date"), criteria.dateFrom()));

        if (criteria.dateTo() != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), criteria.dateTo()));

        if (criteria.areaFrom() != null)
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("area"), criteria.areaFrom()));

        if (criteria.areaTo() != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("area"), criteria.areaTo()));

        if (criteria.pricePerSquareMeterFrom() != null)
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("pricePerSquareMeter"), criteria.pricePerSquareMeterFrom()));

        if (criteria.pricePerSquareMeterTo() != null)
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("pricePerSquareMeter"), criteria.pricePerSquareMeterTo()));

        if (!isEmpty(criteria.offerFrom()))
            predicates.add(root.get("offerFrom").in(criteria.offerFrom()));

        if (!isEmpty(criteria.types())) {
            final String field =
                    criteria instanceof SearchPlotAdvertisementsCriteria
                            ? "plotType"
                            : "buildingType";

            predicates.add(root.get(field).in(criteria.types()));
        }

        switch (criteria) {
            case SearchCommercialAdvertisementsCriteria commercialCriteria -> {
                if (commercialCriteria.floorFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("floor"), commercialCriteria.floorFrom()));

                if (commercialCriteria.floorTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("floor"), commercialCriteria.floorTo()));

                if (commercialCriteria.floorsFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("floors"), commercialCriteria.floorsFrom()));

                if (commercialCriteria.floorsTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("floors"), commercialCriteria.floorsTo()));

                if (commercialCriteria.numberOfRoomsFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("numberOfRooms"),
                                    commercialCriteria.numberOfRoomsFrom()));

                if (commercialCriteria.numberOfRoomsTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("numberOfRooms"),
                                    commercialCriteria.numberOfRoomsTo()));

                if (commercialCriteria.builtYearFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("builtYear"), commercialCriteria.builtYearFrom()));

                if (commercialCriteria.builtYearTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("builtYear"), commercialCriteria.builtYearTo()));

                if (!isEmpty(commercialCriteria.typeOfMarkets()))
                    predicates.add(root.get("typeOfMarket").in(commercialCriteria.typeOfMarkets()));
            }

            case SearchFlatAdvertisementsCriteria flatCriteria -> {
                if (flatCriteria.floorFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("floor"), flatCriteria.floorFrom()));

                if (flatCriteria.floorTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("floor"), flatCriteria.floorTo()));

                if (flatCriteria.floorsFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("floors"), flatCriteria.floorsFrom()));

                if (flatCriteria.floorsTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("floors"), flatCriteria.floorsTo()));

                if (flatCriteria.numberOfRoomsFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("numberOfRooms"), flatCriteria.numberOfRoomsFrom()));

                if (flatCriteria.numberOfRoomsTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("numberOfRooms"), flatCriteria.numberOfRoomsTo()));

                if (flatCriteria.builtYearFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("builtYear"), flatCriteria.builtYearFrom()));

                if (flatCriteria.builtYearTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("builtYear"), flatCriteria.builtYearTo()));

                if (!isEmpty(flatCriteria.typeOfMarkets()))
                    predicates.add(root.get("typeOfMarket").in(flatCriteria.typeOfMarkets()));
            }

            case SearchHouseAdvertisementsCriteria houseCriteria -> {
                if (houseCriteria.floorsFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("floors"), houseCriteria.floorsFrom()));

                if (houseCriteria.floorsTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("floors"), houseCriteria.floorsTo()));

                if (houseCriteria.numberOfRoomsFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("numberOfRooms"), houseCriteria.numberOfRoomsFrom()));

                if (houseCriteria.numberOfRoomsTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("numberOfRooms"), houseCriteria.numberOfRoomsTo()));

                if (houseCriteria.builtYearFrom() != null)
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("builtYear"), houseCriteria.builtYearFrom()));

                if (houseCriteria.builtYearTo() != null)
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("builtYear"), houseCriteria.builtYearTo()));

                if (!isEmpty(houseCriteria.typeOfMarkets()))
                    predicates.add(root.get("typeOfMarket").in(houseCriteria.typeOfMarkets()));
            }

            default -> {}
        }

        return predicates;
    }

    @Nonnull
    private static Class<? extends AdvertisementEntity> getAdvertisementEntityClazz(
            @Nonnull final SearchAdvertisementsCriteria criteria) {

        return switch (criteria) {
            case SearchCommercialAdvertisementsCriteria ignored ->
                    CommercialAdvertisementEntity.class;

            case SearchFlatAdvertisementsCriteria ignored -> FlatAdvertisementEntity.class;
            case SearchHouseAdvertisementsCriteria ignored -> HouseAdvertisementEntity.class;
            case SearchPlotAdvertisementsCriteria ignored -> PlotAdvertisementEntity.class;
        };
    }

    @Nonnull
    private static List<Predicate> getUserAdvertisementsPredicates(
            @Nonnull final Root<UserAdvertisementViewEntity> root,
            @Nonnull final Set<AdvertisementStatus> statuses,
            @Nonnull final String email) {

        return List.of(root.get("status").in(statuses), root.get("email").equalTo(email));
    }

    @Nonnull
    private UserAdvertisementCardProjection createUserAdvertisementCardProjection(
            @Nonnull final Tuple tuple) {

        final Class<? extends UserAdvertisementCardProjection> projectionClazz =
                switch (tuple.get("type", AdvertisementType.class)) {
                    case FLAT -> UserFlatAdvertisementCardProjection.class;
                    case HOUSE -> UserHouseAdvertisementCardProjection.class;
                    case COMMERCIAL -> UserCommercialAdvertisementCardProjection.class;
                    case PLOT -> UserPlotAdvertisementCardProjection.class;
                };

        return projectionFactory.createProjection(projectionClazz, tupleToMap(tuple));
    }

    @Nonnull
    private AdvertisementCardProjection createAdvertisementCardProjection(
            @Nonnull final Tuple tuple) {

        final Class<? extends AdvertisementCardProjection> projectionClazz =
                switch (tuple.get("type", AdvertisementType.class)) {
                    case FLAT -> FlatAdvertisementCardProjection.class;
                    case HOUSE -> HouseAdvertisementCardProjection.class;
                    case COMMERCIAL -> CommercialAdvertisementCardProjection.class;
                    case PLOT -> PlotAdvertisementCardProjection.class;
                };

        return projectionFactory.createProjection(projectionClazz, tupleToMap(tuple));
    }

    @Nonnull
    private static Map<String, Object> tupleToMap(@Nonnull final Tuple tuple) {
        return tuple.getElements().stream()
                .collect(toMap(TupleElement::getAlias, e -> tuple.get(e.getAlias())));
    }
}
