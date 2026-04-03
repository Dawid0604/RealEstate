package pl.dawid0604.realestate.application.dto;

import java.util.List;
import java.util.Objects;

public final class PagedResult<T> {
    private final List<T> items;
    private final int page;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    private PagedResult(
            final List<T> items,
            final int page,
            final int pageSize,
            final long totalElements,
            final int totalPages) {

        requireNonNegative(page, "page");
        requireNonNegative(pageSize, "pageSize");
        requireNonNegative(totalPages, "totalPages");
        requireNonNegativeTotalElements(totalElements);

        this.items = Objects.requireNonNullElse(items, List.of());
        this.page = page;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public static <T> PagedResult<T> of(
            final List<T> items,
            final int page,
            final int pageSize,
            final long totalElements,
            final int totalPages) {

        return new PagedResult<>(items, page, pageSize, totalElements, totalPages);
    }

    public static <T> PagedResult<T> empty(final int page, final int pageSize) {
        return new PagedResult<>(null, page, pageSize, 0, 0);
    }

    private static void requireNonNegative(final int value, final String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative");
        }
    }

    private static void requireNonNegativeTotalElements(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Total elements cannot be negative");
        }
    }

    public List<T> items() {
        return List.copyOf(items);
    }

    public int page() {
        return page;
    }

    public int pageSize() {
        return pageSize;
    }

    public long totalElements() {
        return totalElements;
    }

    public int totalPages() {
        return totalPages;
    }

    public boolean hasNext() {
        return page < totalPages - 1;
    }

    public boolean hasPrevious() {
        return page > 0;
    }
}
