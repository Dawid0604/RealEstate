/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

class AuthenticationEntryPointCustomTest {
    private AuthenticationEntryPointCustom handler;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.handler = new AuthenticationEntryPointCustom(objectMapper);
    }

    @Test
    @DisplayName("Should handle successfully")
    void shouldHandleSuccessfully() throws IOException {
        // Given
        final HttpServletRequest mockRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        final AuthenticationException exception = new AccountExpiredException("Exception");

        // When
        handler.commence(mockRequest, mockResponse, exception);

        // Then
        Assertions.assertThat(mockResponse)
                .returns(HttpStatus.UNAUTHORIZED.value(), HttpServletResponse::getStatus);

        Assertions.assertThat(
                        objectMapper.readValue(
                                mockResponse.getContentAsString(), ProblemDetail.class))
                .isNotNull();
    }
}
