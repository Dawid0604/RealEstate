/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.test.context.jdbc.Sql;

@Target({METHOD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Sql(
        statements = "ALTER TABLE plot_advertisements DISABLE TRIGGER ALL",
        executionPhase = BEFORE_TEST_METHOD)
@Sql(
        statements = "ALTER TABLE plot_advertisements ENABLE TRIGGER ALL",
        executionPhase = AFTER_TEST_METHOD)
@interface DisablePlotConstraints {}
