package pl.dawid0604.realestate.api.user;

import static lombok.AccessLevel.PACKAGE;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.dawid0604.realestate.api.config.openapi.OpenApiProperties;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.api.shared.HasAdminRole;
import pl.dawid0604.realestate.api.user.request.ActivateUserRequest;
import pl.dawid0604.realestate.api.user.request.BanUserRequest;
import pl.dawid0604.realestate.api.user.request.DeleteUserRequest;
import pl.dawid0604.realestate.api.user.request.UnbanUserRequest;
import pl.dawid0604.realestate.api.user.request.UpdateUserPasswordRequest;
import pl.dawid0604.realestate.api.user.request.UpdateUserProfileRequest;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.ActivateUserCommand;
import pl.dawid0604.realestate.application.command.BanUserCommand;
import pl.dawid0604.realestate.application.command.DeleteUserCommand;
import pl.dawid0604.realestate.application.command.UnbanUserCommand;
import pl.dawid0604.realestate.application.command.UpdateUserPasswordCommand;
import pl.dawid0604.realestate.application.command.UpdateUserProfileCommand;
import pl.dawid0604.realestate.application.command.UserLogoutCommand;
import pl.dawid0604.realestate.application.dto.user.UserProfileDto;
import pl.dawid0604.realestate.application.query.UserProfileQuery;

@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor(access = PACKAGE)
@Tag(name = "User", description = "User account management")
@SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
class UserController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @ResponseStatus(NO_CONTENT)
    @GetMapping("/logout")
    @Operation(summary = "Logout action")
    @ApiResponse(responseCode = "200", description = "Successfully logged out")
    void logout(@AuthenticationPrincipal final AuthenticatedUser user) {
        commandBus.send(new UserLogoutCommand(user.getUsername()));
    }

    @ResponseStatus(NO_CONTENT)
    @PatchMapping("/activate")
    @Operation(summary = "User account activation")
    @ApiResponse(responseCode = "200", description = "User account successfully activated")
    @ApiResponse(responseCode = "400", description = "User account already active or banned")
    void activate(@Validated @RequestBody final ActivateUserRequest request) {
        commandBus.send(new ActivateUserCommand(request.email()));
    }

    @HasAdminRole
    @PatchMapping("/ban")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "User account ban action")
    @ApiResponse(responseCode = "200", description = "User account successfully banned")
    @ApiResponse(responseCode = "400", description = "User account already banned")
    void ban(@Validated @RequestBody final BanUserRequest request) {
        commandBus.send(new BanUserCommand(request.email()));
    }

    @HasAdminRole
    @PatchMapping("/unban")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "User account unban action")
    @ApiResponse(responseCode = "200", description = "User account successfully unbanned")
    @ApiResponse(responseCode = "400", description = "User account is not banned")
    void ban(@Validated @RequestBody final UnbanUserRequest request) {
        commandBus.send(new UnbanUserCommand(request.email()));
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "User account delete action")
    @ApiResponse(responseCode = "200", description = "User account successfully deleted")
    @ApiResponse(responseCode = "404", description = "User account not found")
    void delete(@Validated @RequestBody final DeleteUserRequest request) {
        commandBus.send(new DeleteUserCommand(request.email()));
    }

    @PutMapping
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "User account update action")
    @ApiResponse(responseCode = "200", description = "User account successfully updated")
    @ApiResponse(responseCode = "404", description = "User account not found")
    void updateUserProfile(@Validated @RequestBody final UpdateUserProfileRequest request) {
        commandBus.send(
                new UpdateUserProfileCommand(
                        request.email(),
                        request.avatarUrl(),
                        request.notificationEmail(),
                        request.notificationPhoneNumber(),
                        request.firstName(),
                        request.lastName(),
                        request.type()));
    }

    @ResponseStatus(NO_CONTENT)
    @PatchMapping("/password")
    @Operation(summary = "User account password update action")
    @ApiResponse(responseCode = "200", description = "User account successfully updated")
    @ApiResponse(responseCode = "404", description = "User account not found")
    void updatePassword(@Validated @RequestBody final UpdateUserPasswordRequest request) {
        commandBus.send(
                new UpdateUserPasswordCommand(
                        request.email(), request.currentPassword(), request.newPassword()));
    }

    @ResponseStatus(OK)
    @GetMapping("/profile")
    @Operation(summary = "Current logged user profile")
    @ApiResponse(responseCode = "200", description = "User account found")
    @ApiResponse(responseCode = "404", description = "User account not found")
    UserProfileDto getProfile(@AuthenticationPrincipal final AuthenticatedUser loggedUser) {
        return queryBus.send(new UserProfileQuery(loggedUser.email()));
    }
}
