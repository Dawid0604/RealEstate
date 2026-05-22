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

import pl.dawid0604.realestate.api.advertisement.request.CreatePlotAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.SearchPlotAdvertisementsRequest;
import pl.dawid0604.realestate.api.advertisement.request.UpdatePlotAdvertisementRequest;
import pl.dawid0604.realestate.api.config.openapi.OpenApiProperties;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.api.validation.ValidPageNumber;
import pl.dawid0604.realestate.api.validation.ValidPageSize;
import pl.dawid0604.realestate.api.validation.ValidSlug;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.CreatePlotAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdatePlotAdvertisementCommand;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.query.PlotAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.SearchPlotAdvertisementsQuery;
import pl.dawid0604.realestate.domain.shared.Page;

@RestController
@RequiredArgsConstructor(access = PACKAGE)
@RequestMapping("/api/advertisement/plot")
@Tag(name = "Plot advertisement", description = "Plot advertisements management")
class PlotAdvertisementController {
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
            @Validated @RequestBody final CreatePlotAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        final String slug =
                commandBus.send(
                        new CreatePlotAdvertisementCommand(
                                request.title(),
                                request.description(),
                                request.price(),
                                request.localityId(),
                                loggedUser.getUsername(),
                                Mapper.mapPhotos(request.photos()),
                                request.buildingType().name(),
                                request.area(),
                                request.claims(),
                                request.featured()));

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
            @Validated @RequestBody final UpdatePlotAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

        commandBus.send(
                new UpdatePlotAdvertisementCommand(
                        request.slug(),
                        request.title(),
                        request.description(),
                        request.price(),
                        request.localityId(),
                        loggedUser.getUsername(),
                        request.buildingType().name(),
                        request.area(),
                        request.claims(),
                        request.featured()));
    }

    @ResponseStatus(OK)
    @PostMapping("/find")
    @Operation(summary = "Search by criteria")
    @ApiResponse(
            responseCode = "200",
            description = "Advertisements has been successfully found",
            content = @Content(schema = @Schema(implementation = Page.class)))
    Page<PlotAdvertisementCardDto> searchByCriteria(
            @Validated @RequestBody final SearchPlotAdvertisementsRequest request,
            @ValidPageNumber @RequestParam(value = "page", required = false, defaultValue = "0")
                    final int page,
            @RequestParam(value = "size", required = false, defaultValue = "25") @ValidPageSize
                    final int pageSize) {

        return queryBus.send(
                new SearchPlotAdvertisementsQuery(
                        request.areaFrom(),
                        request.areaTo(),
                        request.priceFrom(),
                        request.priceTo(),
                        request.pricePerSquareMeterFrom(),
                        request.pricePerSquareMeterTo(),
                        page,
                        pageSize,
                        Mapper.mapEnumCollectionToSet(request.types()),
                        request.dateFrom(),
                        request.dateTo(),
                        request.localityId()));
    }

    @ResponseStatus(OK)
    @GetMapping("/{slug}")
    @Operation(summary = "Search details by slug")
    @ApiResponse(
            responseCode = "200",
            description = "Advertisement has been successfully found",
            content =
                    @Content(schema = @Schema(implementation = PlotAdvertisementDetailsDto.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Advertisement not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    PlotAdvertisementDetailsDto searchDetailsById(
            @Validated @ValidSlug @PathVariable("slug") final String slug) {

        return queryBus.send(new PlotAdvertisementDetailsQuery(slug));
    }
}
