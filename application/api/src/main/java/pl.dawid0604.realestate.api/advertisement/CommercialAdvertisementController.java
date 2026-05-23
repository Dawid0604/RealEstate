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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import pl.dawid0604.realestate.api.advertisement.request.CreateCommercialAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.SearchCommercialAdvertisementsRequest;
import pl.dawid0604.realestate.api.advertisement.request.UpdateCommercialAdvertisementRequest;
import pl.dawid0604.realestate.api.config.openapi.OpenApiProperties;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.api.validation.ValidPageNumber;
import pl.dawid0604.realestate.api.validation.ValidPageSize;
import pl.dawid0604.realestate.api.validation.ValidSlug;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.CreateCommercialAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdateCommercialAdvertisementCommand;
import pl.dawid0604.realestate.application.dto.advertisement.CommercialAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.CommercialAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.query.CommercialAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.SearchCommercialAdvertisementsQuery;
import pl.dawid0604.realestate.domain.shared.Page;

@RestController
@SuppressWarnings("CPD-START")
@RequiredArgsConstructor(access = PACKAGE)
@RequestMapping("/api/advertisement/commercial")
@Tag(name = "Commercial advertisement", description = "Commercial advertisements management")
class CommercialAdvertisementController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping
    @Operation(summary = "Advertisement create action")
    @SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
    @ApiResponse(responseCode = "200", description = "Advertisement successfully created")
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<Void> create(
            @Validated @RequestBody final CreateCommercialAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        final String slug =
                commandBus.send(
                        new CreateCommercialAdvertisementCommand(
                                request.getTitle(),
                                request.getDescription(),
                                request.getPrice(),
                                request.getLocalityId(),
                                loggedUser.getUsername(),
                                request.getNumberOfRooms(),
                                request.getFloor(),
                                request.getFloors(),
                                request.getBuiltYear(),
                                request.getTypeOfMarket().name(),
                                Mapper.mapPhotos(request.getPhotos()),
                                request.getBuildingType().name(),
                                request.getArea(),
                                request.getClaims(),
                                request.getFeatured()));

        final var locationHeader =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{slug}")
                        .buildAndExpand(slug)
                        .toUri();

        return ResponseEntity.created(locationHeader).build();
    }

    @PutMapping
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Advertisement update action")
    @SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
    @ApiResponse(responseCode = "200", description = "Advertisement successfully updated")
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    void update(
            @Validated @RequestBody final UpdateCommercialAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new UpdateCommercialAdvertisementCommand(
                        request.getSlug(),
                        request.getTitle(),
                        request.getDescription(),
                        request.getPrice(),
                        request.getLocalityId(),
                        loggedUser.getUsername(),
                        request.getNumberOfRooms(),
                        request.getFloor(),
                        request.getFloors(),
                        request.getBuiltYear(),
                        request.getTypeOfMarket().name(),
                        request.getBuildingType().name(),
                        request.getArea(),
                        request.getClaims(),
                        request.getFeatured()));
    }

    @ResponseStatus(OK)
    @PostMapping("/find")
    @Operation(summary = "Search by criteria")
    @ApiResponse(
            responseCode = "200",
            description = "Advertisements has been successfully found",
            content = @Content(schema = @Schema(implementation = Page.class)))
    Page<CommercialAdvertisementCardDto> searchByCriteria(
            @Validated @RequestBody final SearchCommercialAdvertisementsRequest request,
            @ValidPageNumber @RequestParam(value = "page", required = false, defaultValue = "0")
                    final int page,
            @RequestParam(value = "size", required = false, defaultValue = "25") @ValidPageSize
                    final int pageSize) {

        return queryBus.send(
                new SearchCommercialAdvertisementsQuery(
                        request.getAreaFrom(),
                        request.getAreaTo(),
                        request.getPriceFrom(),
                        request.getPriceTo(),
                        request.getPricePerSquareMeterFrom(),
                        request.getPricePerSquareMeterTo(),
                        page,
                        pageSize,
                        Mapper.mapEnumCollectionToSet(request.getTypes()),
                        Mapper.mapEnumCollectionToSet(request.getTypeOfMarkets()),
                        request.getFloorFrom(),
                        request.getFloorTo(),
                        request.getFloorsFrom(),
                        request.getFloorsTo(),
                        request.getNumberOfRoomsFrom(),
                        request.getNumberOfRoomsTo(),
                        request.getBuiltYearFrom(),
                        request.getBuiltYearTo(),
                        request.getDateFrom(),
                        request.getDateTo(),
                        request.getLocalityId()));
    }

    @ResponseStatus(OK)
    @GetMapping("/{slug}")
    @Operation(summary = "Search details by slug")
    @ApiResponse(
            responseCode = "200",
            description = "Advertisement has been successfully found",
            content =
                    @Content(
                            schema =
                                    @Schema(
                                            implementation =
                                                    CommercialAdvertisementDetailsDto.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Advertisement not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    CommercialAdvertisementDetailsDto searchDetailsById(
            @Validated @ValidSlug @PathVariable("slug") final String slug) {

        return queryBus.send(new CommercialAdvertisementDetailsQuery(slug));
    }
}
