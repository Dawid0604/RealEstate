/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.bus;

import static java.util.stream.Collectors.toMap;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import pl.dawid0604.realestate.application.command.Command;
import pl.dawid0604.realestate.application.port.in.CommandHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
non-sealed class CommandBusImpl implements CommandBus {
    private final Map<Class<? extends Command>, CommandHandler<?, ?>> handlers;
    private final PlatformTransactionManager transactionManager;

    CommandBusImpl(
            final List<CommandHandler<? extends Command, ?>> handlerBeans,
            final PlatformTransactionManager transactionManager) {

        this.transactionManager = transactionManager;
        this.handlers =
                Objects.requireNonNullElse(handlerBeans, List.<CommandHandler<?, ?>>of()).stream()
                        .collect(toMap(CommandHandler::getCommandType, handler -> handler));
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R> R send(final Command command) {
        Objects.requireNonNull(command, "Command cannot be null");
        final CommandHandler<?, ?> handler = findHandler(command.getClass());

        if (handler == null) {
            throw new UnsupportedOperationException(
                    "Handler not registered for command, type=" + command.getClass());
        }

        return transactionDecorator((CommandHandler<Command, R>) handler, command);
    }

    private <R> R transactionDecorator(
            final CommandHandler<Command, R> handler, final Command command) {

        final TransactionTemplate template = new TransactionTemplate(transactionManager);
        return template.execute(status -> handler.handle(command));
    }

    private CommandHandler<?, ?> findHandler(final Class<?> commandType) {
        var handler = handlers.get(commandType);
        if (handler != null) return handler;

        var superType = commandType.getSuperclass();
        if (superType != null) {
            handler = handlers.get(superType);
            if (handler != null) return handler;
        }

        for (var iface : commandType.getInterfaces()) {
            handler = handlers.get(iface);
            if (handler != null) return handler;
        }

        return null;
    }
}
