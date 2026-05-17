package pl.dawid0604.realestate.api.locality;

import static lombok.AccessLevel.PACKAGE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import pl.dawid0604.realestate.api.common.HasAdminRole;
import pl.dawid0604.realestate.api.config.openapi.OpenApiProperties;
import pl.dawid0604.realestate.api.locality.request.CreateLocalityRequest;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.CreateLocalityCommand;
import pl.dawid0604.realestate.application.dto.locality.LocalityDto;
import pl.dawid0604.realestate.application.query.FindLocalitiesQuery;
import pl.dawid0604.realestate.application.query.FindLocalityByIdQuery;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/locality")
@RequiredArgsConstructor(access = PACKAGE)
@Tag(name = "Locality", description = "Locality management")
class LocalityController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping
    @HasAdminRole
    @Operation(summary = "Create locality")
    @ApiResponse(responseCode = "201", description = "Locality successfully created")
    @ApiResponse(
            responseCode = "400",
            description = "Locality with given name already exists",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
    ResponseEntity<Void> create(@RequestBody @Validated final CreateLocalityRequest request) {
        final UUID id = commandBus.send(new CreateLocalityCommand(request.name()));

        final var locationHeader =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(id)
                        .toUri();

        return ResponseEntity.created(locationHeader).build();
    }

    @GetMapping
    @Operation(summary = "Find all localities")
    @ApiResponse(
            responseCode = "200",
            description = "Localities has been successfully found",
            content = @Content(schema = @Schema(implementation = LocalityDto.class)))
    @ResponseStatus(HttpStatus.OK)
    Iterable<LocalityDto> findAll() {
        return queryBus.send(new FindLocalitiesQuery());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find locality by id")
    @ApiResponse(
            responseCode = "200",
            description = "Locality has been successfully found",
            content = @Content(schema = @Schema(implementation = LocalityDto.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Locality not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    LocalityDto findById(@PathVariable("id") final UUID localityId) {
        return queryBus.send(new FindLocalityByIdQuery(localityId));
    }
}
