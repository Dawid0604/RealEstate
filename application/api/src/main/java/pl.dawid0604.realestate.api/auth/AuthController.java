package pl.dawid0604.realestate.api.auth;

import static lombok.AccessLevel.PACKAGE;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.dawid0604.realestate.api.auth.request.LoginRequest;
import pl.dawid0604.realestate.api.auth.request.RegisterRequest;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.command.LoginUserCommand;
import pl.dawid0604.realestate.application.command.RegisterUserCommand;
import pl.dawid0604.realestate.domain.shared.user.LoginResponse;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor(access = PACKAGE)
class AuthController {
    private final CommandBus commandBus;

    @ResponseStatus(OK)
    @PostMapping("/login")
    LoginResponse login(@RequestBody final LoginRequest payload) {
        return commandBus.send(new LoginUserCommand(payload.username(), payload.password()));
    }

    @ResponseStatus(CREATED)
    @PostMapping("/register")
    void register(@RequestBody final RegisterRequest payload) {
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
