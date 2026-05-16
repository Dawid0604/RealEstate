package pl.dawid0604.realestate.infrastructure.token;

import static lombok.AccessLevel.PACKAGE;

import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.RefreshToken;

@Component
@NoArgsConstructor(access = PACKAGE)
class TokenMapper {

    RefreshToken toDomain(final RefreshTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        return RefreshToken.reconstitute(
                Identifier.of(entity.getId()),
                Identifier.of(entity.getUserId()),
                entity.getHashedToken(),
                entity.getCreatedAt(),
                entity.getExpiresAt());
    }

    RefreshTokenEntity toEntity(final RefreshToken domain) {
        if (domain == null) {
            return null;
        }

        return new RefreshTokenEntity(
                domain.getId().getValue(),
                domain.getUserId().getValue(),
                domain.getToken(),
                domain.getExpiresAt());
    }
}
