/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.locality;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.dto.locality.LocalityDto;
import pl.dawid0604.realestate.application.mapper.locality.LocalityMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.FindLocalityByIdQuery;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.shared.exception.LocalityNotFoundException;

import java.util.Objects;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class FindLocalityByIdHandler implements QueryHandler<FindLocalityByIdQuery, LocalityDto> {
    private final LocalityRepository localityRepository;
    private final LocalityMapper localityMapper;

    @Override
    public LocalityDto handle(final FindLocalityByIdQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        return localityRepository
                .findById(query.localityId())
                .map(localityMapper::toDto)
                .orElseThrow(() -> new LocalityNotFoundException(query.localityId()));
    }

    @Override
    public Class<FindLocalityByIdQuery> getQueryType() {
        return FindLocalityByIdQuery.class;
    }
}
