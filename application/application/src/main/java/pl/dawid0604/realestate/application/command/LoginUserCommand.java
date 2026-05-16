package pl.dawid0604.realestate.application.command;

public record LoginUserCommand(String email, String password) implements Command {}
