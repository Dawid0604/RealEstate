/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import pl.dawid0604.realestate.api.config.openapi.OpenApiProperties;
import pl.dawid0604.realestate.infrastructure.token.JwtProperties;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, OpenApiProperties.class})
class PropertiesConfig {}
