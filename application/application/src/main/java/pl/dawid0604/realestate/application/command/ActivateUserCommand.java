package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;

public record ActivateUserCommand(@ValidEmail String email) implements Command {}
