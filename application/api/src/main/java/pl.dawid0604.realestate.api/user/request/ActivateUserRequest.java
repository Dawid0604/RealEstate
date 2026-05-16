package pl.dawid0604.realestate.api.user.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidEmail;

@Schema(description = "User activation request")
public record ActivateUserRequest(
        @Schema(description = "User email", example = "anyMail@mail.com") @ValidEmail
                String email) {}
