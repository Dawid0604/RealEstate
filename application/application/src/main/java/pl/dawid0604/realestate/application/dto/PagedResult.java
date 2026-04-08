/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

public final class PagedResult<T> {
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;

    private final List<T> items;
    @Getter private final int page;
    @Getter private final int pageSize;
    @Getter private final long totalElements;
    @Getter private final int totalPages;

    private PagedResult(
            final List<T> incomingItems,
            final int incomingPage,
            final int incomingPageSize,
            final long incomingTotalElements) {

        requirePositivePage(incomingPage);
        requireValidPageSize(incomingPageSize);
        requirePositiveTotalElements(incomingTotalElements);

        this.items = Objects.requireNonNullElse(incomingItems, List.of());
        this.page = incomingPage;
        this.pageSize = incomingPageSize;
        this.totalElements = incomingTotalElements;
        this.totalPages = calculateTotalPages(incomingTotalElements, incomingPageSize);
    }

    public static <T> PagedResult<T> of(
            final List<T> incomingItems,
            final int incomingPage,
            final int incomingPageSize,
            final long incomingTotalElements) {

        return new PagedResult<>(
                incomingItems, incomingPage, incomingPageSize, incomingTotalElements);
    }

    public static <T> PagedResult<T> empty(final int incomingPage, final int incomingPageSize) {
        return new PagedResult<>(null, incomingPage, incomingPageSize, 0);
    }

    private static int calculateTotalPages(final long totalElements, final int pageSize) {
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    private static void requirePositivePage(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }
    }

    private static void requirePositiveTotalElements(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Total elements cannot be negative");
        }
    }

    private static void requireValidPageSize(final int pageSize) {
        if (pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(
                    "Page size must be between " + MIN_PAGE_SIZE + " and " + MAX_PAGE_SIZE);
        }
    }

    public List<T> getItems() {
        return List.copyOf(items);
    }

    public boolean hasNext() {
        return page < totalPages;
    }

    public boolean hasPrevious() {
        return page > 0;
    }
}
