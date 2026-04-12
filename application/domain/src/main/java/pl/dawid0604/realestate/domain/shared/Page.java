/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared;

import static java.util.Collections.emptyList;

import java.util.List;

public record Page<T>(List<T> items, long totalElements) {

    public Page {
        items = items != null ? List.copyOf(items) : emptyList();
    }

    @Override
    public List<T> items() {
        return List.copyOf(items);
    }
}
