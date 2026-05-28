/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import pl.dawid0604.realestate.api.advertisement.request.AdvertisementPhotoRequest;
import pl.dawid0604.realestate.api.advertisement.request.CreatePlotAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.UpdatePlotAdvertisementRequest;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.CreatePlotAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdatePlotAdvertisementCommand;
import pl.dawid0604.realestate.domain.PlotBuildingType;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@EnableMethodSecurity
@WebMvcTest(PlotAdvertisementController.class)
class PlotAdvertisementControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private TokenRepository tokenRepository;
    @MockitoBean private CommandBus commandBus;
    @MockitoBean private QueryBus queryBus;

    private static final String USERNAME = "test_username@mail.com";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Nested
    final class CreateTests {

        @Test
        @DisplayName("Should create successfully")
        void shouldCreateSuccessfully() throws Exception {
            // Given
            final String slug = getSlug();
            final CreatePlotAdvertisementRequest request =
                    new CreatePlotAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            given(
                            commandBus.send(
                                    new CreatePlotAdvertisementCommand(
                                            request.getTitle(),
                                            request.getDescription(),
                                            request.getPrice(),
                                            request.getLocalityId(),
                                            USERNAME,
                                            Mapper.mapPhotos(request.getPhotos()),
                                            request.getBuildingType().name(),
                                            request.getArea(),
                                            request.getClaims(),
                                            request.getFeatured())))
                    .willReturn(slug);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(
                            header().string(
                                            "Location",
                                            "http://localhost/api/advertisement/plot/" + slug));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when title is invalid")
        void shouldReturnBadRequestWhenTitleIsInvalid(final String title) throws Exception {
            // Given
            final CreatePlotAdvertisementRequest request =
                    new CreatePlotAdvertisementRequest(
                            title,
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when description is invalid")
        void shouldReturnBadRequestWhenDescriptionIsInvalid(final String description)
                throws Exception {

            // Given
            final CreatePlotAdvertisementRequest request =
                    new CreatePlotAdvertisementRequest(
                            getTitle(),
                            description,
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @Test
        @DisplayName("Should return bad request when locality is invalid")
        void shouldReturnBadRequestWhenLocalityIsInvalid() throws Exception {
            // Given
            final CreatePlotAdvertisementRequest request =
                    new CreatePlotAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            null,
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @CsvSource({"test.com", "test@", "@test.com", "test @test.com", "test@test..com"})
        @DisplayName("Should return bad request when userEmail is invalid")
        void shouldReturnBadRequestWhenUserEmailIsInvalid(final String userEmail) throws Exception {
            // Given
            final CreatePlotAdvertisementRequest request =
                    new CreatePlotAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            userEmail,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.PlotAdvertisementControllerTest#invalidBigDecimalDataProvider")
        @DisplayName("Should return bad request when price is invalid")
        void shouldReturnBadRequestWhenPriceIsInvalid(final BigDecimal price) throws Exception {
            // Given
            final CreatePlotAdvertisementRequest request =
                    new CreatePlotAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            price,
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.PlotAdvertisementControllerTest#invalidBigDecimalDataProvider")
        @DisplayName("Should return bad request when area is invalid")
        void shouldReturnBadRequestWhenAreaIsInvalid(final BigDecimal area) throws Exception {
            // Given
            final CreatePlotAdvertisementRequest request =
                    new CreatePlotAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            area,
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @Test
        @DisplayName("Should return bad request when buildingType is invalid")
        void shouldReturnBadRequestWhenBuildingTypeIsInvalid() throws Exception {
            // Given
            final CreatePlotAdvertisementRequest request =
                    new CreatePlotAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            null);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    @Nested
    final class UpdateTests {

        @Test
        @DisplayName("Should update successfully")
        void shouldUpdateSuccessfully() throws Exception {
            // Given
            final String slug = getSlug();
            final UpdatePlotAdvertisementRequest request =
                    new UpdatePlotAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            given(
                            commandBus.send(
                                    new UpdatePlotAdvertisementCommand(
                                            request.getSlug(),
                                            request.getTitle(),
                                            request.getDescription(),
                                            request.getPrice(),
                                            request.getLocalityId(),
                                            USERNAME,
                                            request.getPlotType().name(),
                                            request.getArea(),
                                            request.getClaims(),
                                            request.getFeatured())))
                    .willReturn(slug);

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isNoContent());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when slug is invalid")
        void shouldReturnBadRequestWhenSlugIsInvalid(final String slug) throws Exception {
            // Given
            final UpdatePlotAdvertisementRequest request =
                    new UpdatePlotAdvertisementRequest(
                            slug,
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when title is invalid")
        void shouldReturnBadRequestWhenTitleIsInvalid(final String title) throws Exception {
            // Given
            final UpdatePlotAdvertisementRequest request =
                    new UpdatePlotAdvertisementRequest(
                            getSlug(),
                            title,
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when description is invalid")
        void shouldReturnBadRequestWhenDescriptionIsInvalid(final String description)
                throws Exception {

            // Given
            final UpdatePlotAdvertisementRequest request =
                    new UpdatePlotAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            description,
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @Test
        @DisplayName("Should return bad request when locality is invalid")
        void shouldReturnBadRequestWhenLocalityIsInvalid() throws Exception {
            // Given
            final UpdatePlotAdvertisementRequest request =
                    new UpdatePlotAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            null,
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @CsvSource({"test.com", "test@", "@test.com", "test @test.com", "test@test..com"})
        @DisplayName("Should return bad request when userEmail is invalid")
        void shouldReturnBadRequestWhenUserEmailIsInvalid(final String userEmail) throws Exception {
            // Given
            final UpdatePlotAdvertisementRequest request =
                    new UpdatePlotAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            userEmail,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.PlotAdvertisementControllerTest#invalidBigDecimalDataProvider")
        @DisplayName("Should return bad request when price is invalid")
        void shouldReturnBadRequestWhenPriceIsInvalid(final BigDecimal price) throws Exception {
            // Given
            final UpdatePlotAdvertisementRequest request =
                    new UpdatePlotAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            price,
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.PlotAdvertisementControllerTest#invalidBigDecimalDataProvider")
        @DisplayName("Should return bad request when area is invalid")
        void shouldReturnBadRequestWhenAreaIsInvalid(final BigDecimal area) throws Exception {
            // Given
            final UpdatePlotAdvertisementRequest request =
                    new UpdatePlotAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            area,
                            getClaims(),
                            getFeatured(),
                            getPlotBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @Test
        @DisplayName("Should return bad request when buildingType is invalid")
        void shouldReturnBadRequestWhenBuildingTypeIsInvalid() throws Exception {
            // Given
            final UpdatePlotAdvertisementRequest request =
                    new UpdatePlotAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            null);

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/plot")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }
    }

    private static Stream<Arguments> invalidBigDecimalDataProvider() {
        return Stream.of(
                Arguments.of((BigDecimal) null),
                Arguments.of(BigDecimal.valueOf(0)),
                Arguments.of(new BigDecimal("0.00")),
                Arguments.of(new BigDecimal("0.001")));
    }

    private static String getTitle() {
        return "any-valid-title";
    }

    private static String getSlug() {
        return "any-valid-slug";
    }

    private static String getDescription() {
        return "any-valid-description";
    }

    private static BigDecimal getPrice() {
        return BigDecimal.valueOf(250_000);
    }

    private static BigDecimal getArea() {
        return BigDecimal.valueOf(25.35);
    }

    private static UUID getUUID() {
        return UUID.randomUUID();
    }

    private static PlotBuildingType getPlotBuildingType() {
        return PlotBuildingType.AGRICULTURAL;
    }

    private static boolean getFeatured() {
        return true;
    }

    private static Set<AdvertisementPhotoRequest> getPhotos() {
        return Set.of(
                new AdvertisementPhotoRequest("https://anyImageUrl.com/1", 0),
                new AdvertisementPhotoRequest("https://anyImageUrl.com/2", 1));
    }

    private static Map<String, String> getClaims() {
        return Map.of("abc", "cde", "efg", "hjk");
    }

    private static UsernamePasswordAuthenticationToken getUserAuth() {
        final AuthenticatedUser customUser = new AuthenticatedUser(USERNAME, UserRole.ROLE_USER);
        return new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
    }
}
