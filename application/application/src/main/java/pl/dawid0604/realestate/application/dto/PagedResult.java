/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

public final class PagedResult<T> {
    private final List<T> items;
    @Getter private final int page;
    @Getter private final int pageSize;
    @Getter private final long totalElements;
    @Getter private final int totalPages;

    private PagedResult(
            final List<T> incomingItems,
            final int incomingPage,
            final int incomingPageSize,
            final long incomingTotalElements,
            final int incomingTotalPages) {

        requireNonNegative(incomingPage, "incomingPage");
        requireNonNegative(incomingPageSize, "incomingPageSize");
        requireNonNegative(incomingTotalPages, "incomingTotalPages");
        requireNonNegativeTotalElements(incomingTotalElements);

        this.items = Objects.requireNonNullElse(incomingItems, List.of());
        this.page = incomingPage;
        this.pageSize = incomingPageSize;
        this.totalElements = incomingTotalElements;
        this.totalPages = incomingTotalPages;
    }

    public static <T> PagedResult<T> of(
            final List<T> incomingItems,
            final int incomingPage,
            final int incomingPageSize,
            final long incomingTotalElements,
            final int incomingTotalPages) {

        return new PagedResult<>(
                incomingItems,
                incomingPage,
                incomingPageSize,
                incomingTotalElements,
                incomingTotalPages);
    }

    public static <T> PagedResult<T> empty(final int incomingPage, final int incomingPageSize) {
        return new PagedResult<>(null, incomingPage, incomingPageSize, 0, 0);
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

    public List<T> getItems() {
        return List.copyOf(items);
    }

    public boolean hasNext() {
        return page < totalPages - 1;
    }

    public boolean hasPrevious() {
        return page > 0;
    }
}
