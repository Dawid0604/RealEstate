/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config.security;

import static lombok.AccessLevel.PACKAGE;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import pl.dawid0604.realestate.domain.port.out.TokenRepository;
import pl.dawid0604.realestate.domain.shared.exception.ExpiredTokenException;
import pl.dawid0604.realestate.domain.shared.exception.InvalidTokenException;

import java.io.IOException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class JwtAuthFilter extends OncePerRequestFilter {
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            @Nonnull final HttpServletRequest request,
            @Nonnull final HttpServletResponse response,
            @Nonnull final FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!Strings.CS.startsWith(header, BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = header.substring(7);
            requireAccessTokenType(token);

            final var authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (InvalidTokenException | ExpiredTokenException ex) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private void requireAccessTokenType(final String token) {
        if (!tokenRepository.isAccessToken(token)) {
            throw new InvalidTokenException("Invalid token type, expected access token");
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(final String token) {
        final String userEmail = tokenRepository.getUserEmail(token);
        final var userDetails = userDetailsService.loadUserByUsername(userEmail);

        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }
}
