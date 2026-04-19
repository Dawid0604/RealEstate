/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidEmail;

class ActivateUserCommandTest {

    @Test
    @DisplayName("Should implement Command interface")
    void shouldImplementsCommandInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsCommandInterface(ActivateUserCommand.class);
    }

    @Test
    @DisplayName("Should have email field with required annotations")
    void shouldHaveEmailFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertFieldAnnotations(
                ActivateUserCommand.class, "email", List.of(ValidEmail.class));
    }
}
