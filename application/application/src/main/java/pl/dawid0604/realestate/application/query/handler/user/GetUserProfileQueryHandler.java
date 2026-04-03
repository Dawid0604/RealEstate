/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.user;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.dto.UserProfileDto;
import pl.dawid0604.realestate.application.mapper.user.UserMapper;
import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.GetUserProfileQuery;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class GetUserProfileQueryHandler implements QueryHandler<GetUserProfileQuery, UserProfileDto> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserProfileDto handle(final GetUserProfileQuery query) {
        return userRepository
                .findUserProfile(query.email())
                .map(userMapper::toUserProfileDto)
                .orElseThrow(() -> new UserNotFoundException(query.email()));
    }

    @Override
    public Class<GetUserProfileQuery> getQueryType() {
        return GetUserProfileQuery.class;
    }
}
