package pl.dawid0604.realestate.api.user;

import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.dawid0604.realestate.api.config.openapi.OpenApiProperties;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;

@RestController
@SecurityRequirement(name = OpenApiProperties.AUTHENTICATION_REQUIREMENT)
@RequestMapping(value = "/api/user")
class UserController {

    @GetMapping
    @ResponseStatus(OK)
    String sayHello(@AuthenticationPrincipal AuthenticatedUser user) {
        return "Hello " + user.getUsername();
    }
}
