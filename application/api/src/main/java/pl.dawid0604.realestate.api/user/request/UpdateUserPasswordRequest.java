package pl.dawid0604.realestate.api.user.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidEmail;
import pl.dawid0604.realestate.api.validation.ValidPassword;

@Schema(description = "User account update password request")
public record UpdateUserPasswordRequest(
        @Schema(description = "User email", example = "anyMail@mail.com") @ValidEmail String email,
        @ValidPassword @Schema(description = "Current password", example = "John")
                String currentPassword,
        @ValidPassword @Schema(description = "New password", example = "Doe") String newPassword) {}
