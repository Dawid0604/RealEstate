/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.user;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.api.user.request.ActivateUserRequest;
import pl.dawid0604.realestate.api.user.request.BanUserRequest;
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
import pl.dawid0604.realestate.application.query.UserProfileQuery;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;

@EnableMethodSecurity
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private TokenRepository tokenRepository;
    @MockitoBean private CommandBus commandBus;
    @MockitoBean private QueryBus queryBus;

    private static final String USERNAME = "test_username@mail.com";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Nested
    final class LogoutTests {

        @Test
        @DisplayName("Should logout successfully")
        void shouldLogoutSuccessfully() throws Exception {
            // Given
            // When
            // Then
            mockMvc.perform(
                            get("/api/user/logout")
                                    .with(csrf())
                                    .with(authentication(getUserAuth())))
                    .andExpect(status().isNoContent());

            verify(commandBus).send(new UserLogoutCommand(USERNAME));
        }
    }

    @Nested
    final class ActivateTests {

        @Test
        @DisplayName("Should activate successfully")
        void shouldActivateSuccessfully() throws Exception {
            // Given
            final ActivateUserRequest request = new ActivateUserRequest(USERNAME);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/user/activate")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus).send(new ActivateUserCommand(USERNAME));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when payload is invalid")
        void shouldReturnBadRequestWhenPayloadIsInvalid(final String username) throws Exception {
            // Given
            final ActivateUserRequest request = new ActivateUserRequest(username);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/user/activate")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class BanTests {

        @Test
        @DisplayName("Should ban successfully")
        void shouldBanSuccessfully() throws Exception {
            // Given
            final BanUserRequest request = new BanUserRequest(USERNAME);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/user/ban")
                                    .with(csrf())
                                    .with(authentication(getAdminAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus).send(new BanUserCommand(USERNAME));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when payload is invalid")
        void shouldReturnBadRequestWhenPayloadIsInvalid(final String username) throws Exception {
            // Given
            final BanUserRequest request = new BanUserRequest(username);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/user/ban")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class UnbanTests {

        @Test
        @DisplayName("Should unban successfully")
        void shouldUnbanSuccessfully() throws Exception {
            // Given
            final UnbanUserRequest request = new UnbanUserRequest(USERNAME);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/user/unban")
                                    .with(csrf())
                                    .with(authentication(getAdminAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus).send(new UnbanUserCommand(USERNAME));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when payload is invalid")
        void shouldReturnBadRequestWhenPayloadIsInvalid(final String username) throws Exception {
            // Given
            final UnbanUserRequest request = new UnbanUserRequest(username);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/user/unban")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class DeleteTests {

        @Test
        @DisplayName("Should delete successfully")
        void shouldDeleteSuccessfully() throws Exception {
            // Given
            // When
            // Then
            mockMvc.perform(
                            delete("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .with(authentication(getUserAuth())))
                    .andExpect(status().isNoContent());

            verify(commandBus).send(new DeleteUserCommand(USERNAME));
        }
    }

    @Nested
    final class UpdateUserProfileTests {

        @Test
        @DisplayName("Should update successfully")
        void shouldUpdateSuccessfully() throws Exception {
            // Given
            final UpdateUserProfileRequest request =
                    new UpdateUserProfileRequest(
                            USERNAME,
                            "https://anyAvatar.pl/1",
                            "anyNotificationEmail@mail.com",
                            "123456789",
                            "John",
                            "Doe",
                            UserType.AGENCY);

            // When
            // Then
            mockMvc.perform(
                            put("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new UpdateUserProfileCommand(
                                    USERNAME,
                                    request.avatarUrl(),
                                    request.notificationEmail(),
                                    request.notificationPhoneNumber(),
                                    request.firstName(),
                                    request.lastName(),
                                    request.type()));
        }

        @Test
        @DisplayName("Should update successfully when notificationEmail is null")
        void shouldUpdateSuccessfullyWhenNotificationEmailIsNull() throws Exception {
            // Given
            final UpdateUserProfileRequest request =
                    new UpdateUserProfileRequest(
                            USERNAME,
                            "https://anyAvatar.pl/1",
                            null,
                            "123456789",
                            "John",
                            "Doe",
                            UserType.AGENCY);

            // When
            // Then
            mockMvc.perform(
                            put("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new UpdateUserProfileCommand(
                                    USERNAME,
                                    request.avatarUrl(),
                                    request.notificationEmail(),
                                    request.notificationPhoneNumber(),
                                    request.firstName(),
                                    request.lastName(),
                                    request.type()));
        }

        @Test
        @DisplayName("Should update successfully when notificationPhoneNumber is null")
        void shouldUpdateSuccessfullyWhenNotificationPhoneNumberIsNull() throws Exception {
            // Given
            final UpdateUserProfileRequest request =
                    new UpdateUserProfileRequest(
                            USERNAME,
                            "https://anyAvatar.pl/1",
                            "anyNotificationEmail@mail.com",
                            null,
                            "John",
                            "Doe",
                            UserType.AGENCY);

            // When
            // Then
            mockMvc.perform(
                            put("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new UpdateUserProfileCommand(
                                    USERNAME,
                                    request.avatarUrl(),
                                    request.notificationEmail(),
                                    request.notificationPhoneNumber(),
                                    request.firstName(),
                                    request.lastName(),
                                    request.type()));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when email is invalid")
        void shouldReturnBadRequestWhenEmailIsInvalid(final String username) throws Exception {
            // Given
            final UpdateUserProfileRequest request =
                    new UpdateUserProfileRequest(
                            username,
                            "https://anyAvatar.pl/1",
                            "anyNotificationEmail@mail.com",
                            "123456789",
                            "John",
                            "Doe",
                            UserType.AGENCY);

            // When
            // Then
            mockMvc.perform(
                            put("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
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
            final UpdateUserProfileRequest request =
                    new UpdateUserProfileRequest(
                            USERNAME,
                            "https://anyAvatar.pl/1",
                            "anyNotificationEmail@mail.com",
                            "123456789",
                            firstName,
                            "Doe",
                            UserType.AGENCY);

            // When
            // Then
            mockMvc.perform(
                            put("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
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
            final UpdateUserProfileRequest request =
                    new UpdateUserProfileRequest(
                            USERNAME,
                            "https://anyAvatar.pl/1",
                            "anyNotificationEmail@mail.com",
                            "123456789",
                            "John",
                            lastName,
                            UserType.AGENCY);

            // When
            // Then
            mockMvc.perform(
                            put("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @Test
        @DisplayName("Should return bad request when type is invalid")
        void shouldReturnBadRequestWhenTypeIsInvalid() throws Exception {
            // Given
            final UpdateUserProfileRequest request =
                    new UpdateUserProfileRequest(
                            USERNAME,
                            "https://anyAvatar.pl/1",
                            "anyNotificationEmail@mail.com",
                            "123456789",
                            "John",
                            "Doe",
                            null);

            // When
            // Then
            mockMvc.perform(
                            put("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
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
            final UpdateUserProfileRequest request =
                    new UpdateUserProfileRequest(
                            USERNAME,
                            "https://anyAvatar.pl/1",
                            notificationEmail,
                            "123456789",
                            "John",
                            "Doe",
                            UserType.AGENCY);

            // When
            // Then
            mockMvc.perform(
                            put("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
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
            final UpdateUserProfileRequest request =
                    new UpdateUserProfileRequest(
                            USERNAME,
                            "https://anyAvatar.pl/1",
                            "anyNotificationEmail@mail.com",
                            notificationPhoneNumber,
                            "John",
                            "Doe",
                            UserType.AGENCY);

            // When
            // Then
            mockMvc.perform(
                            put("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class UpdatePasswordTests {

        @Test
        @DisplayName("Should update successfully")
        void shouldUpdateSuccessfully() throws Exception {
            // Given
            final UpdateUserPasswordRequest request =
                    new UpdateUserPasswordRequest(USERNAME, "anyCurrentPassword", "anyNewPassword");

            // When
            // Then
            mockMvc.perform(
                            patch("/api/user/password")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new UpdateUserPasswordCommand(
                                    USERNAME, request.currentPassword(), request.newPassword()));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when username is invalid")
        void shouldReturnBadRequestWhenUsernameIsInvalid(final String username) throws Exception {
            // Given
            final UpdateUserPasswordRequest request =
                    new UpdateUserPasswordRequest(username, "anyCurrentPassword", "anyNewPassword");

            // When
            // Then
            mockMvc.perform(
                            patch("/api/user/password")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when currentPassword is invalid")
        void shouldReturnBadRequestWhenCurrentPasswordIsInvalid(final String currentPassword)
                throws Exception {

            // Given
            final UpdateUserPasswordRequest request =
                    new UpdateUserPasswordRequest(USERNAME, currentPassword, "anyNewPassword");

            // When
            // Then
            mockMvc.perform(
                            patch("/api/user/password")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when newPassword is invalid")
        void shouldReturnBadRequestWhenNewPasswordIsInvalid(final String newPassword)
                throws Exception {

            // Given
            final UpdateUserPasswordRequest request =
                    new UpdateUserPasswordRequest(USERNAME, "anyCurrentPassword", newPassword);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/user/password")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class ProfileTests {

        @Test
        @DisplayName("Should return profile successfully")
        void shouldReturnProfileSuccessfully() throws Exception {
            // Given
            // When
            // Then
            mockMvc.perform(
                            get("/api/user/profile")
                                    .with(csrf())
                                    .with(authentication(getUserAuth())))
                    .andExpect(status().isOk());

            verify(queryBus).send(new UserProfileQuery(USERNAME));
        }
    }

    private static UsernamePasswordAuthenticationToken getUserAuth() {
        final AuthenticatedUser customUser = new AuthenticatedUser(USERNAME, UserRole.ROLE_USER);
        return new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
    }

    private static UsernamePasswordAuthenticationToken getAdminAuth() {
        final AuthenticatedUser customUser = new AuthenticatedUser(USERNAME, UserRole.ROLE_ADMIN);
        return new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
    }
}
