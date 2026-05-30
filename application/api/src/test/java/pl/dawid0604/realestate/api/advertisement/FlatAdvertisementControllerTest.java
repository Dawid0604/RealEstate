/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement;

import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import pl.dawid0604.realestate.api.advertisement.request.AdvertisementPhotoRequest;
import pl.dawid0604.realestate.api.advertisement.request.CreateFlatAdvertisementRequest;
import pl.dawid0604.realestate.api.advertisement.request.SearchFlatAdvertisementsRequest;
import pl.dawid0604.realestate.api.advertisement.request.UpdateFlatAdvertisementRequest;
import pl.dawid0604.realestate.api.config.security.AuthenticatedUser;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.CreateFlatAdvertisementCommand;
import pl.dawid0604.realestate.application.command.UpdateFlatAdvertisementCommand;
import pl.dawid0604.realestate.application.query.FlatAdvertisementDetailsQuery;
import pl.dawid0604.realestate.application.query.Query;
import pl.dawid0604.realestate.application.query.SearchFlatAdvertisementsQuery;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchFlatAdvertisementsCriteria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@EnableMethodSecurity
@ExtendWith(MockitoExtension.class)
@WebMvcTest(FlatAdvertisementController.class)
class FlatAdvertisementControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private TokenRepository tokenRepository;
    @MockitoBean private CommandBus commandBus;
    @MockitoBean private QueryBus queryBus;
    @Captor private ArgumentCaptor<Query> argumentCaptor;

    private static final String USERNAME = "test_username@mail.com";
    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper().registerModule(new JavaTimeModule());

    @Nested
    final class CreateTests {

        @Test
        @DisplayName("Should create successfully")
        void shouldCreateSuccessfully() throws Exception {
            // Given
            final String slug = getSlug();
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            given(
                            commandBus.send(
                                    new CreateFlatAdvertisementCommand(
                                            request.getTitle(),
                                            request.getDescription(),
                                            request.getPrice(),
                                            request.getLocalityId(),
                                            USERNAME,
                                            request.getNumberOfRooms(),
                                            request.getFloor(),
                                            request.getFloors(),
                                            request.getBuiltYear(),
                                            request.getTypeOfMarket().name(),
                                            Mapper.mapPhotos(request.getPhotos()),
                                            request.getBuildingType().name(),
                                            request.getArea(),
                                            request.getClaims(),
                                            request.getFeatured())))
                    .willReturn(slug);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(
                            header().string(
                                            "Location",
                                            "http://localhost/api/advertisement/flat/" + slug));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request when title is invalid")
        void shouldReturnBadRequestWhenTitleIsInvalid(final String title) throws Exception {
            // Given
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            title,
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
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
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            description,
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
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
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            null,
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
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
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            userEmail,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.FlatAdvertisementControllerTest#invalidBigDecimalDataProvider")
        @DisplayName("Should return bad request when price is invalid")
        void shouldReturnBadRequestWhenPriceIsInvalid(final BigDecimal price) throws Exception {
            // Given
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            price,
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.FlatAdvertisementControllerTest#invalidBigDecimalDataProvider")
        @DisplayName("Should return bad request when area is invalid")
        void shouldReturnBadRequestWhenAreaIsInvalid(final BigDecimal area) throws Exception {
            // Given
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            area,
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1, 0})
        @DisplayName("Should return bad request when numberOfRooms is invalid")
        void shouldReturnBadRequestWhenNumberOfRoomsIsInvalid(final int numberOfRooms)
                throws Exception {

            // Given
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            numberOfRooms,
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        @DisplayName("Should return bad request when floor is invalid")
        void shouldReturnBadRequestWhenFloorIsInvalid(final int floor) throws Exception {
            // Given
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            floor,
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        @DisplayName("Should return bad request when floors is invalid")
        void shouldReturnBadRequestWhenFloorsIsInvalid(final int floors) throws Exception {
            // Given
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            floors,
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @MethodSource("invalidBuiltYearDataProvider")
        @DisplayName("Should return bad request when builtYear is invalid")
        void shouldReturnBadRequestWhenBuiltYearIsInvalid(final int builtYear) throws Exception {
            // Given
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            builtYear,
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @Test
        @DisplayName("Should return bad request when typeOfMarket is invalid")
        void shouldReturnBadRequestWhenTypeOfMarketIsInvalid() throws Exception {
            // Given
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            null,
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
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
            final CreateFlatAdvertisementRequest request =
                    new CreateFlatAdvertisementRequest(
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getPhotos(),
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            null);

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        private static Stream<Arguments> invalidBuiltYearDataProvider() {
            return Stream.of(
                    Arguments.of(1),
                    Arguments.of(1200),
                    Arguments.of(1799),
                    Arguments.of(2204),
                    Arguments.of(LocalDate.now().getYear() + 3));
        }
    }

    @Nested
    final class UpdateTests {

        @Test
        @DisplayName("Should update successfully")
        void shouldUpdateSuccessfully() throws Exception {
            // Given
            final String slug = getSlug();
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            given(
                            commandBus.send(
                                    new UpdateFlatAdvertisementCommand(
                                            request.getSlug(),
                                            request.getTitle(),
                                            request.getDescription(),
                                            request.getPrice(),
                                            request.getLocalityId(),
                                            USERNAME,
                                            request.getNumberOfRooms(),
                                            request.getFloor(),
                                            request.getFloors(),
                                            request.getBuiltYear(),
                                            request.getTypeOfMarket().name(),
                                            request.getBuildingType().name(),
                                            request.getArea(),
                                            request.getClaims(),
                                            request.getFeatured())))
                    .willReturn(slug);

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
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
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            slug,
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
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
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            title,
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
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
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            description,
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
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
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            null,
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
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
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            userEmail,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.FlatAdvertisementControllerTest#invalidBigDecimalDataProvider")
        @DisplayName("Should return bad request when price is invalid")
        void shouldReturnBadRequestWhenPriceIsInvalid(final BigDecimal price) throws Exception {
            // Given
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            price,
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @MethodSource(
                "pl.dawid0604.realestate.api.advertisement.FlatAdvertisementControllerTest#invalidBigDecimalDataProvider")
        @DisplayName("Should return bad request when area is invalid")
        void shouldReturnBadRequestWhenAreaIsInvalid(final BigDecimal area) throws Exception {
            // Given
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            area,
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1, 0})
        @DisplayName("Should return bad request when numberOfRooms is invalid")
        void shouldReturnBadRequestWhenNumberOfRoomsIsInvalid(final int numberOfRooms)
                throws Exception {

            // Given
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            numberOfRooms,
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        @DisplayName("Should return bad request when floor is invalid")
        void shouldReturnBadRequestWhenFloorIsInvalid(final int floor) throws Exception {
            // Given
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            floor,
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        @DisplayName("Should return bad request when floors is invalid")
        void shouldReturnBadRequestWhenFloorsIsInvalid(final int floors) throws Exception {
            // Given
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            floors,
                            getBuiltYear(),
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @ParameterizedTest
        @MethodSource("invalidBuiltYearDataProvider")
        @DisplayName("Should return bad request when builtYear is invalid")
        void shouldReturnBadRequestWhenBuiltYearIsInvalid(final int builtYear) throws Exception {
            // Given
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            builtYear,
                            getTypeOfMarket(),
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        @Test
        @DisplayName("Should return bad request when typeOfMarket is invalid")
        void shouldReturnBadRequestWhenTypeOfMarketIsInvalid() throws Exception {
            // Given
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            null,
                            getFlatBuildingType());

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
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
            final UpdateFlatAdvertisementRequest request =
                    new UpdateFlatAdvertisementRequest(
                            getSlug(),
                            getTitle(),
                            getDescription(),
                            getPrice(),
                            getUUID(),
                            USERNAME,
                            getArea(),
                            getClaims(),
                            getFeatured(),
                            getNumberOfRooms(),
                            getFloor(),
                            getFloors(),
                            getBuiltYear(),
                            getTypeOfMarket(),
                            null);

            // When
            // Then
            mockMvc.perform(
                            put("/api/advertisement/flat")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(commandBus);
        }

        private static Stream<Arguments> invalidBuiltYearDataProvider() {
            return Stream.of(
                    Arguments.of(1),
                    Arguments.of(1200),
                    Arguments.of(1799),
                    Arguments.of(2204),
                    Arguments.of(LocalDate.now().getYear() + 3));
        }
    }

    @Nested
    final class SearchByCriteriaTests {

        @Test
        @DisplayName("Should search successfully")
        void shouldSearchSuccessfully() throws Exception {
            // Given
            final int page = 1;
            final int pageSize = 25;

            final SearchFlatAdvertisementsRequest request =
                    new SearchFlatAdvertisementsRequest(
                            getAreaFrom(),
                            getAreaTo(),
                            getPriceFrom(),
                            getPriceTo(),
                            getPricePerSquareMeterFrom(),
                            getPricePerSquareMeterTo(),
                            getUUID(),
                            getDateFrom(),
                            getDateTo(),
                            getFlatTypes(),
                            getTypeOfMarkets(),
                            getFloorFrom(),
                            getFloorTo(),
                            getFloorsFrom(),
                            getFloorsTo(),
                            getNumberOfRoomsFrom(),
                            getNumberOfRoomsTo(),
                            getBuiltYearFrom(),
                            getBuiltYearTo());

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/flat/find")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("page", String.valueOf(page))
                                    .param("size", String.valueOf(pageSize))
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(queryBus).send(argumentCaptor.capture());
            verifyQuery(argumentCaptor.getValue(), request, page, pageSize);
        }

        @Test
        @DisplayName("Should search successfully with nullable fields")
        void shouldSearchSuccessfullyWithNullableFields() throws Exception {
            // Given
            final int page = 1;
            final int pageSize = 25;

            final SearchFlatAdvertisementsRequest request =
                    new SearchFlatAdvertisementsRequest(
                            null, null, null, null, null, null, getUUID(), null, null, null, null,
                            null, null, null, null, null, null, null, null);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/flat/find")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("page", String.valueOf(page))
                                    .param("size", String.valueOf(pageSize))
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(queryBus).send(argumentCaptor.capture());
            verifyQuery(argumentCaptor.getValue(), request, page, pageSize);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 44, 55, 125})
        @DisplayName("Should find with boundary page successfully")
        void shouldFindWithBoundaryPageSuccessfully(final int page) throws Exception {
            // Given
            final int pageSize = 25;

            final SearchFlatAdvertisementsRequest request =
                    new SearchFlatAdvertisementsRequest(
                            null, null, null, null, null, null, getUUID(), null, null, null, null,
                            null, null, null, null, null, null, null, null);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/flat/find")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("page", String.valueOf(page))
                                    .param("size", String.valueOf(pageSize))
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(queryBus).send(argumentCaptor.capture());
            verifyQuery(argumentCaptor.getValue(), request, page, pageSize);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 44, 55, 99, 100})
        @DisplayName("Should find with boundary pageSize successfully")
        void shouldFindWithBoundaryPageSizeSuccessfully(final int pageSize) throws Exception {
            // Given
            final int page = 1;

            final SearchFlatAdvertisementsRequest request =
                    new SearchFlatAdvertisementsRequest(
                            null, null, null, null, null, null, getUUID(), null, null, null, null,
                            null, null, null, null, null, null, null, null);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/flat/find")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("page", String.valueOf(page))
                                    .param("size", String.valueOf(pageSize))
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(queryBus).send(argumentCaptor.capture());
            verifyQuery(argumentCaptor.getValue(), request, page, pageSize);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -100, -2500})
        @DisplayName("Should return bad request when page is invalid")
        void shouldReturnBadRequestWhenPageIsInvalid(final int page) throws Exception {
            // Given
            final int pageSize = 25;
            final SearchFlatAdvertisementsRequest request =
                    new SearchFlatAdvertisementsRequest(
                            null, null, null, null, null, null, getUUID(), null, null, null, null,
                            null, null, null, null, null, null, null, null);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/flat/find")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("page", String.valueOf(page))
                                    .param("size", String.valueOf(pageSize))
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(queryBus);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -100, -2500, 101, 250, 2500})
        @DisplayName("Should return bad request when pageSize is invalid")
        void shouldReturnBadRequestWhenPageSizeIsInvalid(final int pageSize) throws Exception {
            // Given
            final int page = 1;
            final SearchFlatAdvertisementsRequest request =
                    new SearchFlatAdvertisementsRequest(
                            null, null, null, null, null, null, getUUID(), null, null, null, null,
                            null, null, null, null, null, null, null, null);

            // When
            // Then
            mockMvc.perform(
                            post("/api/advertisement/flat/find")
                                    .with(csrf())
                                    .with(authentication(getUserAuth()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("page", String.valueOf(page))
                                    .param("size", String.valueOf(pageSize))
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(queryBus);
        }

        private static void verifyQuery(
                final Query query,
                final SearchFlatAdvertisementsRequest request,
                final int page,
                final int pageSize) {

            Assertions.assertThat(query)
                    .isExactlyInstanceOf(SearchFlatAdvertisementsQuery.class)
                    .asInstanceOf(type(SearchFlatAdvertisementsQuery.class))
                    .returns(request.getAreaFrom(), q -> q.criteria().areaFrom())
                    .returns(request.getAreaTo(), q -> q.criteria().areaTo())
                    .returns(request.getPriceFrom(), q -> q.criteria().priceFrom())
                    .returns(request.getPriceTo(), q -> q.criteria().priceTo())
                    .returns(
                            request.getPricePerSquareMeterFrom(),
                            q -> q.criteria().pricePerSquareMeterFrom())
                    .returns(
                            request.getPricePerSquareMeterTo(),
                            q -> q.criteria().pricePerSquareMeterTo())
                    .returns(page, q -> q.criteria().page())
                    .returns(pageSize, q -> q.criteria().pageSize())
                    .extracting(SearchFlatAdvertisementsQuery::criteria)
                    .asInstanceOf(type(SearchFlatAdvertisementsCriteria.class))
                    .returns(request.getFloorFrom(), SearchFlatAdvertisementsCriteria::floorFrom)
                    .returns(request.getFloorTo(), SearchFlatAdvertisementsCriteria::floorTo)
                    .returns(request.getFloorsFrom(), SearchFlatAdvertisementsCriteria::floorsFrom)
                    .returns(request.getFloorsTo(), SearchFlatAdvertisementsCriteria::floorsTo)
                    .returns(
                            request.getNumberOfRoomsFrom(),
                            SearchFlatAdvertisementsCriteria::numberOfRoomsFrom)
                    .returns(
                            request.getNumberOfRoomsTo(),
                            SearchFlatAdvertisementsCriteria::numberOfRoomsTo)
                    .returns(
                            request.getBuiltYearFrom(),
                            SearchFlatAdvertisementsCriteria::builtYearFrom)
                    .returns(
                            request.getBuiltYearTo(), SearchFlatAdvertisementsCriteria::builtYearTo)
                    .returns(request.getDateFrom(), SearchFlatAdvertisementsCriteria::dateFrom)
                    .returns(request.getDateTo(), SearchFlatAdvertisementsCriteria::dateTo)
                    .returns(request.getLocalityId(), SearchFlatAdvertisementsCriteria::localityId)
                    .returns(
                            Mapper.mapEnumCollectionToSet(request.getTypeOfMarkets()),
                            SearchFlatAdvertisementsCriteria::typeOfMarkets)
                    .returns(
                            Mapper.mapEnumCollectionToSet(request.getTypes()),
                            SearchFlatAdvertisementsCriteria::types);
        }

        private static BigDecimal getAreaFrom() {
            return BigDecimal.valueOf(25.35);
        }

        private static BigDecimal getAreaTo() {
            return BigDecimal.valueOf(35.35);
        }

        private static BigDecimal getPriceFrom() {
            return BigDecimal.valueOf(250_000);
        }

        private static BigDecimal getPriceTo() {
            return BigDecimal.valueOf(350_000);
        }

        private static BigDecimal getPricePerSquareMeterFrom() {
            return BigDecimal.valueOf(2500);
        }

        private static BigDecimal getPricePerSquareMeterTo() {
            return BigDecimal.valueOf(3500);
        }

        private static LocalDate getDateFrom() {
            return LocalDate.of(2012, 1, 2);
        }

        private static LocalDate getDateTo() {
            return LocalDate.of(2013, 2, 13);
        }

        private static Set<FlatBuildingType> getFlatTypes() {
            return Set.of(FlatBuildingType.APARTMENT);
        }

        private static Set<TypeOfMarket> getTypeOfMarkets() {
            return Set.of(TypeOfMarket.PRIMARY);
        }

        private static int getNumberOfRoomsFrom() {
            return 1;
        }

        private static int getNumberOfRoomsTo() {
            return 2;
        }

        private static int getFloorFrom() {
            return 11;
        }

        private static int getFloorTo() {
            return 22;
        }

        private static int getFloorsFrom() {
            return 21;
        }

        private static int getFloorsTo() {
            return 32;
        }

        private static int getBuiltYearFrom() {
            return 2011;
        }

        private static int getBuiltYearTo() {
            return 2012;
        }
    }

    @Nested
    final class SearchDetailsBySlugTests {

        @Test
        @DisplayName("Should find successfully")
        void shouldFindSuccessfully() throws Exception {
            // Given
            final String slug = getSlug();

            // When
            // Then
            mockMvc.perform(
                            get("/api/advertisement/flat/{slug}", slug)
                                    .with(csrf())
                                    .with(authentication(getUserAuth())))
                    .andExpect(status().isOk());

            verify(queryBus).send(new FlatAdvertisementDetailsQuery(slug));
        }

        @ParameterizedTest
        @MethodSource("invalidSlugDataProvider")
        @DisplayName("Should return bad request when slug is invalid")
        void shouldReturnBadRequestWhenSlugIsInvalid(final String slug) throws Exception {
            // Given
            // When
            // Then
            mockMvc.perform(
                            get("/api/advertisement/flat/{slug}", slug)
                                    .with(csrf())
                                    .with(authentication(getUserAuth())))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(queryBus);
        }

        private static Stream<Arguments> invalidSlugDataProvider() {
            return Stream.of(
                    Arguments.of(RandomStringUtils.secure().nextAlphanumeric(9)),
                    Arguments.of(RandomStringUtils.secure().nextAlphanumeric(109)));
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

    private static int getNumberOfRooms() {
        return 3;
    }

    private static int getFloor() {
        return 1;
    }

    private static int getFloors() {
        return 2;
    }

    private static int getBuiltYear() {
        return 2011;
    }

    private static TypeOfMarket getTypeOfMarket() {
        return TypeOfMarket.PRIMARY;
    }

    private static FlatBuildingType getFlatBuildingType() {
        return FlatBuildingType.TENEMENT;
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
