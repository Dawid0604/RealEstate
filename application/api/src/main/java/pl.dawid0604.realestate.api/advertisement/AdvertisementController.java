/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ProblemDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.dawid0604.realestate.api.advertisement.request.ActivateAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.AddAdvertisementPhotoRequest;
import pl.dawid0604.realestate.api.advertisement.request.DeactivateAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.DeleteAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.PromoteAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.SearchUserAdvertisementsRequest;
import pl.dawid0604.realestate.api.advertisement.request.SetAsSoldAdvertisementRequest;
import pl.dawid0604.realestate.api.config.openapi.OpenApiProperties;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.api.validation.ValidPageNumber;
import pl.dawid0604.realestate.api.validation.ValidPageSize;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.ActivateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.AddAdvertisementPhotoCommand;
import pl.dawid0604.realestate.application.command.DeactivateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.DeleteAdvertisementCommand;
import pl.dawid0604.realestate.application.command.SetAsFeaturedAdvertisementCommand;
import pl.dawid0604.realestate.application.command.SetAsSoldAdvertisementCommand;
import pl.dawid0604.realestate.application.dto.advertisement.UserAdvertisementCardDto;
import pl.dawid0604.realestate.application.query.UserAdvertisementsQuery;
import pl.dawid0604.realestate.domain.shared.Page;

@RestController
@RequestMapping("/api/advertisement")
@RequiredArgsConstructor(access = PACKAGE)
@Tag(name = "Advertisement", description = "Advertisements common management")
class AdvertisementController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PatchMapping("/activate")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Advertisement activation")
    @SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
    @ApiResponse(responseCode = "200", description = "Advertisement successfully activated")
    @ApiResponse(
            responseCode = "400",
            description = "Advertisement is sold",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Advertisement or user not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    void activate(
            @Validated @RequestBody final ActivateAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new ActivateAdvertisementCommand(
                        request.slug(), request.type(), loggedUser.getUsername()));
    }

    @PatchMapping("/promote")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Advertisement promotion")
    @SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
    @ApiResponse(responseCode = "200", description = "Advertisement successfully promoted")
    @ApiResponse(
            responseCode = "400",
            description = "Advertisement is sold or inactive",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Advertisement or user not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    void promote(
            @Validated @RequestBody final PromoteAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new SetAsFeaturedAdvertisementCommand(
                        request.slug(), request.type(), loggedUser.getUsername()));
    }

    @PatchMapping("/sold")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Set advertisement as sold")
    @SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
    @ApiResponse(responseCode = "200", description = "Advertisement successfully updated")
    @ApiResponse(
            responseCode = "400",
            description = "Advertisement is sold or inactive",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Advertisement or user not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    void setAsSold(
            @Validated @RequestBody final SetAsSoldAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new SetAsSoldAdvertisementCommand(
                        request.slug(), request.type(), loggedUser.getUsername()));
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Advertisement deletion")
    @SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
    @ApiResponse(responseCode = "200", description = "Advertisement successfully deleted")
    @ApiResponse(
            responseCode = "400",
            description = "Advertisement is deleted",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Advertisement or user not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    void delete(
            @Validated @RequestBody final DeleteAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new DeleteAdvertisementCommand(
                        request.slug(), request.type(), loggedUser.getUsername()));
    }

    @ResponseStatus(NO_CONTENT)
    @PatchMapping("/deactivate")
    @Operation(summary = "Advertisement deactivation")
    @SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
    @ApiResponse(responseCode = "200", description = "Advertisement successfully deactivated")
    @ApiResponse(
            responseCode = "400",
            description = "Advertisement is sold or inactive",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Advertisement or user not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    void deactivate(
            @Validated @RequestBody final DeactivateAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new DeactivateAdvertisementCommand(
                        request.slug(), request.type(), loggedUser.getUsername()));
    }

    @ResponseStatus(NO_CONTENT)
    @PatchMapping("/photo")
    @Operation(summary = "Add advertisement photo action")
    @SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
    @ApiResponse(responseCode = "200", description = "Advertisement photo successfully added")
    @ApiResponse(
            responseCode = "404",
            description = "Advertisement or user not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    void addPhoto(
            @Validated @RequestBody final AddAdvertisementPhotoRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new AddAdvertisementPhotoCommand(
                        request.slug(),
                        request.type(),
                        request.photoUrl(),
                        request.position(),
                        loggedUser.getUsername()));
    }

    @ResponseStatus(OK)
    @PostMapping("/user")
    @Operation(summary = "Search user advertisements")
    @SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
    @ApiResponse(
            responseCode = "200",
            description = "Advertisements has been successfully found",
            content = @Content(schema = @Schema(implementation = Page.class)))
    Page<UserAdvertisementCardDto> searchByUser(
            @Validated @RequestBody final SearchUserAdvertisementsRequest request,
            @ValidPageNumber @RequestParam(value = "page", required = false, defaultValue = "0")
                    final int page,
            @RequestParam(value = "size", required = false, defaultValue = "25") @ValidPageSize
                    final int pageSize,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        return queryBus.send(
                new UserAdvertisementsQuery(
                        loggedUser.getUsername(), page, pageSize, request.statuses()));
    }
}
