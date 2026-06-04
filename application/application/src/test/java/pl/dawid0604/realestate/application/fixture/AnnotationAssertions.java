/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.fixture;

import java.lang.annotation.Annotation;
import java.util.List;

import org.assertj.core.api.Assertions;

import lombok.experimental.UtilityClass;
import pl.dawid0604.realestate.application.command.Command;
import pl.dawid0604.realestate.application.query.Query;

@UtilityClass
public class AnnotationAssertions {

    public static void assertFieldAnnotations(
            final Class<?> clazz,
            final String fieldName,
            final List<Class<? extends Annotation>> requiredAnnotations) {

        Assertions.assertThat(clazz)
                .hasDeclaredFields(fieldName)
                .satisfies(
                        c -> {
                            var field = c.getDeclaredField(fieldName);

                            requiredAnnotations.forEach(
                                    annotation ->
                                            Assertions.assertThat(
                                                            field.isAnnotationPresent(annotation))
                                                    .isTrue());
                        });
    }

    public static void assertImplementsCommandInterface(final Class<?> clazz) {
        Assertions.assertThat(clazz).isAssignableTo(Command.class);
    }

    public static void assertImplementsQueryInterface(final Class<?> clazz) {
        Assertions.assertThat(clazz).isAssignableTo(Query.class);
    }
}
