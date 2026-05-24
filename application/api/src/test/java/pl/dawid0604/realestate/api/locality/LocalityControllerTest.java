/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.locality;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import pl.dawid0604.realestate.api.locality.request.CreateLocalityRequest;
import pl.dawid0604.realestate.application.bus.CommandBus;
import pl.dawid0604.realestate.application.bus.QueryBus;
import pl.dawid0604.realestate.application.command.CreateLocalityCommand;
import pl.dawid0604.realestate.application.dto.locality.LocalityDto;
import pl.dawid0604.realestate.application.query.FindLocalitiesQuery;
import pl.dawid0604.realestate.application.query.FindLocalityByIdQuery;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;

import java.util.List;
import java.util.UUID;

@EnableMethodSecurity
@WebMvcTest(LocalityController.class)
class LocalityControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private TokenRepository tokenRepository;
    @MockitoBean private CommandBus commandBus;
    @MockitoBean private QueryBus queryBus;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Nested
    final class CreateLocalityTests {

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Should create")
        void shouldCreate() throws Exception {
            // Given
            final CreateLocalityRequest request = new CreateLocalityRequest("Warsaw");
            final UUID id = UUID.randomUUID();

            given(commandBus.send(new CreateLocalityCommand(request.name()))).willReturn(id);

            // When
            // Then
            mockMvc.perform(
                            post("/api/locality")
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/api/locality/" + id));
        }

        @Test
        @WithMockUser
        @DisplayName("Should deny when user is not admin")
        void shouldDenyWhenUserIsNotAdmin() throws Exception {
            // Given
            final CreateLocalityRequest request = new CreateLocalityRequest("Warsaw");
            final UUID id = UUID.randomUUID();

            given(commandBus.send(new CreateLocalityCommand(request.name()))).willReturn(id);

            // When
            // Then
            mockMvc.perform(
                            post("/api/locality")
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }

        @WithMockUser
        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return bad request with invalid input")
        void shouldReturnBadRequestWithInvalidInput(final String name) throws Exception {
            // Given
            final CreateLocalityRequest request = new CreateLocalityRequest(name);
            final UUID id = UUID.randomUUID();

            given(commandBus.send(new CreateLocalityCommand(request.name()))).willReturn(id);

            // When
            // Then
            mockMvc.perform(
                            post("/api/locality")
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(OBJECT_MAPPER.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    final class FindAllTests {

        @Test
        @WithMockUser
        @DisplayName("Should find all successfully")
        void shouldFindAllSuccessfully() throws Exception {
            // Given
            final List<LocalityDto> localities =
                    List.of(
                            new LocalityDto(UUID.randomUUID(), "Warsaw"),
                            new LocalityDto(UUID.randomUUID(), "Krakow"));

            given(queryBus.send(new FindLocalitiesQuery())).willReturn(localities);

            // When
            // Then
            mockMvc.perform(get("/api/locality").with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2));
        }
    }

    @Nested
    final class FindByIdTests {

        @Test
        @WithMockUser
        @DisplayName("Should find by id successfully")
        void shouldFindByIdSuccessfully() throws Exception {
            // Given
            final UUID id = UUID.randomUUID();
            final LocalityDto locality = new LocalityDto(id, "Warsaw");

            given(queryBus.send(new FindLocalityByIdQuery(id))).willReturn(locality);

            // When
            // Then
            mockMvc.perform(get("/api/locality/{id}", id).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(locality.id().toString()))
                    .andExpect(jsonPath("$.name").value(locality.name()));
        }
    }
}
