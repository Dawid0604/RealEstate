/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementDetailsDto;
import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.AdvertisementDetailsQuery;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class AdvertisementDetailsQueryHandler
        implements QueryHandler<AdvertisementDetailsQuery, AdvertisementDetailsDto> {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper advertisementMapper;

    @Override
    public AdvertisementDetailsDto handle(final AdvertisementDetailsQuery query) {
        return null;
    }

    @Override
    public Class<AdvertisementDetailsQuery> getQueryType() {
        return AdvertisementDetailsQuery.class;
    }
}
