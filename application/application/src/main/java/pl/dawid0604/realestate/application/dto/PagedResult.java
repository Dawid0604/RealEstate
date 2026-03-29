package pl.dawid0604.realestate.application.dto;

import java.util.List;

public record PagedResult<T>(List<T> items, int page, int pageSize) {

    public PagedResult {
        if (items == null) {
            items = List.of();
        }

        requirePositive(page, "page");
        requirePositive(pageSize, "pageSize");
    }

    private static void requirePositive(final int value, final String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public List<T> items() {
        return List.copyOf(items);
    }
}
