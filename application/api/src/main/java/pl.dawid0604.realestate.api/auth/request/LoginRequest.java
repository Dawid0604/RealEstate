/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidEmail;
import pl.dawid0604.realestate.api.validation.ValidPassword;

@Schema(description = "Login credentials")
public record LoginRequest(
        @ValidEmail @Schema(description = "User email", example = "anyMail@mail.com")
                String username,
        @ValidPassword @Schema(description = "User password", example = "AnyPassword$1.")
                String password) {}
