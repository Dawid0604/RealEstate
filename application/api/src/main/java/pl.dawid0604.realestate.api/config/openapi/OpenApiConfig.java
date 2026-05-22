/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config.openapi;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

import static lombok.AccessLevel.PACKAGE;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(access = PACKAGE)
class OpenApiConfig {
    private final OpenApiProperties properties;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(getInfo()).components(getComponents());
    }

    private Components getComponents() {
        final SecurityScheme securityScheme =
                new SecurityScheme()
                        .type(HTTP)
                        .scheme(OpenApiProperties.AUTHENTICATION_SCHEME)
                        .bearerFormat(OpenApiProperties.BEARER_FORMAT);

        return new Components()
                .addSecuritySchemes(OpenApiProperties.AUTHENTICATION_REQUIREMENT, securityScheme);
    }

    private Info getInfo() {
        return new Info()
                .title(properties.title())
                .description(properties.description())
                .version(properties.version());
    }
}
