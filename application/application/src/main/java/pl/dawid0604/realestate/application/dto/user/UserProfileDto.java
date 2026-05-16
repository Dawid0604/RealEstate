/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.UserType;

import java.util.UUID;

@Schema(description = "User profile")
public record UserProfileDto(
        @Schema(description = "User id", example = "019e2325-d92b-70ad-94e3-609123e34a79")
                UUID userId,
        @Schema(description = "User email", example = "anyMail@mail.com") String email,
        @Schema(description = "User first name and last name", example = "John Doe")
                String fullName,
        @Schema(description = "User contact phone number", example = "123456789")
                String contactPhoneNumber,
        @Schema(description = "User contact email", example = "anyMail@mail.com")
                String contactEmail,
        @Schema(description = "User avatar url", example = "https://anyImage.com/avatar/1")
                String avatarUrl,
        @Schema(description = "User role") UserRole role,
        @Schema(description = "User type") UserType type,
        @Schema(description = "User status") UserStatus status) {}
