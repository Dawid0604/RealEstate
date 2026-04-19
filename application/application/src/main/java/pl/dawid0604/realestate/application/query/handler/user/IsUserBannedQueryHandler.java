/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.IsUserBannedQuery;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.UserRepository;

import java.util.Objects;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class IsUserBannedQueryHandler implements QueryHandler<IsUserBannedQuery, Boolean> {
    private final UserRepository userRepository;

    @Override
    public Boolean handle(final IsUserBannedQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");
        return userRepository.hasStatus(query.email(), UserStatus.BANNED);
    }

    @Override
    public Class<IsUserBannedQuery> getQueryType() {
        return IsUserBannedQuery.class;
    }
}
