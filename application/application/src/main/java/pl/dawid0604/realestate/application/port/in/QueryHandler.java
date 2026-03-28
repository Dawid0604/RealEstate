/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.port.in;

import pl.dawid0604.realestate.application.query.Query;

public interface QueryHandler<Q extends Query, R> {
    R handle(Q query);

    Class<Q> getQueryType();
}
