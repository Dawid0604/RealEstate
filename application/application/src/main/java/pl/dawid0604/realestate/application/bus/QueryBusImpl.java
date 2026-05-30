/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.bus;

import static java.util.stream.Collectors.toMap;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
class QueryBusImpl implements QueryBus {
    private final Map<Class<? extends Query>, QueryHandler<?, ?>> handlers;
    private final TransactionTemplate readOnlyTransactionTemplate;

    QueryBusImpl(
            final List<QueryHandler<? extends Query, ?>> handlerBeans,
            final PlatformTransactionManager transactionManager) {

        this.readOnlyTransactionTemplate = new TransactionTemplate(transactionManager);
        this.readOnlyTransactionTemplate.setReadOnly(true);

        this.handlers =
                Objects.requireNonNullElse(handlerBeans, List.<QueryHandler<?, ?>>of()).stream()
                        .collect(toMap(QueryHandler::getQueryType, handler -> handler));
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R> R send(final Query query) {
        Objects.requireNonNull(query, "Query cannot be null");
        final QueryHandler<?, ?> handler = findHandler(query.getClass());

        if (handler == null) {
            throw new UnsupportedOperationException(
                    "Handler not registered for query, type=" + query.getClass());
        }

        return transactionDecorator((QueryHandler<Query, R>) handler, query);
    }

    private <R> R transactionDecorator(final QueryHandler<Query, R> handler, final Query query) {
        return readOnlyTransactionTemplate.execute(status -> handler.handle(query));
    }

    private QueryHandler<?, ?> findHandler(final Class<?> queryType) {
        var handler = handlers.get(queryType);

        if (handler != null) {
            return handler;
        }

        final var superType = queryType.getSuperclass();

        if (superType != null) {
            handler = handlers.get(superType);

            if (handler != null) {
                return handler;
            }
        }

        for (final var iface : queryType.getInterfaces()) {
            handler = handlers.get(iface);

            if (handler != null) {
                return handler;
            }
        }

        return null;
    }
}
