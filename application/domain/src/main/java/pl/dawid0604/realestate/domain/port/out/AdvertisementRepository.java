/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import pl.dawid0604.realestate.domain.Advertisement;

import java.util.Optional;

public interface AdvertisementRepository {
    void save(Advertisement advertisement);

    Optional<Advertisement> findBySlug(String slug);
}
