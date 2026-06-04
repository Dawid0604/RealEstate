/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.test.context.jdbc.Sql;

@Target({METHOD, ElementType.TYPE})
@Retention(RUNTIME)
@Sql(scripts = "/scripts/clear_database.sql", executionPhase = BEFORE_TEST_METHOD)
public @interface ClearDatabase {}
