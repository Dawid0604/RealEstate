/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.custom.jwt")
public record JwtProperties(
        String secret, long accessTokenExpiration, long refreshTokenExpiration) {

    static final String ROLE_CLAIM = "role";
}
