/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);

    Optional<RefreshToken> findByUserId(Identifier userId);

    void deleteIfExistsByUserId(Identifier userId);
}
