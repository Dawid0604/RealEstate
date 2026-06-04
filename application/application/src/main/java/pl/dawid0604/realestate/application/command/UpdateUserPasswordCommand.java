/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

public record UpdateUserPasswordCommand(String email, String currentPassword, String newPassword)
        implements Command {}
