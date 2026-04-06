/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import java.util.Optional;

import pl.dawid0604.realestate.domain.Advertisement;

public interface AdvertisementRepository {
    void save(Advertisement advertisement);

    Optional<Advertisement> findBySlug(String slug);
}
