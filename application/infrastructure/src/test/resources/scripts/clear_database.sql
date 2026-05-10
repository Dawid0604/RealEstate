TRUNCATE TABLE
    localities,
    users,

    flat_advertisements,
    house_advertisements,
    commercial_advertisements,
    plot_advertisements,

    flat_advertisements_claims,
    house_advertisements_claims,
    commercial_advertisements_claims,
    plot_advertisements_claims,

    flat_advertisements_photos,
    house_advertisements_photos,
    commercial_advertisements_photos,
    plot_advertisements_photos
RESTART IDENTITY CASCADE;
