/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidUserType;

public record UpdateUserTypeCommand(@ValidEmail String email, @ValidUserType String type)
        implements Command {}
