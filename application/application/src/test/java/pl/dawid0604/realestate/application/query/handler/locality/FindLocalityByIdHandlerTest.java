/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.locality;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.dto.locality.LocalityDto;
import pl.dawid0604.realestate.application.mapper.locality.LocalityMapper;
import pl.dawid0604.realestate.application.query.FindLocalityByIdQuery;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.shared.exception.LocalityNotFoundException;
import pl.dawid0604.realestate.domain.shared.locality.projection.LocalityProjection;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class FindLocalityByIdHandlerTest {
    @Mock private LocalityRepository localityRepository;
    @Mock private LocalityMapper localityMapper;
    private FindLocalityByIdHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new FindLocalityByIdHandler(localityRepository, localityMapper);
    }

    @Test
    @DisplayName("Should throw exception when query is null")
    void shouldThrowExceptionWhenQueryIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("Query cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when locality not found")
    void shouldThrowExceptionWhenLocalityNotFound() {
        // Given
        final FindLocalityByIdQuery query = getQuery();

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(query))
                .isExactlyInstanceOf(LocalityNotFoundException.class);

        verifyNoInteractions(localityMapper);
        verify(localityRepository).findById(query.localityId());
    }

    @Test
    @DisplayName("Should handle successfully")
    void shouldHandleSuccessfully() {
        // Given
        final FindLocalityByIdQuery query = getQuery();
        final LocalityProjection localityProjection =
                new LocalityProjection() {
                    @Override
                    public UUID getId() {
                        return query.localityId();
                    }

                    @Override
                    public String getName() {
                        return "Warsaw";
                    }
                };

        given(localityRepository.findById(query.localityId()))
                .willReturn(Optional.of(localityProjection));

        given(localityMapper.toDto(localityProjection)).willReturn(mock());

        // When
        final LocalityDto result = handler.handle(query);

        // Then
        Assertions.assertThat(result).isNotNull();
    }

    private static FindLocalityByIdQuery getQuery() {
        return new FindLocalityByIdQuery(UUID.randomUUID());
    }
}
