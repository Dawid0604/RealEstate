/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

public record UserLogoutCommand(String userEmail) implements Command {}
