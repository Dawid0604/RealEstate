/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.user;

import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.api.user.request.ActivateUserRequest;
import pl.dawid0604.realestate.api.user.request.BanUserRequest;
import pl.dawid0604.realestate.api.user.request.DeleteUserRequest;
import pl.dawid0604.realestate.api.user.request.UnbanUserRequest;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.ActivateUserCommand;
import pl.dawid0604.realestate.application.command.BanUserCommand;
import pl.dawid0604.realestate.application.command.DeleteUserCommand;
import pl.dawid0604.realestate.application.command.UnbanUserCommand;
import pl.dawid0604.realestate.application.command.UserLogoutCommand;
import pl.dawid0604.realestate.domain.UserRole;
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
    }

    @Nested
    final class DeleteTests {

        @Test
        @DisplayName("Should delete successfully")
        void shouldDeleteSuccessfully() throws Exception {
            // Given
            final DeleteUserRequest request = new DeleteUserRequest(USERNAME);

            // When
            // Then
            mockMvc.perform(
                            delete("/api/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus).send(new DeleteUserCommand(USERNAME));
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
