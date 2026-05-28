/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import pl.dawid0604.realestate.api.advertisement.request.ActivateAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.AddAdvertisementPhotoRequest;
import pl.dawid0604.realestate.api.advertisement.request.DeactivateAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.DeleteAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.PromoteAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.SearchUserAdvertisementsRequest;
import pl.dawid0604.realestate.api.advertisement.request.SetAsSoldAdvertisementRequest;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.ActivateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.AddAdvertisementPhotoCommand;
import pl.dawid0604.realestate.application.command.DeactivateAdvertisementCommand;
import pl.dawid0604.realestate.application.command.DeleteAdvertisementCommand;
import pl.dawid0604.realestate.application.command.SetAsFeaturedAdvertisementCommand;
import pl.dawid0604.realestate.application.command.SetAsSoldAdvertisementCommand;
import pl.dawid0604.realestate.application.query.UserAdvertisementsQuery;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;

import java.util.Arrays;
import java.util.Set;
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

        @Test
        @DisplayName("Should return bad request when advertisementType is invalid")
        void shouldReturnBadRequestWhenAdvertisementTypeIsInvalid() throws Exception {
            // Given
            final ActivateAdvertisementRequest request =
                    new ActivateAdvertisementRequest(getSlug(), null);

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

        @Test
        @DisplayName("Should return bad request when advertisementType is invalid")
        void shouldReturnBadRequestWhenAdvertisementTypeIsInvalid() throws Exception {
            // Given
            final DeactivateAdvertisementRequest request =
                    new DeactivateAdvertisementRequest(getSlug(), null);

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

        @Test
        @DisplayName("Should return bad request when advertisementType is invalid")
        void shouldReturnBadRequestWhenAdvertisementTypeIsInvalid() throws Exception {
            // Given
            final PromoteAdvertisementRequest request =
                    new PromoteAdvertisementRequest(getSlug(), null);

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

        @Test
        @DisplayName("Should return bad request when advertisementType is invalid")
        void shouldReturnBadRequestWhenAdvertisementTypeIsInvalid() throws Exception {
            // Given
            final SetAsSoldAdvertisementRequest request =
                    new SetAsSoldAdvertisementRequest(getSlug(), null);

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

    @Nested
    final class DeleteTests {

        @ParameterizedTest
        @EnumSource(AdvertisementType.class)
        @DisplayName("Should delete successfully")
        void shouldDeleteSuccessfully(final AdvertisementType advertisementType) throws Exception {
            // Given
            final DeleteAdvertisementRequest request =
                    new DeleteAdvertisementRequest(getSlug(), advertisementType);

            // When
            // Then
            mockMvc.perform(
                            delete("/api/advertisement")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(new DeleteAdvertisementCommand(request.slug(), request.type(), USERNAME));
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.AdvertisementControllerTest#boundarySlugDataProvider")
        @DisplayName("Should delete successfully with boundary slug")
        void shouldDeleteSuccessfullyWithBoundarySlug(final String slug) throws Exception {
            // Given
            final DeleteAdvertisementRequest request =
                    new DeleteAdvertisementRequest(slug, AdvertisementType.FLAT);

            // When
            // Then
            mockMvc.perform(
                            delete("/api/advertisement")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(new DeleteAdvertisementCommand(request.slug(), request.type(), USERNAME));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when slug is invalid")
        void shouldReturnBadRequestWhenSlugIsInvalid(final String slug) throws Exception {
            // Given
            final DeleteAdvertisementRequest request =
                    new DeleteAdvertisementRequest(slug, AdvertisementType.FLAT);

            // When
            // Then
            mockMvc.perform(
                            delete("/api/advertisement")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @Test
        @DisplayName("Should return bad request when advertisementType is invalid")
        void shouldReturnBadRequestWhenAdvertisementTypeIsInvalid() throws Exception {
            // Given
            final DeleteAdvertisementRequest request =
                    new DeleteAdvertisementRequest(getSlug(), null);

            // When
            // Then
            mockMvc.perform(
                            delete("/api/advertisement")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class AddPhotoTests {

        @ParameterizedTest
        @EnumSource(AdvertisementType.class)
        @DisplayName("Should add photo successfully")
        void shouldAddPhotoSuccessfully(final AdvertisementType advertisementType)
                throws Exception {

            // Given
            final AddAdvertisementPhotoRequest request =
                    new AddAdvertisementPhotoRequest(getSlug(), advertisementType, getUrl(), 0);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/photo")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new AddAdvertisementPhotoCommand(
                                    request.slug(),
                                    request.type(),
                                    request.photoUrl(),
                                    request.position(),
                                    USERNAME));
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.AdvertisementControllerTest#boundarySlugDataProvider")
        @DisplayName("Should add photo with boundary slug successfully")
        void shouldAddPhotoWithBoundarySlugSuccessfully(final String slug) throws Exception {

            // Given
            final AddAdvertisementPhotoRequest request =
                    new AddAdvertisementPhotoRequest(slug, AdvertisementType.FLAT, getUrl(), 0);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/photo")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new AddAdvertisementPhotoCommand(
                                    request.slug(),
                                    request.type(),
                                    request.photoUrl(),
                                    request.position(),
                                    USERNAME));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4, 5, 15, 19, 20})
        @DisplayName("Should add photo with boundary position successfully")
        void shouldAddPhotoWithBoundaryPositionSuccessfully(final int position) throws Exception {
            // Given
            final AddAdvertisementPhotoRequest request =
                    new AddAdvertisementPhotoRequest(
                            getSlug(), AdvertisementType.FLAT, getUrl(), position);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/photo")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(commandBus)
                    .send(
                            new AddAdvertisementPhotoCommand(
                                    request.slug(),
                                    request.type(),
                                    request.photoUrl(),
                                    request.position(),
                                    USERNAME));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when slug is invalid")
        void shouldReturnBadRequestWhenSlugIsInvalid(final String slug) throws Exception {
            // Given
            final AddAdvertisementPhotoRequest request =
                    new AddAdvertisementPhotoRequest(slug, AdvertisementType.FLAT, getUrl(), 0);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/photo")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @Test
        @DisplayName("Should return bad request when advertisementType is invalid")
        void shouldReturnBadRequestWhenAdvertisementTypeIsInvalid() throws Exception {
            // Given
            final AddAdvertisementPhotoRequest request =
                    new AddAdvertisementPhotoRequest(getSlug(), null, getUrl(), 0);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/photo")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(
                strings = {
                    "   ",
                    "http://",
                    "https://",
                    "www.google.com",
                    "github.com/user",
                    "http://localhost:abc",
                    "https://localhost:abc",
                    "http://google.com/ ",
                    "https://google.com/ "
                })
        @DisplayName("Should return bad request when url is invalid")
        void shouldReturnBadRequestWhenUrlIsInvalid(final String url) throws Exception {
            // Given
            final AddAdvertisementPhotoRequest request =
                    new AddAdvertisementPhotoRequest(getSlug(), AdvertisementType.FLAT, url, 0);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/photo")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(ints = {-1, -100, -2500, 25, 2500})
        @DisplayName("Should return bad request when position is invalid")
        void shouldReturnBadRequestWhenPositionIsInvalid(final Integer position) throws Exception {
            // Given
            final AddAdvertisementPhotoRequest request =
                    new AddAdvertisementPhotoRequest(
                            getSlug(), AdvertisementType.FLAT, getUrl(), position);

            // When
            // Then
            mockMvc.perform(
                            patch("/api/advertisement/photo")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        private static String getUrl() {
            return "https://anyPhoto.com/1";
        }
    }

    @Nested
    final class SearchByUserTests {

        @Test
        @DisplayName("Should find successfully")
        void shouldFindSuccessfully() throws Exception {
            // Given
            final Set<AdvertisementStatus> statuses = Set.of(AdvertisementStatus.DELETED);
            final int pageNumber = 1;
            final int pageSize = 25;
            final SearchUserAdvertisementsRequest request =
                    new SearchUserAdvertisementsRequest(statuses);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .param("page", String.valueOf(pageNumber))
                                    .param("size", String.valueOf(pageSize))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(queryBus)
                    .send(
                            new UserAdvertisementsQuery(
                                    USERNAME, pageNumber, pageSize, request.statuses()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 44, 55, 125})
        @DisplayName("Should find with boundary page successfully")
        void shouldFindWithBoundaryPageSuccessfully(final int pageNumber) throws Exception {
            // Given
            final Set<AdvertisementStatus> statuses = Set.of(AdvertisementStatus.DELETED);
            final int pageSize = 25;
            final SearchUserAdvertisementsRequest request =
                    new SearchUserAdvertisementsRequest(statuses);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .param("page", String.valueOf(pageNumber))
                                    .param("size", String.valueOf(pageSize))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(queryBus)
                    .send(
                            new UserAdvertisementsQuery(
                                    USERNAME, pageNumber, pageSize, request.statuses()));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 44, 55, 99, 100})
        @DisplayName("Should find with boundary pageSize successfully")
        void shouldFindWithBoundaryPageSizeSuccessfully(final int pageSize) throws Exception {
            // Given
            final Set<AdvertisementStatus> statuses = Set.of(AdvertisementStatus.DELETED);
            final int pageNumber = 1;
            final SearchUserAdvertisementsRequest request =
                    new SearchUserAdvertisementsRequest(statuses);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .param("page", String.valueOf(pageNumber))
                                    .param("size", String.valueOf(pageSize))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(queryBus)
                    .send(
                            new UserAdvertisementsQuery(
                                    USERNAME, pageNumber, pageSize, request.statuses()));
        }

        @Test
        @DisplayName("Should find without query params successfully")
        void shouldFindWithoutQueryParamsSuccessfully() throws Exception {
            // Given
            final Set<AdvertisementStatus> statuses = Set.of(AdvertisementStatus.DELETED);
            final SearchUserAdvertisementsRequest request =
                    new SearchUserAdvertisementsRequest(statuses);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(queryBus).send(new UserAdvertisementsQuery(USERNAME, 0, 25, request.statuses()));
        }

        @ParameterizedTest
        @MethodSource("emptyStatusesDataProvider")
        @DisplayName("Should find with empty statuses successfully")
        void shouldFindWithEmptyStatusesSuccessfully(final Set<AdvertisementStatus> statuses)
                throws Exception {

            // Given
            final int pageNumber = 1;
            final int pageSize = 25;
            final SearchUserAdvertisementsRequest request =
                    new SearchUserAdvertisementsRequest(statuses);

            final Set<AdvertisementStatus> allStatuses =
                    Arrays.stream(AdvertisementStatus.values()).collect(toSet());

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .param("page", String.valueOf(pageNumber))
                                    .param("size", String.valueOf(pageSize))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(queryBus)
                    .send(new UserAdvertisementsQuery(USERNAME, pageNumber, pageSize, allStatuses));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -100, -2500})
        @DisplayName("Should return bad request when page is invalid")
        void shouldReturnBadRequestWhenPageIsInvalid(final int page) throws Exception {
            // Given
            final Set<AdvertisementStatus> statuses = Set.of(AdvertisementStatus.DELETED);
            final SearchUserAdvertisementsRequest request =
                    new SearchUserAdvertisementsRequest(statuses);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .param("page", String.valueOf(page))
                                    .param("size", "25")
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(queryBus);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -100, -2500, 101, 250, 2500})
        @DisplayName("Should return bad request when pageSize is invalid")
        void shouldReturnBadRequestWhenPageSizeIsInvalid(final int pageSize) throws Exception {
            // Given
            final Set<AdvertisementStatus> statuses = Set.of(AdvertisementStatus.DELETED);
            final SearchUserAdvertisementsRequest request =
                    new SearchUserAdvertisementsRequest(statuses);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/user")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .param("page", "1")
                                    .param("size", String.valueOf(pageSize))
                                    .contentType(APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(queryBus);
        }

        private static Stream<Arguments> emptyStatusesDataProvider() {
            return Stream.of(
                    Arguments.of((Set<AdvertisementStatus>) null), Arguments.of(emptySet()));
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
}
