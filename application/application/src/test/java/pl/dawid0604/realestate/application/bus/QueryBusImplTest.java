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

import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.UserAdvertisementsQuery;
import pl.dawid0604.realestate.application.query.UserProfileQuery;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class QueryBusImplTest {
    @Mock private PlatformTransactionManager transactionManager;

    private static final class TestHandler implements QueryHandler<UserProfileQuery, String> {

        @Override
        public String handle(final UserProfileQuery query) {
            return null;
        }

        @Override
        public Class<UserProfileQuery> getQueryType() {
            return UserProfileQuery.class;
        }
    }

    private static final class SecondTestHandler
            implements QueryHandler<UserAdvertisementsQuery, Void> {

        @Override
        public Void handle(final UserAdvertisementsQuery query) {
            return null;
        }

        @Override
        public Class<UserAdvertisementsQuery> getQueryType() {
            return UserAdvertisementsQuery.class;
        }
    }

    @Test
    @DisplayName("Should create instance")
    void shouldCreateInstance() {
        // Given
        final QueryHandler<UserProfileQuery, String> handler = new TestHandler();

        // When
        // Then
        Assertions.assertThatCode(() -> new QueryBusImpl(List.of(handler), transactionManager))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should create instance with empty list of handlers")
    void shouldCreateInstanceWithEmptyLisOfHandlers() {
        // Given
        // When
        // Then
        Assertions.assertThatCode(() -> new QueryBusImpl(List.of(), transactionManager))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should create instance with nullable collection")
    void shouldCreateInstanceWithNullableCollection() {
        // Given
        // When
        // Then
        Assertions.assertThatCode(() -> new QueryBusImpl(null, transactionManager))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should send query")
    void shouldSendQuery() {
        // Given
        final TestHandler handler = spy(new TestHandler());

        // When
        final QueryBusImpl instance = new QueryBusImpl(List.of(handler), transactionManager);

        // Then
        Assertions.assertThatCode(() -> instance.send(mock(UserProfileQuery.class)))
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
                        () -> new QueryBusImpl(List.of(handler1, handler2), transactionManager))
                .isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should throw exception when query is null")
    void shouldThrowExceptionWhenQueryIsNull() {
        // Given
        final TestHandler handler1 = spy(new TestHandler());

        // When
        final QueryBusImpl instance = new QueryBusImpl(List.of(handler1), transactionManager);

        // Then
        Assertions.assertThatThrownBy(() -> instance.send(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessageContaining("Query cannot be null");
    }

    @Test
    @DisplayName("Should handle query with different generic types")
    void shouldHandleQueryDifferentGenericTypes() {
        // Given
        final QueryHandler<UserProfileQuery, String> handler1 = spy(new TestHandler());
        final QueryHandler<UserAdvertisementsQuery, Void> handler2 = spy(new SecondTestHandler());

        // When
        final QueryBusImpl instance =
                new QueryBusImpl(List.of(handler1, handler2), transactionManager);
        instance.send(mock(UserAdvertisementsQuery.class));

        // Then
        verify(handler1, never()).handle(any());
        verify(handler2).handle(any());
    }

    @Test
    @DisplayName("Should handle query with void return type")
    void shouldHandleQueryVoidReturnType() {
        // Given
        final TestHandler handler = spy(new TestHandler());

        // When
        final QueryBusImpl instance = new QueryBusImpl(List.of(handler), transactionManager);
        instance.send(mock(UserProfileQuery.class));

        // Then
        verify(handler).handle(any());
    }

    @Test
    @DisplayName("Should throw exception when handler not found")
    void shouldThrowExceptionWhenHandlerNotFound() {
        // Given
        // When
        final QueryBusImpl instance = new QueryBusImpl(List.of(), transactionManager);

        // Then
        Assertions.assertThatThrownBy(() -> instance.send(mock(UserProfileQuery.class)))
                .isExactlyInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Handler not registered for query");
    }
}
