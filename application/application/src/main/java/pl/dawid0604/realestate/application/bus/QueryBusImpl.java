/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.bus;

import static java.util.stream.Collectors.toMap;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
non-sealed class QueryBusImpl implements QueryBus {
    private final Map<Class<? extends Query>, QueryHandler<?, ?>> handlers;

    QueryBusImpl(final List<QueryHandler<? extends Query, ?>> handlerBeans) {
        this.handlers =
                Objects.requireNonNullElse(handlerBeans, List.<QueryHandler<?, ?>>of()).stream()
                        .collect(toMap(QueryHandler::getQueryType, handler -> handler));
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R> R send(final Query query) {
        Objects.requireNonNull(query, "Query cannot be null");
        final QueryHandler<?, ?> handler = handlers.get(query.getClass());

        if (handler == null) {
            throw new UnsupportedOperationException(
                    "Handler not registered for query, type=" + query.getClass());
        }

        return ((QueryHandler<Query, R>) handler).handle(query);
    }
}
