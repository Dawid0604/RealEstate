package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidFirstName;
import pl.dawid0604.realestate.application.validation.ValidLastName;

public record UpdateUserFullNameCommand(
        @ValidEmail String email,
        @ValidFirstName String newFirstName,
        @ValidLastName String newLastName)
        implements Command {}
