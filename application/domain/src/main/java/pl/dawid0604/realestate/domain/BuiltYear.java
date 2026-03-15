/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

public record BuiltYear(int value) {
    private static final int MIN_BUILT_YEAR = 1900;

    public BuiltYear {
        if (value < 0) {
            throw new IllegalArgumentException("Built year cannot be negative");
        }

        if (value < MIN_BUILT_YEAR) {
            throw new IllegalArgumentException("Built year cannot be before " + MIN_BUILT_YEAR);
        }
    }
}
