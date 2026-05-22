/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config.openapi;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.custom.openapi")
public record OpenApiProperties(String title, String description, String version) {

    public static final String AUTHENTICATION_REQUIREMENT = "Bearer";
    public static final String AUTHENTICATION_SCHEME = "bearer";
    public static final String BEARER_FORMAT = "JWT";
}
