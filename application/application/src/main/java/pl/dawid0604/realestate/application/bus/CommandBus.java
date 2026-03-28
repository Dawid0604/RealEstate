/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.bus;

import static java.util.stream.Collectors.toMap;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.Command;
import pl.dawid0604.realestate.application.port.in.CommandHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CommandBus {
    private final Map<Class<Command>, CommandHandler<Command, ?>> handlers;

    CommandBus(final List<CommandHandler<Command, ?>> handlerBeans) {
        this.handlers =
                handlerBeans.stream()
                        .collect(toMap(CommandHandler::getCommandType, handler -> handler));
    }

    @SuppressWarnings("unchecked")
    public final <R> R send(final Command command) {
        return Optional.ofNullable(handlers.get(command.getClass()))
                .map(h -> (CommandHandler<Command, R>) h)
                .map(h -> h.handle(command))
                .orElseThrow(
                        () ->
                                new IllegalStateException(
                                        "Handler not registered for command, type="
                                                + command.getClass()));
    }
}
