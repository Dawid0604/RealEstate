/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared;

import java.util.List;
import java.util.Objects;

public final class Page<T> {
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;

    private final List<T> items;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    private Page(
            final List<T> incomingItems,
            final int incomingPage,
            final int incomingPageSize,
            final long incomingTotalElements) {

        requirePositivePage(incomingPage);
        requireValidPageSize(incomingPageSize);
        requirePositiveTotalElements(incomingTotalElements);

        this.items = Objects.requireNonNullElse(incomingItems, List.of());
        this.pageNumber = incomingPage;
        this.pageSize = incomingPageSize;
        this.totalElements = incomingTotalElements;
        this.totalPages = calculateTotalPages(incomingTotalElements, incomingPageSize);
    }

    public static <T> Page<T> of(
            final List<T> incomingItems,
            final int incomingPage,
            final int incomingPageSize,
            final long incomingTotalElements) {

        return new Page<>(incomingItems, incomingPage, incomingPageSize, incomingTotalElements);
    }

    public static <T> Page<T> empty(final int incomingPage, final int incomingPageSize) {
        return new Page<>(null, incomingPage, incomingPageSize, 0);
    }

    private static int calculateTotalPages(final long totalElements, final int pageSize) {
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    private static void requirePositivePage(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
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
        return pageNumber < totalPages;
    }

    public boolean hasPrevious() {
        return pageNumber > 0;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
