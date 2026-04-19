/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.user;

import static lombok.AccessLevel.PACKAGE;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;
import pl.dawid0604.realestate.domain.shared.user.projection.AdvertisementUserProjection;
import pl.dawid0604.realestate.domain.shared.user.projection.UserProfileProjection;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class UserAdapter implements UserRepository {
    private final UserJpaRepository repository;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileProjection> findUserProfile(final String email) {
        return repository.findUserProfileByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdvertisementUserProjection> findAdvertisementUser(final String email) {
        return repository.findAdvertisementUserByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(final String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void save(final User user) {
        repository.save(mapper.toEntity(user));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(final String email) {
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void deleteByEmail(final String email) {
        if (repository.deleteByEmail(email) == 0) {
            throw new UserNotFoundException(email);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasStatus(final String email, final UserStatus userStatus) {
        return repository.hasStatus(email, userStatus);
    }
}
