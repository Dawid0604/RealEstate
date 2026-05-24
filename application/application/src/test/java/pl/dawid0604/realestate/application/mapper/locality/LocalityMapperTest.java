/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.mapper.locality;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import pl.dawid0604.realestate.application.dto.locality.LocalityDto;
import pl.dawid0604.realestate.domain.shared.locality.projection.LocalityProjection;

import java.util.UUID;

class LocalityMapperTest {
    private LocalityMapper localityMapper;

    @BeforeEach
    void setUp() {
        this.localityMapper = Mappers.getMapper(LocalityMapper.class);
    }

    @Test
    @DisplayName("Should return null when input is null")
    void shouldReturnNullWhenInputIsNull() {
        // Given
        // When
        final LocalityDto result = localityMapper.toDto(null);

        // Then
        Assertions.assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should map successfully")
    void shouldMapSuccessfully() {
        // Given
        final UUID localityId = UUID.randomUUID();

        final LocalityProjection localityProjection =
                new LocalityProjection() {

                    @Override
                    public UUID getId() {
                        return localityId;
                    }

                    @Override
                    public String getName() {
                        return "Warsaw";
                    }
                };

        // When
        final LocalityDto result = localityMapper.toDto(localityProjection);

        // Then
        Assertions.assertThat(result)
                .isNotNull()
                .returns(localityProjection.getId(), LocalityDto::id)
                .returns(localityProjection.getName(), LocalityDto::name);
    }
}
