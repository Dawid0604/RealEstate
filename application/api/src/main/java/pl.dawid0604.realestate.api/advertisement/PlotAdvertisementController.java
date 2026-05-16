package pl.dawid0604.realestate.api.advertisement;

import static lombok.AccessLevel.PACKAGE;

import static org.springframework.http.HttpStatus.CREATED;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.dawid0604.realestate.api.advertisement.request.CreatePlotAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.SearchPlotAdvertisementsRequest;
import pl.dawid0604.realestate.api.advertisement.request.UpdatePlotAdvertisementRequest;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.api.validation.ValidSlug;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.CreatePlotAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdatePlotAdvertisementCommand;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.query.PlotAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.SearchPlotAdvertisementsQuery;
import pl.dawid0604.realestate.domain.shared.Page;

@RestController
@RequiredArgsConstructor(access = PACKAGE)
@RequestMapping(value = "/api/advertisement/plot")
@Tag(name = "Plot advertisement", description = "Plot advertisements management")
class PlotAdvertisementController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Advertisement create action")
    @ApiResponse(responseCode = "200", description = "Advertisement successfully created")
    @ApiResponse(responseCode = "404", description = "User not found")
    void create(
            @Validated @RequestBody final CreatePlotAdvertisementRequest request,
            @AuthenticationPrincipal final AuthenticatedUser loggedUser) {

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
    }

    @PutMapping
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Advertisement update action")
    @ApiResponse(responseCode = "200", description = "Advertisement successfully updated")
    @ApiResponse(responseCode = "404", description = "User not found")
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

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Search by criteria")
    @ApiResponse(
            responseCode = "200",
            description = "Advertisements has been successfully found",
            content = @Content(schema = @Schema(implementation = Page.class)))
    void searchByCriteria(@Validated @RequestBody final SearchPlotAdvertisementsRequest request) {
        queryBus.send(
                new SearchPlotAdvertisementsQuery(
                        request.areaFrom(),
                        request.areaTo(),
                        request.priceFrom(),
                        request.priceTo(),
                        request.pricePerSquareMeterFrom(),
                        request.pricePerSquareMeterTo(),
                        request.page(),
                        request.pageSize(),
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
    @ApiResponse(responseCode = "404", description = "Advertisement not found")
    PlotAdvertisementDetailsDto searchDetailsById(
            @Validated @ValidSlug @PathVariable("slug") final String slug) {

        return queryBus.send(new PlotAdvertisementDetailsQuery(slug));
    }
}
