/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import pl.dawid0604.realestate.api.advertisement.request.ActivateAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.DeactivateAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.PromoteAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.SetAsSoldAdvertisementRequest;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.ActivateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.DeactivateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.SetAsFeaturedAdvertisementCommand;
import pl.dawid0604.realestate.application.command.SetAsSoldAdvertisementCommand;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;

import java.util.stream.Stream;

@EnableMethodSecurity
@WebMvcTest(AdvertisementController.class)
class AdvertisementControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private TokenRepository tokenRepository;
    @MockitoBean private CommandBus commandBus;
    @MockitoBean private QueryBus queryBus;

    private static final String USERNAME = "test_username@mail.com";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Nested
    final class ActivateTests {

        @ParameterizedTest
        @EnumSource(AdvertisementType.class)
        @DisplayName("Should activate successfully")
        void shouldActivateSuccessfully(final AdvertisementType advertisementType)
                throws Exception {

            // Given
            final ActivateAdvertisementRequest request =
                    new ActivateAdvertisementRequest(getSlug(), advertisementType);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/activate")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new ActivateAdvertisementCommand(
                                    request.slug(), request.type(), USERNAME));
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.AdvertisementControllerTest#boundarySlugDataProvider")
        @DisplayName("Should activate successfully with boundary slug")
        void shouldActivateSuccessfullyWithBoundarySlug(final String slug) throws Exception {
            // Given
            final ActivateAdvertisementRequest request =
                    new ActivateAdvertisementRequest(slug, AdvertisementType.FLAT);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/activate")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new ActivateAdvertisementCommand(
                                    request.slug(), request.type(), USERNAME));
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.AdvertisementControllerTest#invalidSlugDataProvider")
        @DisplayName("Should return bad request when slug is invalid")
        void shouldReturnBadRequestWhenSlugIsInvalid(final String slug) throws Exception {
            // Given
            final ActivateAdvertisementRequest request =
                    new ActivateAdvertisementRequest(slug, AdvertisementType.FLAT);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/activate")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class DeactivateTests {

        @ParameterizedTest
        @EnumSource(AdvertisementType.class)
        @DisplayName("Should deactivate successfully")
        void shouldDeactivateSuccessfully(final AdvertisementType advertisementType)
                throws Exception {

            // Given
            final DeactivateAdvertisementRequest request =
                    new DeactivateAdvertisementRequest(getSlug(), advertisementType);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/deactivate")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new DeactivateAdvertisementCommand(
                                    request.slug(), request.type(), USERNAME));
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.AdvertisementControllerTest#boundarySlugDataProvider")
        @DisplayName("Should activate successfully with boundary slug")
        void shouldDeactivateSuccessfullyWithBoundarySlug(final String slug) throws Exception {
            // Given
            final DeactivateAdvertisementRequest request =
                    new DeactivateAdvertisementRequest(slug, AdvertisementType.FLAT);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/deactivate")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new DeactivateAdvertisementCommand(
                                    request.slug(), request.type(), USERNAME));
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.AdvertisementControllerTest#invalidSlugDataProvider")
        @DisplayName("Should return bad request when slug is invalid")
        void shouldReturnBadRequestWhenSlugIsInvalid(final String slug) throws Exception {
            // Given
            final DeactivateAdvertisementRequest request =
                    new DeactivateAdvertisementRequest(slug, AdvertisementType.FLAT);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/deactivate")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class PromoteTests {

        @ParameterizedTest
        @EnumSource(AdvertisementType.class)
        @DisplayName("Should promote successfully")
        void shouldPromoteSuccessfully(final AdvertisementType advertisementType) throws Exception {

            // Given
            final PromoteAdvertisementRequest request =
                    new PromoteAdvertisementRequest(getSlug(), advertisementType);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/promote")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new SetAsFeaturedAdvertisementCommand(
                                    request.slug(), request.type(), USERNAME));
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.AdvertisementControllerTest#boundarySlugDataProvider")
        @DisplayName("Should promote successfully with boundary slug")
        void shouldPromoteSuccessfullyWithBoundarySlug(final String slug) throws Exception {
            // Given
            final PromoteAdvertisementRequest request =
                    new PromoteAdvertisementRequest(slug, AdvertisementType.FLAT);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/promote")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new SetAsFeaturedAdvertisementCommand(
                                    request.slug(), request.type(), USERNAME));
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.AdvertisementControllerTest#invalidSlugDataProvider")
        @DisplayName("Should return bad request when slug is invalid")
        void shouldReturnBadRequestWhenSlugIsInvalid(final String slug) throws Exception {
            // Given
            final PromoteAdvertisementRequest request =
                    new PromoteAdvertisementRequest(slug, AdvertisementType.FLAT);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/promote")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class SetAsSoldTests {

        @ParameterizedTest
        @EnumSource(AdvertisementType.class)
        @DisplayName("Should set as sold successfully")
        void shouldSetAsSoldSuccessfully(final AdvertisementType advertisementType)
                throws Exception {

            // Given
            final SetAsSoldAdvertisementRequest request =
                    new SetAsSoldAdvertisementRequest(getSlug(), advertisementType);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/sold")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new SetAsSoldAdvertisementCommand(
                                    request.slug(), request.type(), USERNAME));
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.AdvertisementControllerTest#boundarySlugDataProvider")
        @DisplayName("Should set as sold successfully with boundary slug")
        void shouldSetAsSoldSuccessfullyWithBoundarySlug(final String slug) throws Exception {
            // Given
            final SetAsSoldAdvertisementRequest request =
                    new SetAsSoldAdvertisementRequest(slug, AdvertisementType.FLAT);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/sold")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new SetAsSoldAdvertisementCommand(
                                    request.slug(), request.type(), USERNAME));
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.AdvertisementControllerTest#invalidSlugDataProvider")
        @DisplayName("Should return bad request when slug is invalid")
        void shouldReturnBadRequestWhenSlugIsInvalid(final String slug) throws Exception {
            // Given
            final SetAsSoldAdvertisementRequest request =
                    new SetAsSoldAdvertisementRequest(slug, AdvertisementType.FLAT);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/sold")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    private static Stream<Arguments> invalidSlugDataProvider() {
        final String slugMin = RandomStringUtils.secure().nextAlphanumeric(9);
        final String slugMax = RandomStringUtils.secure().nextAlphanumeric(101);

        return Stream.of(
                Arguments.of(""),
                Arguments.of((String) null),
                Arguments.of(" "),
                Arguments.of(slugMin),
                Arguments.of(slugMax));
    }

    private static Stream<Arguments> boundarySlugDataProvider() {
        final String slugMin = RandomStringUtils.secure().nextAlphanumeric(10);
        final String slugMax = RandomStringUtils.secure().nextAlphanumeric(100);
        return Stream.of(Arguments.of(slugMin), Arguments.of(slugMax));
    }

    private static String getSlug() {
        return "any-slug-123";
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
