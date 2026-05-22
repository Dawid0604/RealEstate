/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.bus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;

import pl.dawid0604.realestate.application.command.RegisterUserCommand;
import pl.dawid0604.realestate.application.command.UnbanUserCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CommandBusImplTest {
    @Mock private PlatformTransactionManager transactionManager;

    private static final class TestHandler implements CommandHandler<UnbanUserCommand, String> {

        @Override
        public String handle(final UnbanUserCommand command) {
            return null;
        }

        @Override
        public Class<UnbanUserCommand> getCommandType() {
            return UnbanUserCommand.class;
        }
    }

    private static final class SecondTestHandler
            implements CommandHandler<RegisterUserCommand, Void> {

        @Override
        public Void handle(final RegisterUserCommand command) {
            return null;
        }

        @Override
        public Class<RegisterUserCommand> getCommandType() {
            return RegisterUserCommand.class;
        }
    }

    @Test
    @DisplayName("Should create instance")
    void shouldCreateInstance() {
        // Given
        final CommandHandler<UnbanUserCommand, String> handler = new TestHandler();

        // When
        // Then
        Assertions.assertThatCode(() -> new CommandBusImpl(List.of(handler), transactionManager))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should create instance with empty list of handlers")
    void shouldCreateInstanceWithEmptyLisOfHandlers() {
        // Given
        // When
        // Then
        Assertions.assertThatCode(() -> new CommandBusImpl(List.of(), transactionManager))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should create instance with nullable collection")
    void shouldCreateInstanceWithNullableCollection() {
        // Given
        // When
        // Then
        Assertions.assertThatCode(() -> new CommandBusImpl(null, transactionManager))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should send command")
    void shouldSendCommand() {
        // Given
        final TestHandler handler = spy(new TestHandler());

        // When
        final CommandBusImpl instance = new CommandBusImpl(List.of(handler), transactionManager);

        // Then
        Assertions.assertThatCode(() -> instance.send(mock(UnbanUserCommand.class)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw exception with duplicated handlers")
    void shouldThrowExceptionWithDuplicatedHandlers() {
        // Given
        final TestHandler handler1 = spy(new TestHandler());
        final TestHandler handler2 = spy(new TestHandler());

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> new CommandBusImpl(List.of(handler1, handler2), transactionManager))
                .isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should throw exception when command is null")
    void shouldThrowExceptionWhenCommandIsNull() {
        // Given
        final TestHandler handler1 = spy(new TestHandler());

        // When
        final CommandBusImpl instance = new CommandBusImpl(List.of(handler1), transactionManager);

        // Then
        Assertions.assertThatThrownBy(() -> instance.send(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessageContaining("Command cannot be null");
    }

    @Test
    @DisplayName("Should handle command with different generic types")
    void shouldHandleCommandDifferentGenericTypes() {
        // Given
        final CommandHandler<UnbanUserCommand, String> handler1 = spy(new TestHandler());
        final CommandHandler<RegisterUserCommand, Void> handler2 = spy(new SecondTestHandler());

        // When
        final CommandBusImpl instance =
                new CommandBusImpl(List.of(handler1, handler2), transactionManager);
        instance.send(mock(RegisterUserCommand.class));

        // Then
        verify(handler1, never()).handle(any());
        verify(handler2).handle(any());
    }

    @Test
    @DisplayName("Should handle command with void return type")
    void shouldHandleCommandVoidReturnType() {
        // Given
        final TestHandler handler = spy(new TestHandler());

        // When
        final CommandBusImpl instance = new CommandBusImpl(List.of(handler), transactionManager);
        instance.send(mock(UnbanUserCommand.class));

        // Then
        verify(handler).handle(any());
    }

    @Test
    @DisplayName("Should throw exception when handler not found")
    void shouldThrowExceptionWhenHandlerNotFound() {
        // Given
        // When
        final CommandBusImpl instance = new CommandBusImpl(List.of(), transactionManager);

        // Then
        Assertions.assertThatThrownBy(() -> instance.send(mock(UnbanUserCommand.class)))
                .isExactlyInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Handler not registered for command");
    }
}
