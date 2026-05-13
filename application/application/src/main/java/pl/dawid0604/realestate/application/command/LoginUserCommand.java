package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidPassword;

public record LoginUserCommand(@ValidEmail String email, @ValidPassword String password)
        implements Command {}
