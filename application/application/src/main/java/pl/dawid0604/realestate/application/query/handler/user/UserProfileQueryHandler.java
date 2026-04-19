/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.dto.user.UserProfileDto;
import pl.dawid0604.realestate.application.mapper.user.UserMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.UserProfileQuery;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Objects;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class UserProfileQueryHandler implements QueryHandler<UserProfileQuery, UserProfileDto> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserProfileDto handle(final UserProfileQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        return userRepository
                .findUserProfile(query.email())
                .map(userMapper::toUserProfileDto)
                .orElseThrow(() -> new UserNotFoundException(query.email()));
    }

    @Override
    public Class<UserProfileQuery> getQueryType() {
        return UserProfileQuery.class;
    }
}
