/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.token;

import static lombok.AccessLevel.PACKAGE;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.port.out.TokenRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.ExpiredTokenException;
import pl.dawid0604.realestate.domain.shared.exception.InvalidTokenException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class TokenAdapter implements TokenRepository {
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;

    @Override
    public String getUserEmail(final String token) {
        requireNotBlankToken(token);
        return extractClaims(token).getSubject();
    }

    @Override
    public boolean isAccessToken(final String token) {
        return tokenTypeIs(token, JwtProperties.ACCESS_TOKEN_TYPE);
    }

    @Override
    public boolean isRefreshToken(final String token) {
        return tokenTypeIs(token, JwtProperties.REFRESH_TOKEN_TYPE);
    }

    @Override
    public String generateAccessToken(final String userEmail) {
        requireNotBlankEmail(userEmail);

        final UserRole userRole =
                userRepository
                        .findUserRoleByEmail(userEmail)
                        .orElseThrow(() -> new UserNotFoundException(userEmail));

        return Jwts.builder()
                .subject(userEmail)
                .claim(JwtProperties.ROLE_CLAIM, userRole)
                .issuedAt(Date.from(Instant.now()))
                .expiration(
                        Date.from(Instant.now().plusSeconds(jwtProperties.accessTokenExpiration())))
                .signWith(signingKey())
                .claim(JwtProperties.TOKEN_TYPE_CLAIM, JwtProperties.ACCESS_TOKEN_TYPE)
                .compact();
    }

    @Override
    public String generateRefreshToken(final String userEmail) {
        requireNotBlankEmail(userEmail);

        if (!userRepository.existsByEmail(userEmail)) {
            throw new UserNotFoundException(userEmail);
        }

        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(Date.from(Instant.now()))
                .expiration(
                        Date.from(
                                Instant.now().plusSeconds(jwtProperties.refreshTokenExpiration())))
                .signWith(signingKey())
                .claim(JwtProperties.TOKEN_TYPE_CLAIM, JwtProperties.REFRESH_TOKEN_TYPE)
                .compact();
    }

    @Override
    public Instant getTokenExpirationDate(final String token) {
        requireNotBlankToken(token);
        return extractClaims(token).getExpiration().toInstant();
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
    }

    private boolean tokenTypeIs(final String token, final String requiredType) {
        requireNotBlankToken(token);

        final String type = extractClaims(token).get(JwtProperties.TOKEN_TYPE_CLAIM, String.class);
        return Objects.equals(type, requiredType);
    }

    private static void requireNotBlankEmail(final String email) {
        if (isBlank(email)) {
            throw new IllegalArgumentException("Email cannot be blank");
        }
    }

    private static void requireNotBlankToken(final String token) {
        if (isBlank(token)) {
            throw new IllegalArgumentException("Token cannot be blank");
        }
    }

    private Claims extractClaims(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException exception) {
            throw new ExpiredTokenException(exception);

        } catch (JwtException exception) {
            throw new InvalidTokenException(exception);
        }
    }
}
