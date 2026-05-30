/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.locality;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.dto.locality.LocalityDto;
import pl.dawid0604.realestate.application.mapper.locality.LocalityMapper;
import pl.dawid0604.realestate.application.query.FindLocalitiesQuery;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.shared.locality.projection.LocalityProjection;

@ExtendWith(MockitoExtension.class)
class FindLocalitiesHandlerTest {
    @Mock private LocalityRepository localityRepository;
    @Mock private LocalityMapper localityMapper;
    private FindLocalitiesHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new FindLocalitiesHandler(localityRepository, localityMapper);
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
    @DisplayName("Should handle successfully")
    void shouldHandleSuccessfully() {
        // Given
        final List<LocalityProjection> data =
                List.of(mock(LocalityProjection.class), mock(LocalityProjection.class));

        given(localityRepository.findAll()).willReturn(data);
        given(localityMapper.toDto(data.getFirst())).willReturn(mock(LocalityDto.class));
        given(localityMapper.toDto(data.getLast())).willReturn(mock(LocalityDto.class));

        // When
        final var result = handler.handle(new FindLocalitiesQuery());

        // Then
        Assertions.assertThat(result).hasSize(data.size()).isInstanceOf(Set.class);
    }
}
