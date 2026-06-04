/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.token;

import static lombok.AccessLevel.PACKAGE;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.RefreshToken;
import pl.dawid0604.realestate.domain.port.out.RefreshTokenRepository;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class RefreshTokenAdapter implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final TokenMapper mapper;

    @Override
    @Transactional
    public void save(final RefreshToken refreshToken) {
        refreshTokenJpaRepository.save(mapper.toEntity(refreshToken));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByUserId(final Identifier userId) {
        return refreshTokenJpaRepository.findByUserId(userId.getValue()).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteIfExistsByUserId(final Identifier userId) {
        refreshTokenJpaRepository.deleteByUserId(userId.getValue());
    }
}
