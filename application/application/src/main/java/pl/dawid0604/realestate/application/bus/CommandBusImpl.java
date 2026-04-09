/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.bus;

import static java.util.stream.Collectors.toMap;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.Command;
import pl.dawid0604.realestate.application.port.in.CommandHandler;

import java.util.List;
import java.util.Map;

@Component
non-sealed class CommandBusImpl implements CommandBus {
    private final Map<Class<? extends Command>, CommandHandler<? extends Command, ?>> handlers;

    CommandBusImpl(final List<CommandHandler<? extends Command, ?>> handlerBeans) {
        this.handlers =
                handlerBeans.stream()
                        .collect(toMap(CommandHandler::getCommandType, handler -> handler));
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R> R send(final Command command) {
        final CommandHandler<?, ?> handler = handlers.get(command.getClass());

        if (handler == null) {
            throw new UnsupportedOperationException(
                    "Handler not registered for command, type=" + command.getClass());
        }

        return ((CommandHandler<Command, R>) handler).handle(command);
    }
}
