/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.locality;

import static lombok.AccessLevel.PACKAGE;

import static java.util.stream.Collectors.toSet;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.dto.locality.LocalityDto;
import pl.dawid0604.realestate.application.mapper.locality.LocalityMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.FindLocalitiesQuery;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;

import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class FindLocalitiesHandler implements QueryHandler<FindLocalitiesQuery, Set<LocalityDto>> {
    private final LocalityRepository localityRepository;
    private final LocalityMapper localityMapper;

    @Override
    public Set<LocalityDto> handle(final FindLocalitiesQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");
        return localityRepository.findAll().stream().map(localityMapper::toDto).collect(toSet());
    }

    @Override
    public Class<FindLocalitiesQuery> getQueryType() {
        return FindLocalitiesQuery.class;
    }
}
