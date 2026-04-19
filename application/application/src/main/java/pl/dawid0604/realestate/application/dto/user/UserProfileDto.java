/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.user;

import java.util.UUID;

public record UserProfileDto(
        UUID userId,
        String email,
        String fullName,
        String contactPhoneNumber,
        String contactEmail,
        String avatarUrl,
        String role,
        String type,
        String status) {}
