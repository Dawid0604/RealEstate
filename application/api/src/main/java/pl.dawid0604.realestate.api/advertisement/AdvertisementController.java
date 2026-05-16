package pl.dawid0604.realestate.api.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.dawid0604.realestate.api.advertisement.request.ActivateAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.AddAdvertisementPhotoRequest;
import pl.dawid0604.realestate.api.advertisement.request.DeactivateAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.DeleteAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.PromoteAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.SearchUserAdvertisementsRequest;
import pl.dawid0604.realestate.api.advertisement.request.SetAsSoldAdvertisementRequest;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.ActivateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.AddAdvertisementPhotoCommand;
import pl.dawid0604.realestate.application.command.DeactivateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.DeleteAdvertisementCommand;
import pl.dawid0604.realestate.application.command.SetAsFeaturedAdvertisementCommand;
import pl.dawid0604.realestate.application.command.SetAsSoldAdvertisementCommand;
import pl.dawid0604.realestate.application.query.UserAdvertisementsQuery;
import pl.dawid0604.realestate.domain.shared.Page;

@RestController
@RequestMapping(value = "/api/advertisement")
@RequiredArgsConstructor(access = PACKAGE)
@Tag(name = "Advertisement", description = "Advertisements common management")
class AdvertisementController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PatchMapping("/activate")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Advertisement activation")
    @ApiResponse(responseCode = "200", description = "Advertisement successfully activated")
    @ApiResponse(responseCode = "400", description = "Advertisement is sold")
    @ApiResponse(responseCode = "404", description = "Advertisement or user not found")
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
    @ApiResponse(responseCode = "200", description = "Advertisement successfully promoted")
    @ApiResponse(responseCode = "400", description = "Advertisement is sold or inactive")
    @ApiResponse(responseCode = "404", description = "Advertisement or user not found")
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
    @ApiResponse(responseCode = "200", description = "Advertisement successfully updated")
    @ApiResponse(responseCode = "400", description = "Advertisement is sold or inactive")
    @ApiResponse(responseCode = "404", description = "Advertisement or user not found")
    void setAsSold(
            @Validated @RequestBody final SetAsSoldAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new SetAsSoldAdvertisementCommand(
                        request.slug(), request.type(), loggedUser.getUsername()));
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Advertisement delection")
    @ApiResponse(responseCode = "200", description = "Advertisement successfully deleted")
    @ApiResponse(responseCode = "400", description = "Advertisement is deleted")
    @ApiResponse(responseCode = "404", description = "Advertisement or user not found")
    void delete(
            @Validated @RequestBody final DeleteAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new DeleteAdvertisementCommand(
                        request.slug(), request.type(), loggedUser.getUsername()));
    }

    @ResponseStatus(OK)
    @PatchMapping("/deactivate")
    @Operation(summary = "Advertisement deactivation")
    @ApiResponse(responseCode = "200", description = "Advertisement successfully deactivated")
    @ApiResponse(responseCode = "400", description = "Advertisement is sold or inactive")
    @ApiResponse(responseCode = "404", description = "Advertisement or user not found")
    void deactivate(
            @Validated @RequestBody final DeactivateAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new DeactivateAdvertisementCommand(
                        request.slug(), request.type(), loggedUser.getUsername()));
    }

    @ResponseStatus(OK)
    @PatchMapping("/photo")
    @Operation(summary = "Add advertisement photo action")
    @ApiResponse(responseCode = "200", description = "Advertisement photo successfully added")
    @ApiResponse(responseCode = "404", description = "Advertisement or user not found")
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

    @GetMapping("/user")
    @ResponseStatus(OK)
    @Operation(summary = "Search user advertisements")
    @ApiResponse(
            responseCode = "200",
            description = "Advertisements has been successfully found",
            content = @Content(schema = @Schema(implementation = Page.class)))
    void searchByUser(
            @Validated @RequestBody final SearchUserAdvertisementsRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        queryBus.send(
                new UserAdvertisementsQuery(
                        loggedUser.getUsername(),
                        request.page(),
                        request.pageSize(),
                        request.statuses()));
    }
}
