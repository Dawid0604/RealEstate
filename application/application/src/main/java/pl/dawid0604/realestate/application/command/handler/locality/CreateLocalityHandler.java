/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.locality;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.CreateLocalityCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.Locality;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.shared.exception.LocalityExistsException;

import java.util.UUID;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class CreateLocalityHandler implements CommandHandler<CreateLocalityCommand, UUID> {
    private final LocalityRepository localityRepository;

    @Override
    public UUID handle(final CreateLocalityCommand command) {
        if (localityRepository.existsByName(command.name())) {
            throw new LocalityExistsException();
        }

        final Locality locality = new Locality(Identifier.generate(), command.name());
        localityRepository.save(locality);

        return locality.id().getValue();
    }

    @Override
    public Class<CreateLocalityCommand> getCommandType() {
        return CreateLocalityCommand.class;
    }
}
