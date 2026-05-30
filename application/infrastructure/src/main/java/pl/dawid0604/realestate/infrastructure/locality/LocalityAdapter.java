/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PACKAGE;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.realestate.domain.Locality;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.shared.locality.projection.LocalityProjection;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class LocalityAdapter implements LocalityRepository {
    private final LocalityJpaRepository repository;
    private final LocalityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(final UUID localityId) {
        return repository.existsById(localityId);
    }

    @Override
    @Transactional
    public void save(final Locality locality) {
        repository.save(mapper.toEntity(locality));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(final String name) {
        return repository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, String> getFullNamesInBatch(final Iterable<UUID> localityIds) {
        return repository.findAllFullNamesByIdIn(localityIds).stream()
                .collect(toMap(LocalityProjection::getId, LocalityProjection::getName));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalityProjection> findAll() {
        return repository.findAllCustom();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocalityProjection> findById(final UUID localityId) {
        return repository.findByIdCustom(localityId);
    }
}
