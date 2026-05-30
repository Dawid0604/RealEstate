/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import static lombok.AccessLevel.PACKAGE;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import pl.dawid0604.realestate.domain.Locality;

@Component
@NoArgsConstructor(access = PACKAGE)
class LocalityMapper {

    LocalityEntity toEntity(final Locality domain) {
        if (domain == null) {
            return null;
        }

        return new LocalityEntity(domain.getId().getValue(), domain.getName());
    }
}
