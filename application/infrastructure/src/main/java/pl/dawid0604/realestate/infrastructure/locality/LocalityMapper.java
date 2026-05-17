/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import static lombok.AccessLevel.PACKAGE;

import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.domain.Locality;

@Component
@NoArgsConstructor(access = PACKAGE)
class LocalityMapper {

    LocalityEntity toEntity(final Locality domain) {
        if (domain == null) {
            return null;
        }

        return new LocalityEntity(domain.id().getValue(), domain.name());
    }
}
