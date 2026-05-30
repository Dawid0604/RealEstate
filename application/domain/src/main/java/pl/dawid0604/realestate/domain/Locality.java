/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Objects;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class Locality {
    private final Identifier id;
    private final String name;

    private Locality(final Identifier id, final String name) {
        if (id == null) {
            throw new InvalidArgumentValueException("Id cannot be null");
        }

        if (isBlank(name)) {
            throw new InvalidArgumentValueException("Name cannot be blank");
        }

        this.id = id;
        this.name = name;
    }

    public static Locality create(final String name) {
        return new Locality(Identifier.generate(), name);
    }

    public static Locality reconstitute(final Identifier id, final String name) {
        return new Locality(id, name);
    }

    public Identifier getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Locality that && Objects.equals(that.id, this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
