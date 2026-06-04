/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.locality;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.Locality;

class LocalityMapperTest {
    private LocalityMapper localityMapper;

    @BeforeEach
    void setUp() {
        this.localityMapper = new LocalityMapper();
    }

    @Test
    @DisplayName("Should return null when value is null")
    void shouldReturnNullWhenValueIsNull() {
        // Given
        // When
        final LocalityEntity result = localityMapper.toEntity(null);

        // Then
        Assertions.assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should map properly")
    void shouldMapProperly() {
        // Given
        final Identifier id = Identifier.generate();
        final String name = "Warsaw";
        final Locality locality = Locality.reconstitute(id, name);

        // When
        final LocalityEntity result = localityMapper.toEntity(locality);

        // Then
        Assertions.assertThat(result)
                .returns(locality.getId().getValue(), LocalityEntity::getId)
                .returns(locality.getName(), LocalityEntity::getName);
    }
}
