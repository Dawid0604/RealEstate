/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.advertisement;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.mapper.advertisement.AdvertisementMapper;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.port.out.PhotoRepository;

@ExtendWith(MockitoExtension.class)
class SearchAdvertisementQueryHandlerTest {
    @Mock private AdvertisementRepository advertisementRepository;
    @Mock private AdvertisementMapper advertisementMapper;
    @Mock private PhotoRepository photoRepository;
    @Mock private LocalityRepository localityRepository;
    private SearchAdvertisementQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler =
                new SearchAdvertisementQueryHandler(
                        advertisementRepository,
                        advertisementMapper,
                        photoRepository,
                        localityRepository);
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
}
