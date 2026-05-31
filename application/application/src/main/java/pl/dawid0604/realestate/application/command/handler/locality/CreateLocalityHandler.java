/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.locality;

import static lombok.AccessLevel.PACKAGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.application.command.CreateLocalityCommand;
import pl.dawid0604.realestate.application.port.in.CommandHandler;
import pl.dawid0604.realestate.domain.Locality;
import pl.dawid0604.realestate.domain.port.out.LocalityRepository;
import pl.dawid0604.realestate.domain.shared.exception.LocalityExistsException;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor(access = PACKAGE)
class CreateLocalityHandler implements CommandHandler<CreateLocalityCommand, UUID> {
    private final LocalityRepository localityRepository;

    @Override
    public UUID handle(final CreateLocalityCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        log.info("Creating locality: name={}", command.name());

        if (localityRepository.existsByName(command.name())) {
            log.warn("Creation failed: name already exists: name={}", command.name());
            throw new LocalityExistsException();
        }

        final Locality locality = Locality.create(command.name());
        localityRepository.save(locality);

        log.info("Locality created: name={}", command.name());
        return locality.getId().getValue();
    }

    @Override
    public Class<CreateLocalityCommand> getCommandType() {
        return CreateLocalityCommand.class;
    }
}
