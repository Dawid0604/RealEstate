/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.locality;

import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.command.CreateLocalityCommand;
import pl.dawid0604.realestate.domain.Locality;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.shared.exception.LocalityExistsException;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CreateLocalityHandlerTest {
    @Mock private LocalityRepository localityRepository;
    @Captor private ArgumentCaptor<Locality> localityArgumentCaptor;
    private CreateLocalityHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new CreateLocalityHandler(localityRepository);
    }

    @Test
    @DisplayName("Should throw exception when command is null")
    void shouldThrowExceptionWhenCommandIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("Command cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when locality exists")
    void shouldThrowExceptionWhenLocalityExists() {
        // Given
        final CreateLocalityCommand command = getCommand();
        given(localityRepository.existsByName(command.name())).willReturn(true);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(LocalityExistsException.class);
    }

    @Test
    @DisplayName("Should handle successfully")
    void shouldHandleSuccessfully() {
        // Given
        final CreateLocalityCommand command = getCommand();

        // When
        final UUID result = handler.handle(command);

        // Then
        verify(localityRepository).save(localityArgumentCaptor.capture());

        Assertions.assertThat(localityArgumentCaptor.getValue())
                .satisfies(
                        loc -> {
                            Assertions.assertThat(loc.getName()).isEqualTo(command.name());
                            Assertions.assertThat(loc.getId().getValue()).isEqualTo(result);
                        });
    }

    private static CreateLocalityCommand getCommand() {
        return new CreateLocalityCommand("Warsaw");
    }
}
