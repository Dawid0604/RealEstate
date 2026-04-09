/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.bus;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.command.UnbanUserCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CommandBusImplTest {

    private static final class TestHandler implements CommandHandler<UnbanUserCommand, Void> {

        @Override
        public Void handle(final UnbanUserCommand command) {
            return null;
        }

        @Override
        public Class<UnbanUserCommand> getCommandType() {
            return UnbanUserCommand.class;
        }
    }

    @Test
    @DisplayName("Should create instance")
    void shouldCreateInstance() {
        // Given
        final CommandHandler<UnbanUserCommand, Void> handler = new TestHandler();

        // When
        // Then
        Assertions.assertThatCode(() -> new CommandBusImpl(List.of(handler)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should send command")
    void shouldSendCommand() {
        // Given
        final TestHandler handler = spy(new TestHandler());

        // When
        final CommandBusImpl instance = new CommandBusImpl(List.of(handler));

        // Then
        Assertions.assertThatCode(() -> instance.send(mock(UnbanUserCommand.class)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw exception when handler not found")
    void shouldThrowExceptionWhenHandlerNotFound() {
        // Given
        // When
        final CommandBusImpl instance = new CommandBusImpl(List.of());

        // Then
        Assertions.assertThatThrownBy(() -> instance.send(mock(UnbanUserCommand.class)))
                .isExactlyInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Handler not registered for command");
    }
}
