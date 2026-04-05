/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;

public record BanUserCommand(@ValidEmail String email) implements Command {}
