/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.bus;

import pl.dawid0604.realestate.application.query.Query;

public sealed interface QueryBus permits QueryBusImpl {
    <R> R send(Query query);
}
