/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.bus;

import pl.dawid0604.realestate.application.command.Command;

public sealed interface CommandBus permits CommandBusImpl {
    <R> R send(Command command);
}
