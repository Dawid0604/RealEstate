/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.user.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidEmail;
import pl.dawid0604.realestate.api.validation.ValidFirstName;
import pl.dawid0604.realestate.api.validation.ValidLastName;
import pl.dawid0604.realestate.api.validation.ValidUrl;
import pl.dawid0604.realestate.api.validation.ValidUserType;
import pl.dawid0604.realestate.domain.UserType;

@Schema(description = "User account update avatar request")
public record UpdateUserProfileRequest(
        @Schema(description = "User email", example = "anyMail@mail.com") @ValidEmail String email,
        @ValidUrl @Schema(description = "Avatar url", example = "https://anyImage.com/avatar.png")
                String avatarUrl,
        @Schema(description = "Notification email", example = "anyMail@mail.com")
                String notificationEmail,
        @Schema(description = "Notification phone number", example = "123456789")
                String notificationPhoneNumber,
        @ValidFirstName @Schema(description = "User first name", defaultValue = "John")
                String firstName,
        @ValidLastName @Schema(description = "User last name", defaultValue = "Doe")
                String lastName,
        @ValidUserType @Schema(description = "User type") UserType type) {}
