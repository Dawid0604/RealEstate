/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.config;

import static lombok.AccessLevel.PACKAGE;

import lombok.NoArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@NoArgsConstructor(access = PACKAGE)
class JpaConfig {}
