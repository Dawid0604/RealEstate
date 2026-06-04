/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config.security;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {
    @Mock private TokenRepository tokenRepository;
    @Mock private UserDetailsService userDetailsService;
    private JwtAuthFilter filter;

    @BeforeEach
    void setUp() {
        this.filter = new JwtAuthFilter(tokenRepository, userDetailsService);
    }

    @Test
    @DisplayName("Should do filter when token is not present")
    void shouldDoFilterWhenTokenIsNotPresent() throws ServletException, IOException {
        // Given
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        final FilterChain mockFilterChain = mock();

        // When
        filter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Then
        verifyNoInteractions(tokenRepository, userDetailsService);
    }

    @Test
    @DisplayName("Should handle invalid token and without security context")
    void shouldHandleInvalidTokenAndWithoutSecurityContext() throws ServletException, IOException {
        // Given
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        final FilterChain mockFilterChain = mock();

        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, getBearerToken());
        given(tokenRepository.isAccessToken(getToken())).willReturn(false);

        // When
        filter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Then
        Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(userDetailsService);
    }

    @Test
    @DisplayName("Should handle invalid token and with security context")
    void shouldHandleInvalidTokenAndWithSecurityContext() throws ServletException, IOException {

        // Given
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        final FilterChain mockFilterChain = mock();

        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, getBearerToken());

        SecurityContextHolder.getContext()
                .setAuthentication(mock(UsernamePasswordAuthenticationToken.class));

        // When
        filter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Then
        Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(userDetailsService);
        verify(tokenRepository).isAccessToken(getToken());
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        final FilterChain mockFilterChain = mock();

        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, getBearerToken());
        given(tokenRepository.isAccessToken(getToken())).willReturn(true);
        given(tokenRepository.getUserEmail(getToken())).willReturn(getUsername());
        given(userDetailsService.loadUserByUsername(getUsername()))
                .willThrow(new UsernameNotFoundException("Exception"));

        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> filter.doFilterInternal(mockRequest, mockResponse, mockFilterChain))
                .isExactlyInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("Should authenticate successfully")
    void shouldAuthenticateSuccessfully() throws ServletException, IOException {
        // Given
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        final MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        final FilterChain mockFilterChain = mock();
        final AuthenticatedUser user = new AuthenticatedUser(getUsername(), UserRole.ROLE_USER);

        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, getBearerToken());
        given(tokenRepository.isAccessToken(getToken())).willReturn(true);
        given(tokenRepository.getUserEmail(getToken())).willReturn(getUsername());
        given(userDetailsService.loadUserByUsername(getUsername())).willReturn(user);

        // When
        filter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Then
        Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNotNull()
                .isInstanceOf(UsernamePasswordAuthenticationToken.class)
                .asInstanceOf(
                        InstanceOfAssertFactories.type(UsernamePasswordAuthenticationToken.class))
                .extracting(UsernamePasswordAuthenticationToken::getPrincipal)
                .isEqualTo(user);
    }

    private static String getUsername() {
        return "userEmail@mail.com";
    }

    private static String getToken() {
        return "any-token";
    }

    private static String getBearerToken() {
        return "Bearer " + getToken();
    }
}
