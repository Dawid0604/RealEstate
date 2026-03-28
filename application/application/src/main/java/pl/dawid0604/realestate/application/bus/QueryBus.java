/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.bus;

import static java.util.stream.Collectors.toMap;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.port.in.QueryHandler;
import pl.dawid0604.realestate.application.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class QueryBus {
    private final Map<Class<Query>, QueryHandler<Query, ?>> handlers;

    QueryBus(final List<QueryHandler<Query, ?>> handlerBeans) {
        this.handlers =
                handlerBeans.stream()
                        .collect(toMap(QueryHandler::getQueryType, handler -> handler));
    }

    @SuppressWarnings("unchecked")
    public final <R> R send(final Query query) {
        return Optional.ofNullable(handlers.get(query.getClass()))
                .map(h -> (QueryHandler<Query, R>) h)
                .map(h -> h.handle(query))
                .orElseThrow(
                        () ->
                                new IllegalStateException(
                                        "Handler not registered for query, type="
                                                + query.getClass()));
    }
}
