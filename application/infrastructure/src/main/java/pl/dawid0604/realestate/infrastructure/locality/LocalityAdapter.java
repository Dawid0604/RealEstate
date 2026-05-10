/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PACKAGE;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class LocalityAdapter implements LocalityRepository {
    private final LocalityJpaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(final UUID localityId) {
        return repository.existsById(localityId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, String> getFullNamesInBatch(final Iterable<UUID> localityIds) {
        return repository.findAllFullNamesByIdIn(localityIds).stream()
                .collect(
                        toMap(
                                LocalityJpaRepository.FullNameProjection::getId,
                                LocalityJpaRepository.FullNameProjection::getName));
    }
}
