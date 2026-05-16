package pl.dawid0604.realestate.api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidEmail;
import pl.dawid0604.realestate.api.validation.ValidFirstName;
import pl.dawid0604.realestate.api.validation.ValidLastName;
import pl.dawid0604.realestate.api.validation.ValidPassword;
import pl.dawid0604.realestate.api.validation.ValidUserType;
import pl.dawid0604.realestate.domain.UserType;

@Schema(description = "Register credentials")
public record RegisterRequest(
        @ValidEmail @Schema(description = "User email", example = "anyMail@mail.com")
                String username,
        @ValidPassword @Schema(description = "User password", example = "AnyPassword$1.")
                String password,
        @ValidFirstName @Schema(description = "User first name", example = "John") String firstName,
        @ValidLastName @Schema(description = "User last name", example = "Doe") String lastName,
        @ValidUserType @Schema(description = "User account type", example = "AGENCY") UserType type,
        @Schema(description = "User notification email", example = "anyMail@mail.com")
                String notificationEmail,
        @Schema(description = "User notification phone number", example = "123456789")
                String notificationPhoneNumber) {}
