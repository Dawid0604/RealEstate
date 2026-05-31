/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.auth;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.dawid0604.realestate.api.auth.request.LoginRequest;
import pl.dawid0604.realestate.api.auth.request.RefreshTokenRequest;
import pl.dawid0604.realestate.api.auth.request.RegisterRequest;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.LoginUserCommand;
import pl.dawid0604.realestate.application.command.RefreshTokenCommand;
import pl.dawid0604.realestate.application.command.RegisterUserCommand;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;

@WebMvcTest(AuthController.class)
@Import(AuthControllerTest.TestSecurityConfig.class)
class AuthControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private TokenRepository tokenRepository;
    @MockitoBean private CommandBus commandBus;
    @MockitoBean private QueryBus queryBus;

    private static final String USERNAME = "test_username@mail.com";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(final HttpSecurity http) {
            return http.authorizeHttpRequests(
                            auth ->
                                    auth.requestMatchers("/api/auth/login", "/api/auth/register")
                                            .permitAll()
                                            .anyRequest()
                                            .authenticated())
                    .build();
        }
    }

    @Nested
    final class LoginTests {

        @Test
        @DisplayName("Should login successfully")
        void shouldLoginSuccessfully() throws Exception {
            // Given
            final LoginRequest request = new LoginRequest(USERNAME, "anyPassword");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/login")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(commandBus).send(new LoginUserCommand(request.username(), request.password()));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when username is invalid")
        void shouldReturnBadRequestWhenUsernameIsInvalid(final String username) throws Exception {
            // Given
            final LoginRequest request = new LoginRequest(username, "anyPassword");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/login")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when password is invalid")
        void shouldReturnBadRequestWhenPasswordIsInvalid(final String password) throws Exception {
            // Given
            final LoginRequest request = new LoginRequest(USERNAME, password);

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/login")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class RegisterTests {

        @Test
        @DisplayName("Should register successfully")
        void shouldRegisterSuccessfully() throws Exception {
            // Given
            final RegisterRequest request =
                    new RegisterRequest(
                            USERNAME,
                            "anyPassword",
                            "John",
                            "Doe",
                            UserType.AGENCY,
                            "anyNotificationEmail@mail.com",
                            "123456789");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/register")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(commandBus)
                    .send(
                            new RegisterUserCommand(
                                    request.username(),
                                    request.password(),
                                    request.firstName(),
                                    request.lastName(),
                                    request.type(),
                                    request.notificationEmail(),
                                    request.notificationPhoneNumber()));
        }

        @Test
        @DisplayName("Should register successfully with null notificationEmail")
        void shouldRegisterSuccessfullyWithNullNotificationEmail() throws Exception {
            // Given
            final RegisterRequest request =
                    new RegisterRequest(
                            USERNAME,
                            "anyPassword",
                            "John",
                            "Doe",
                            UserType.AGENCY,
                            null,
                            "123456789");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/register")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(commandBus)
                    .send(
                            new RegisterUserCommand(
                                    request.username(),
                                    request.password(),
                                    request.firstName(),
                                    request.lastName(),
                                    request.type(),
                                    request.notificationEmail(),
                                    request.notificationPhoneNumber()));
        }

        @Test
        @DisplayName("Should register successfully with null notificationPhoneNumber")
        void shouldRegisterSuccessfullyWithNullNotificationPhoneNumber() throws Exception {
            // Given
            final RegisterRequest request =
                    new RegisterRequest(
                            USERNAME,
                            "anyPassword",
                            "John",
                            "Doe",
                            UserType.AGENCY,
                            "anyNotificationEmail@mail.com",
                            null);

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/register")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(commandBus)
                    .send(
                            new RegisterUserCommand(
                                    request.username(),
                                    request.password(),
                                    request.firstName(),
                                    request.lastName(),
                                    request.type(),
                                    request.notificationEmail(),
                                    request.notificationPhoneNumber()));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when username is invalid")
        void shouldReturnBadRequestWhenUsernameIsInvalid(final String username) throws Exception {
            // Given
            final RegisterRequest request =
                    new RegisterRequest(
                            username,
                            "anyPassword",
                            "John",
                            "Doe",
                            UserType.AGENCY,
                            "anyNotificationEmail@mail.com",
                            "123456789");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/register")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when password is invalid")
        void shouldReturnBadRequestWhenPasswordIsInvalid(final String password) throws Exception {
            // Given
            final RegisterRequest request =
                    new RegisterRequest(
                            USERNAME,
                            password,
                            "John",
                            "Doe",
                            UserType.AGENCY,
                            "anyNotificationEmail@mail.com",
                            "123456789");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/register")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when firstName is invalid")
        void shouldReturnBadRequestWhenFirstNameIsInvalid(final String firstName) throws Exception {
            // Given
            final RegisterRequest request =
                    new RegisterRequest(
                            USERNAME,
                            "anyPassword",
                            firstName,
                            "Doe",
                            UserType.AGENCY,
                            "anyNotificationEmail@mail.com",
                            "123456789");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/register")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when lastName is invalid")
        void shouldReturnBadRequestWhenLastNameIsInvalid(final String lastName) throws Exception {
            // Given
            final RegisterRequest request =
                    new RegisterRequest(
                            USERNAME,
                            "anyPassword",
                            "John",
                            lastName,
                            UserType.AGENCY,
                            "anyNotificationEmail@mail.com",
                            "123456789");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/register")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @Test
        @DisplayName("Should return bad request when type is invalid")
        void shouldReturnBadRequestWhenTypeIsInvalid() throws Exception {
            // Given
            final RegisterRequest request =
                    new RegisterRequest(
                            USERNAME,
                            "anyPassword",
                            "John",
                            "Doe",
                            null,
                            "anyNotificationEmail@mail.com",
                            "123456789");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/register")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "",
                    " ",
                    "plainaddress",
                    "@domain.com",
                    "test@",
                    "test@domain",
                    "test.domain.com",
                    "test@domain.",
                    "test@domain.c",
                    "test@domain.12",
                    "test@sub_domain.com",
                    "test space@domain.com",
                    "test#@domain.com",
                    "test@dom@ain.com"
                })
        @DisplayName("Should return bad request when notificationEmail is invalid")
        void shouldReturnBadRequestWhenNotificationEmailIsInvalid(final String notificationEmail)
                throws Exception {
            // Given
            final RegisterRequest request =
                    new RegisterRequest(
                            USERNAME,
                            "anyPassword",
                            "John",
                            "Doe",
                            UserType.AGENCY,
                            notificationEmail,
                            "123456789");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/register")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "",
                    " ",
                    "123456",
                    "+48123",
                    "1234567890123456",
                    "+48 123 456 789 012",
                    "+48 123-ABC-789",
                    "123-456-789#",
                    "48_123_456",
                    "48.123.456",
                    "++481234567",
                    "4812345+678"
                })
        @DisplayName("Should return bad request when notificationPhoneNumber is invalid")
        void shouldReturnBadRequestWhenNotificationPhoneNumberIsInvalid(
                final String notificationPhoneNumber) throws Exception {

            // Given
            final RegisterRequest request =
                    new RegisterRequest(
                            USERNAME,
                            "anyPassword",
                            "John",
                            "Doe",
                            UserType.AGENCY,
                            "anyNotificationEmail@mail.com",
                            notificationPhoneNumber);

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/register")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class RefreshTokenTests {

        @Test
        @WithMockUser
        @DisplayName("Should refresh token successfully")
        void shouldRefreshTokenSuccessfully() throws Exception {
            // Given
            final RefreshTokenRequest request = new RefreshTokenRequest("anyRefreshToken");

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/token/refresh")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(commandBus).send(new RefreshTokenCommand(request.refreshToken()));
        }

        @WithMockUser
        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when refresh token is invalid")
        void shouldReturnBadRequestWhenRefreshTokenIsInvalid(final String refreshToken)
                throws Exception {

            // Given
            final RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

            // When
            // Then
            mockMvc.perform(
                            post("/api/auth/token/refresh")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }
}
