/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.config;

import static lombok.AccessLevel.PACKAGE;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.NoArgsConstructor;

@Configuration
@EnableJpaAuditing
@NoArgsConstructor(access = PACKAGE)
@EntityScan(basePackages = "pl.dawid0604.realestate.infrastructure")
@EnableJpaRepositories(basePackages = "pl.dawid0604.realestate.infrastructure")
class JpaConfig {}
