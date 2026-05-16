/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.custom.jwt")
public record JwtProperties(
        String secret, long accessTokenExpiration, long refreshTokenExpiration) {

    static final String ROLE_CLAIM = "role";
    static final String TOKEN_TYPE_CLAIM = "type";
    static final String ACCESS_TOKEN_TYPE = "access";
    static final String REFRESH_TOKEN_TYPE = "refresh";
}
