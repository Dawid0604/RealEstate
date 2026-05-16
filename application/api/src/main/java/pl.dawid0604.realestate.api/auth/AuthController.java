package pl.dawid0604.realestate.api.auth;

import static lombok.AccessLevel.PACKAGE;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ProblemDetail;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.dawid0604.realestate.api.auth.request.LoginRequest;
import pl.dawid0604.realestate.api.auth.request.RefreshTokenRequest;
import pl.dawid0604.realestate.api.auth.request.RegisterRequest;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.command.LoginUserCommand;
import pl.dawid0604.realestate.application.command.RefreshTokenCommand;
import pl.dawid0604.realestate.application.command.RegisterUserCommand;
import pl.dawid0604.realestate.application.dto.auth.TokenResponseDto;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor(access = PACKAGE)
@Tag(name = "Authentication", description = "User access management")
class AuthController {
    private final CommandBus commandBus;

    @ResponseStatus(OK)
    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticates user and returns JWT access token and refresh token")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully authenticated",
            content = @Content(schema = @Schema(implementation = TokenResponseDto.class)))
    @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(
            responseCode = "422",
            description = "Validation failed",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    TokenResponseDto login(@Validated @RequestBody final LoginRequest payload) {
        return commandBus.send(new LoginUserCommand(payload.username(), payload.password()));
    }

    @ResponseStatus(OK)
    @PostMapping("/token/refresh")
    @Operation(
            summary = "Refresh token action",
            description =
                    "Refreshes and returns JWT access and refresh token. Old refresh token has been invalidated")
    @ApiResponse(responseCode = "200", description = "Successfully refreshed")
    @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    TokenResponseDto refreshToken(@Validated @RequestBody final RefreshTokenRequest request) {
        return commandBus.send(new RefreshTokenCommand(request.refreshToken()));
    }

    @ResponseStatus(CREATED)
    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Creates user account")
    @ApiResponse(responseCode = "201", description = "Successfully registered")
    @ApiResponse(
            responseCode = "409",
            description = "Email already exists",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(
            responseCode = "422",
            description = "Validation failed",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    void register(@Validated @RequestBody final RegisterRequest payload) {
        commandBus.send(
                new RegisterUserCommand(
                        payload.username(),
                        payload.password(),
                        payload.firstName(),
                        payload.lastName(),
                        payload.type(),
                        payload.notificationEmail(),
                        payload.notificationPhoneNumber()));
    }
}
