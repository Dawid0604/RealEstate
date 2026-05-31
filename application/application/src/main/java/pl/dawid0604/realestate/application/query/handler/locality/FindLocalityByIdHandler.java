/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.locality;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.dto.locality.LocalityDto;
import pl.dawid0604.realestate.application.mapper.locality.LocalityMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.FindLocalityByIdQuery;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.shared.exception.LocalityNotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor(access = PACKAGE)
class FindLocalityByIdHandler implements QueryHandler<FindLocalityByIdQuery, LocalityDto> {
    private final LocalityRepository localityRepository;
    private final LocalityMapper localityMapper;

    @Override
    public LocalityDto handle(final FindLocalityByIdQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");
        log.info("Fetching locality: id={}", query.localityId());

        return localityRepository
                .findById(query.localityId())
                .map(localityMapper::toDto)
                .orElseThrow(throwException(query));
    }

    private static Supplier<LocalityNotFoundException> throwException(
            final FindLocalityByIdQuery query) {

        return () -> {
            log.warn("Locality not found: id={}", query.localityId());
            return new LocalityNotFoundException(query.localityId());
        };
    }

    @Override
    public Class<FindLocalityByIdQuery> getQueryType() {
        return FindLocalityByIdQuery.class;
    }
}
