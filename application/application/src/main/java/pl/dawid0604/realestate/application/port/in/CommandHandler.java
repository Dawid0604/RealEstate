/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.port.in;

import pl.dawid0604.realestate.application.command.Command;

public interface CommandHandler<C extends Command, R> {
    R handle(C command);

    Class<C> getCommandType();
}
